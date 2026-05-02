package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.config.properties.MuhouAppProperties;
import com.muhou.backend.common.exception.BizException;
import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.common.support.StatusTextHelper;
import com.muhou.backend.common.util.MoneyUtils;
import com.muhou.backend.common.util.TimeUtils;
import com.muhou.backend.infrastructure.client.PaymentCreateResult;
import com.muhou.backend.infrastructure.client.WechatPaymentGateway;
import com.muhou.backend.infrastructure.persistence.entity.OrderPaymentEntity;
import com.muhou.backend.infrastructure.persistence.entity.OrderReviewEntity;
import com.muhou.backend.infrastructure.persistence.entity.ProjectSchemeEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropEntity;
import com.muhou.backend.infrastructure.persistence.entity.RentalOrderEntity;
import com.muhou.backend.infrastructure.persistence.entity.RentalOrderItemEntity;
import com.muhou.backend.infrastructure.persistence.entity.UserEntity;
import com.muhou.backend.infrastructure.persistence.mapper.OrderPaymentMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderReviewMapper;
import com.muhou.backend.infrastructure.persistence.mapper.ProjectSchemeItemMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropMapper;
import com.muhou.backend.infrastructure.persistence.mapper.RentalOrderItemMapper;
import com.muhou.backend.infrastructure.persistence.mapper.RentalOrderMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserMapper;
import com.muhou.backend.web.request.CreateOrderRequest;
import com.muhou.backend.web.request.OrderPayRequest;
import com.muhou.backend.web.request.OrderReviewRequest;
import com.muhou.backend.web.response.OrderItemResponse;
import com.muhou.backend.web.response.OrderResponse;
import com.muhou.backend.web.response.PayResponse;
import com.muhou.backend.web.response.PropResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderApplicationService {

    private final RentalOrderMapper rentalOrderMapper;
    private final RentalOrderItemMapper rentalOrderItemMapper;
    private final OrderPaymentMapper orderPaymentMapper;
    private final OrderReviewMapper orderReviewMapper;
    private final ProjectSchemeItemMapper projectSchemeItemMapper;
    private final PropMapper propMapper;
    private final UserMapper userMapper;
    private final ProjectApplicationService projectApplicationService;
    private final PropApplicationService propApplicationService;
    private final WechatPaymentGateway wechatPaymentGateway;
    private final MuhouAppProperties properties;
    private final CurrentUserSupport currentUserSupport;

    public OrderApplicationService(RentalOrderMapper rentalOrderMapper,
                                   RentalOrderItemMapper rentalOrderItemMapper,
                                   OrderPaymentMapper orderPaymentMapper,
                                   OrderReviewMapper orderReviewMapper,
                                   ProjectSchemeItemMapper projectSchemeItemMapper,
                                   PropMapper propMapper,
                                   UserMapper userMapper,
                                   ProjectApplicationService projectApplicationService,
                                   PropApplicationService propApplicationService,
                                   WechatPaymentGateway wechatPaymentGateway,
                                   MuhouAppProperties properties,
                                   CurrentUserSupport currentUserSupport) {
        this.rentalOrderMapper = rentalOrderMapper;
        this.rentalOrderItemMapper = rentalOrderItemMapper;
        this.orderPaymentMapper = orderPaymentMapper;
        this.orderReviewMapper = orderReviewMapper;
        this.projectSchemeItemMapper = projectSchemeItemMapper;
        this.propMapper = propMapper;
        this.userMapper = userMapper;
        this.projectApplicationService = projectApplicationService;
        this.propApplicationService = propApplicationService;
        this.wechatPaymentGateway = wechatPaymentGateway;
        this.properties = properties;
        this.currentUserSupport = currentUserSupport;
    }

    public List<OrderResponse> listOrders(String role) {
        String currentRole = requireCurrentRole();
        if (role != null && !role.isBlank() && !currentRole.equals(role)) {
            throw new BizException(ResultCode.FORBIDDEN, "不允许越权切换订单视角");
        }
        String resolvedRole = currentRole;
        long userId = currentUserSupport.requireCurrentUserId();
        return rentalOrderMapper.selectByRole(resolvedRole, userId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public OrderResponse getOrder(Long orderId) {
        return toResponse(getAccessibleOrder(orderId));
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Long demanderUserId = currentDemanderUserId();
        ProjectSchemeEntity project = projectApplicationService.getOwnedProject(request.getProjectId());
        if (!"editing".equals(project.getProjectStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前方案已下单，不能重复创建订单");
        }

        List<Long> propIds = projectSchemeItemMapper.selectPropIdsByProjectId(project.getId());
        if (propIds.isEmpty()) {
            throw new BizException(ResultCode.CONFLICT, "方案中没有可下单的道具");
        }

        List<PropEntity> props = propMapper.selectByIds(propIds);
        List<String> blocked = props.stream()
            .filter(item -> !StatusTextHelper.isPropOrderable(item.getPropStatus()))
            .map(PropEntity::getPropName)
            .toList();
        if (!blocked.isEmpty()) {
            throw new BizException(ResultCode.CONFLICT, "以下道具当前不可下单: " + String.join("、", blocked));
        }

        Long supplierUserId = ensureSingleSupplier(props);
        LocalDate rentalStartDate = request.getRentalStartDate();
        LocalDate rentalEndDate = request.getRentalEndDate();
        int rentalDays = calculateRentalDays(rentalStartDate, rentalEndDate);
        int dailyRentAmountFen = props.stream().mapToInt(item -> defaultZero(item.getDailyRentPriceFen())).sum();
        long rentAmountFenLong = (long) dailyRentAmountFen * rentalDays;
        if (rentAmountFenLong > Integer.MAX_VALUE) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "租赁金额超出系统限制");
        }
        int rentAmountFen = (int) rentAmountFenLong;
        int depositAmountFen = props.stream().mapToInt(item -> defaultZero(item.getDepositAmountFen())).sum();

        RentalOrderEntity order = new RentalOrderEntity();
        order.setOrderNo("ORD-" + System.currentTimeMillis());
        order.setProjectId(project.getId());
        order.setDemanderUserId(demanderUserId);
        order.setSupplierUserId(supplierUserId);
        order.setOrderStatus("pending_factory_confirm");
        order.setRentalDays(rentalDays);
        order.setRentalStartDate(rentalStartDate);
        order.setRentalEndDate(rentalEndDate);
        order.setContactAddress(normalizeRequiredText(request.getContactAddress(), "使用地址不能为空"));
        order.setUseScene(normalizeRequiredText(request.getUseScene(), "使用场景不能为空"));
        order.setSpecialRemark(normalizeOptionalText(request.getSpecialRemark()));
        order.setRentAmountFen(rentAmountFen);
        order.setDepositAmountFen(depositAmountFen);
        order.setTotalAmountFen(rentAmountFen + depositAmountFen);
        order.setPayStatus("unpaid");
        order.setRefundStatus("none");
        order.setTotalPaidFen(0);
        order.setTotalRefundedFen(0);
        order.setConfirmDeadlineAt(LocalDateTime.now().plusHours(1));
        order.setRemark("由前端方案提交生成");
        order.setCreatedBy(demanderUserId);
        rentalOrderMapper.insert(order);

        List<RentalOrderItemEntity> orderItems = props.stream().map(prop -> {
            RentalOrderItemEntity item = new RentalOrderItemEntity();
            item.setOrderId(order.getId());
            item.setPropId(prop.getId());
            item.setPropNameSnapshot(prop.getPropName());
            item.setImageUrlSnapshot(prop.getImageUrl());
            item.setDailyRentPriceFenSnapshot(prop.getDailyRentPriceFen());
            item.setDepositAmountFenSnapshot(prop.getDepositAmountFen());
            item.setOutboundStatus("pending");
            item.setReturnStatus("pending");
            return item;
        }).collect(Collectors.toList());
        rentalOrderItemMapper.batchInsert(orderItems);
        return getOrder(order.getId());
    }

    @Transactional
    public PayResponse payOrder(Long orderId, OrderPayRequest request) {
        RentalOrderEntity order = getAccessibleOrder(orderId);
        if (!"pending_factory_confirm".equals(order.getOrderStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前订单状态不能发起支付");
        }
        if ("paid".equals(order.getPayStatus())) {
            PayResponse response = new PayResponse();
            response.setOrderId(orderId);
            response.setPayStatus("paid");
            response.setMock(properties.getPayment().isMockEnabled());
            return response;
        }
        if (!"unpaid".equals(order.getPayStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前订单支付状态不能重复发起支付");
        }

        OrderPaymentEntity payment = new OrderPaymentEntity();
        payment.setPaymentNo("PAY-" + System.currentTimeMillis());
        payment.setOrderId(orderId);
        payment.setPayScene("rent_deposit");
        payment.setPaymentChannel("wechat_miniapp");
        payment.setPaymentStatus("created");
        payment.setMerchantMchid(properties.getPayment().getMchId());
        payment.setAppid(blankToDefault(properties.getPayment().getAppId(), "demo-miniapp"));
        payment.setOpenid(blankToDefault(request.getOpenId(), "mock-openid"));
        payment.setMerchantOutTradeNo("OUT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        payment.setDescription("MUHOU 道具租赁订单支付");
        payment.setAmountFen(order.getTotalAmountFen());
        payment.setCurrency("CNY");
        payment.setClientIp(request.getClientIp());
        payment.setNotifyUrl(properties.getPayment().getNotifyUrl());
        payment.setAttachData("orderId=" + orderId);
        payment.setTimeExpire(LocalDateTime.now().plusMinutes(15));
        orderPaymentMapper.insert(payment);

        PaymentCreateResult payResult = wechatPaymentGateway.createPayment(payment);
        LocalDateTime now = LocalDateTime.now();
        orderPaymentMapper.markSuccess(payment.getId(), "success", payResult.getTransactionId(), payResult.getPrepayId(), now, payResult.getRawResponse());
        rentalOrderMapper.markPaid(orderId, payment.getId(), "paid", order.getTotalAmountFen(), now);

        List<RentalOrderItemEntity> items = rentalOrderItemMapper.selectByOrderId(orderId);
        for (RentalOrderItemEntity item : items) {
            propMapper.updateStatus(item.getPropId(), "locked");
        }
        projectApplicationService.markProjectOrdered(order.getProjectId());

        PayResponse response = new PayResponse();
        response.setOrderId(orderId);
        response.setPaymentId(payment.getId());
        response.setPaymentNo(payment.getPaymentNo());
        response.setMerchantOutTradeNo(payment.getMerchantOutTradeNo());
        response.setPrepayId(payResult.getPrepayId());
        response.setTransactionId(payResult.getTransactionId());
        response.setPayStatus("paid");
        response.setMock(payResult.isMock());
        return response;
    }

    @Transactional
    public OrderResponse confirmOrderByFactory(Long orderId) {
        RentalOrderEntity order = getSupplierOwnedOrder(orderId);
        if (!"paid".equals(order.getPayStatus())) {
            throw new BizException(ResultCode.CONFLICT, "订单尚未支付，工厂不能确认");
        }
        if (!"pending_factory_confirm".equals(order.getOrderStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前订单状态不能执行工厂确认");
        }
        if (order.getConfirmDeadlineAt() != null && LocalDateTime.now().isAfter(order.getConfirmDeadlineAt())) {
            cancelTimedOutOrder(order, "工厂超时未确认，订单已自动取消");
            throw new BizException(ResultCode.CONFLICT, "订单已超时取消，不能再确认");
        }
        rentalOrderMapper.markFactoryConfirmed(orderId, LocalDateTime.now());
        List<RentalOrderItemEntity> items = rentalOrderItemMapper.selectByOrderId(orderId);
        for (RentalOrderItemEntity item : items) {
            propMapper.updateStatus(item.getPropId(), "renting");
        }
        return getOrder(orderId);
    }

    @Transactional
    public OrderResponse rejectOrderByFactory(Long orderId, String rejectReason) {
        RentalOrderEntity order = getSupplierOwnedOrder(orderId);
        if (!"paid".equals(order.getPayStatus())) {
            throw new BizException(ResultCode.CONFLICT, "订单尚未支付，工厂不能驳回");
        }
        if (!"pending_factory_confirm".equals(order.getOrderStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前订单状态不能执行工厂驳回");
        }
        String reason = rejectReason == null ? "" : rejectReason.trim();
        if (reason.isBlank()) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "驳回理由不能为空");
        }
        int updated = rentalOrderMapper.markFactoryRejected(orderId, LocalDateTime.now(), reason);
        if (updated <= 0) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变化，不能重复驳回");
        }
        List<RentalOrderItemEntity> items = rentalOrderItemMapper.selectByOrderId(orderId);
        for (RentalOrderItemEntity item : items) {
            propMapper.updateStatus(item.getPropId(), "idle");
        }
        return getOrder(orderId);
    }

    @Transactional
    public OrderResponse scanOutbound(Long orderId, Long propId) {
        RentalOrderEntity order = getSupplierOwnedOrder(orderId);
        if (!List.of("wait_pickup", "renting").contains(order.getOrderStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前订单状态不能扫码出库");
        }
        RentalOrderItemEntity item = requireOrderItem(orderId, propId);
        if ("scanned".equals(item.getOutboundStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具已经完成出库扫码");
        }
        rentalOrderItemMapper.markOutboundScanned(orderId, propId, LocalDateTime.now());
        int total = rentalOrderItemMapper.countByOrderId(orderId);
        int outboundScanned = rentalOrderItemMapper.countOutboundScannedByOrderId(orderId);
        if (total > 0 && total == outboundScanned) {
            rentalOrderMapper.markPickedUp(orderId, LocalDateTime.now());
        }
        return getOrder(orderId);
    }

    @Transactional
    public OrderResponse scanReturn(Long orderId, Long propId) {
        RentalOrderEntity order = getSupplierOwnedOrder(orderId);
        if (!List.of("renting", "wait_review").contains(order.getOrderStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前订单状态不能扫码归还");
        }
        RentalOrderItemEntity item = requireOrderItem(orderId, propId);
        if (!"scanned".equals(item.getOutboundStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具尚未完成出库，不能直接归还");
        }
        if ("scanned".equals(item.getReturnStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具已经完成归还扫码");
        }
        rentalOrderItemMapper.markReturnScanned(orderId, propId, LocalDateTime.now());
        propMapper.updateStatus(propId, "idle");
        int total = rentalOrderItemMapper.countByOrderId(orderId);
        int returnScanned = rentalOrderItemMapper.countReturnScannedByOrderId(orderId);
        if (total > 0 && total == returnScanned) {
            rentalOrderMapper.markReturned(orderId, LocalDateTime.now());
        }
        return getOrder(orderId);
    }

    @Transactional
    public OrderResponse reviewOrder(Long orderId, OrderReviewRequest request) {
        RentalOrderEntity order = getAccessibleOrder(orderId);
        if (!"wait_review".equals(order.getOrderStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前订单状态不能评价");
        }
        if (!List.of("demander", "supplier").contains(request.getReviewerRole())) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "reviewerRole 仅支持 demander 或 supplier");
        }
        String currentRole = requireCurrentRole();
        if (currentRole != null && !currentRole.equals(request.getReviewerRole())) {
            throw new BizException(ResultCode.FORBIDDEN, "当前登录角色与评价角色不一致");
        }
        OrderReviewEntity existed = orderReviewMapper.selectByOrderIdAndRole(orderId, request.getReviewerRole());
        if (existed != null) {
            throw new BizException(ResultCode.CONFLICT, "当前角色已提交过评价");
        }
        OrderReviewEntity review = new OrderReviewEntity();
        review.setOrderId(orderId);
        review.setReviewerUserId(currentUserSupport.requireCurrentUserId());
        review.setReviewerRole(request.getReviewerRole());
        int propScore = normalizeScore(request.getPropScore() == null ? request.getScore() : request.getPropScore());
        int counterpartyScore = normalizeScore(request.getCounterpartyScore() == null ? request.getScore() : request.getCounterpartyScore());
        review.setPropScore(propScore);
        review.setCounterpartyScore(counterpartyScore);
        review.setScore(Math.round((propScore + counterpartyScore) / 2.0f));
        review.setContent(request.getContent());
        review.setVisibleFlag(1);
        review.setAutoFlag(0);
        orderReviewMapper.insert(review);
        completeOrderIfBothReviewed(orderId);
        return getOrder(orderId);
    }

    @Transactional
    public int cancelTimeoutOrders() {
        List<RentalOrderEntity> expiredOrders = rentalOrderMapper.selectTimeoutPendingConfirmOrders(LocalDateTime.now());
        int count = 0;
        for (RentalOrderEntity order : expiredOrders) {
            count += cancelTimedOutOrder(order, "工厂超时未确认，系统自动取消订单");
        }
        return count;
    }

    @Transactional
    public int autoGoodReviewExpiredOrders() {
        List<RentalOrderEntity> expiredOrders = rentalOrderMapper.selectWaitReviewExpiredOrders(LocalDateTime.now().minusDays(3));
        int count = 0;
        for (RentalOrderEntity order : expiredOrders) {
            if (insertDefaultReviewIfMissing(order, "demander", order.getDemanderUserId()) > 0) {
                count++;
            }
            if (insertDefaultReviewIfMissing(order, "supplier", order.getSupplierUserId()) > 0) {
                count++;
            }
            completeOrderIfBothReviewed(order.getId());
        }
        return count;
    }

    private int cancelTimedOutOrder(RentalOrderEntity order, String cancelReason) {
        int updated = rentalOrderMapper.markTimeoutCancelled(order.getId(), LocalDateTime.now(), cancelReason);
        if (updated <= 0) {
            return 0;
        }
        for (RentalOrderItemEntity item : rentalOrderItemMapper.selectByOrderId(order.getId())) {
            propMapper.updateStatus(item.getPropId(), "idle");
        }
        return updated;
    }

    private int insertDefaultReviewIfMissing(RentalOrderEntity order, String reviewerRole, Long reviewerUserId) {
        if (orderReviewMapper.selectByOrderIdAndRole(order.getId(), reviewerRole) != null) {
            return 0;
        }
        OrderReviewEntity review = new OrderReviewEntity();
        review.setOrderId(order.getId());
        review.setReviewerUserId(reviewerUserId);
        review.setReviewerRole(reviewerRole);
        review.setScore(5);
        review.setPropScore(5);
        review.setCounterpartyScore(5);
        review.setContent("系统默认好评");
        review.setVisibleFlag(1);
        review.setAutoFlag(1);
        return orderReviewMapper.insert(review);
    }

    private void completeOrderIfBothReviewed(Long orderId) {
        if (orderReviewMapper.countByOrderId(orderId) >= 2) {
            rentalOrderMapper.markReviewed(orderId, LocalDateTime.now());
        }
    }

    private RentalOrderEntity getOrderEntity(Long orderId) {
        RentalOrderEntity entity = rentalOrderMapper.selectById(orderId);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND, "订单不存在");
        }
        return entity;
    }

    private RentalOrderEntity getAccessibleOrder(Long orderId) {
        RentalOrderEntity entity = getOrderEntity(orderId);
        String currentRole = requireCurrentRole();
        Long currentUserId = currentUserSupport.requireCurrentUserId();
        if ("demander".equals(currentRole) && currentUserId.equals(entity.getDemanderUserId())) {
            return entity;
        }
        if ("supplier".equals(currentRole) && currentUserId.equals(entity.getSupplierUserId())) {
            return entity;
        }
        throw new BizException(ResultCode.FORBIDDEN, "无权访问该订单");
    }

    private RentalOrderEntity getSupplierOwnedOrder(Long orderId) {
        RentalOrderEntity entity = getOrderEntity(orderId);
        String currentRole = requireCurrentRole();
        Long currentUserId = currentUserSupport.requireCurrentUserId();
        if (!"supplier".equals(currentRole) || !currentUserId.equals(entity.getSupplierUserId())) {
            throw new BizException(ResultCode.FORBIDDEN, "当前工厂无权操作该订单");
        }
        return entity;
    }

    private RentalOrderItemEntity requireOrderItem(Long orderId, Long propId) {
        return rentalOrderItemMapper.selectByOrderId(orderId).stream()
            .filter(item -> item.getPropId().equals(propId))
            .findFirst()
            .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单中不存在该道具"));
    }

    private OrderResponse toResponse(RentalOrderEntity order) {
        List<RentalOrderItemEntity> itemEntities = rentalOrderItemMapper.selectByOrderId(order.getId());
        List<Long> propIds = itemEntities.stream().map(RentalOrderItemEntity::getPropId).toList();
        List<PropEntity> currentProps = propIds.isEmpty() ? Collections.emptyList() : propMapper.selectByIds(propIds);
        List<OrderItemResponse> props = new ArrayList<>();

        for (RentalOrderItemEntity itemEntity : itemEntities) {
            PropEntity currentProp = currentProps.stream().filter(p -> p.getId().equals(itemEntity.getPropId())).findFirst().orElse(null);
            OrderItemResponse itemResponse = new OrderItemResponse();
            if (currentProp != null) {
                PropResponse base = propApplicationService.toResponse(currentProp);
                itemResponse.setId(base.getId());
                itemResponse.setName(base.getName());
                itemResponse.setImage(base.getImage());
                itemResponse.setImageUrl(base.getImageUrl());
                itemResponse.setStyle(base.getStyle());
                itemResponse.setType(base.getType());
                itemResponse.setSize(base.getSize());
                itemResponse.setLengthCm(base.getLengthCm());
                itemResponse.setWidthCm(base.getWidthCm());
                itemResponse.setHeightCm(base.getHeightCm());
                itemResponse.setMaterial(base.getMaterial());
                itemResponse.setPrice(base.getPrice());
                itemResponse.setDeposit(base.getDeposit());
                itemResponse.setFireResistantOption(base.getFireResistantOption());
                itemResponse.setWeight(base.getWeight());
                itemResponse.setTransportSuggestion(base.getTransportSuggestion());
                itemResponse.setStatus(base.getStatus());
                itemResponse.setStatusText(base.getStatusText());
            } else {
                itemResponse.setId(itemEntity.getPropId());
                itemResponse.setName(itemEntity.getPropNameSnapshot());
                itemResponse.setImage(itemEntity.getImageUrlSnapshot());
                itemResponse.setImageUrl(itemEntity.getImageUrlSnapshot());
                itemResponse.setPrice(MoneyUtils.fenToYuan(itemEntity.getDailyRentPriceFenSnapshot()));
                itemResponse.setDeposit(MoneyUtils.fenToYuan(itemEntity.getDepositAmountFenSnapshot()));
            }
            itemResponse.setOutboundScanned("scanned".equals(itemEntity.getOutboundStatus()));
            itemResponse.setReturnScanned("scanned".equals(itemEntity.getReturnStatus()));
            props.add(itemResponse);
        }

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNo(order.getOrderNo());
        response.setProjectId(order.getProjectId());
        response.setStatus(order.getOrderStatus());
        response.setStatusText(StatusTextHelper.orderStatusText(order.getOrderStatus()));
        response.setPayStatus(order.getPayStatus());
        response.setRentalDays(order.getRentalDays() == null ? 1 : order.getRentalDays());
        response.setRentalStartDate(order.getRentalStartDate() == null ? null : order.getRentalStartDate().toString());
        response.setRentalEndDate(order.getRentalEndDate() == null ? null : order.getRentalEndDate().toString());
        response.setContactAddress(order.getContactAddress());
        response.setUseScene(order.getUseScene());
        response.setSpecialRemark(order.getSpecialRemark());
        response.setCancelReason(order.getCancelReason());
        UserEntity demander = userMapper.selectById(order.getDemanderUserId());
        if (demander != null) {
            response.setDemanderName(demander.getNickname());
            response.setDemanderPhone(demander.getPhone());
            response.setDemanderIdentity(isTrue(demander.getStudentVerified()) ? "学生" : "其他");
            response.setDemanderCreditScore(demander.getCreditScore() == null ? defaultCreditScore(demander) : demander.getCreditScore());
        }
        response.setPrice(MoneyUtils.fenToYuan(order.getRentAmountFen()));
        response.setDeposit(MoneyUtils.fenToYuan(order.getDepositAmountFen()));
        response.setDate(TimeUtils.formatDate(order.getCreatedAt()));
        response.setPropIds(propIds);
        response.setProps(props);
        response.setPropCount(itemEntities.size());
        response.setOutboundScannedCount((int) itemEntities.stream().filter(item -> "scanned".equals(item.getOutboundStatus())).count());
        response.setReturnScannedCount((int) itemEntities.stream().filter(item -> "scanned".equals(item.getReturnStatus())).count());
        response.setCanFactoryConfirm("pending_factory_confirm".equals(order.getOrderStatus()) && "paid".equals(order.getPayStatus()));
        response.setCanFactoryReject("pending_factory_confirm".equals(order.getOrderStatus()) && "paid".equals(order.getPayStatus()));
        response.setCanScanOutbound(List.of("wait_pickup", "renting").contains(order.getOrderStatus()));
        response.setCanScanReturn("renting".equals(order.getOrderStatus()));
        boolean demanderReviewed = orderReviewMapper.selectByOrderIdAndRole(order.getId(), "demander") != null;
        boolean supplierReviewed = orderReviewMapper.selectByOrderIdAndRole(order.getId(), "supplier") != null;
        response.setDemanderReviewed(demanderReviewed);
        response.setSupplierReviewed(supplierReviewed);
        String currentRole = currentUserSupport.getCurrentRole();
        response.setCanReview("wait_review".equals(order.getOrderStatus()) &&
            (("demander".equals(currentRole) && !demanderReviewed) || ("supplier".equals(currentRole) && !supplierReviewed)));
        response.setConfirmDeadlineAt(TimeUtils.toEpochMilli(order.getConfirmDeadlineAt()));
        response.setCreatedAt(TimeUtils.toEpochMilli(order.getCreatedAt()));
        response.setPaidAt(TimeUtils.toEpochMilli(order.getPaidAt()));
        response.setConfirmedAt(TimeUtils.toEpochMilli(order.getConfirmedAt()));
        response.setPickedUpAt(TimeUtils.toEpochMilli(order.getPickedUpAt()));
        response.setReturnedAt(TimeUtils.toEpochMilli(order.getReturnedAt()));
        response.setReviewedAt(TimeUtils.toEpochMilli(order.getReviewedAt()));
        return response;
    }

    private Long ensureSingleSupplier(List<PropEntity> props) {
        Long supplierUserId = props.isEmpty() ? null : props.get(0).getSupplierUserId();
        boolean crossSupplier = props.stream().anyMatch(item -> !supplierUserId.equals(item.getSupplierUserId()));
        if (crossSupplier) {
            throw new BizException(ResultCode.CONFLICT, "当前版本暂不支持跨工厂混合下单，请按工厂分别提交方案");
        }
        if (supplierUserId == null) {
            throw new BizException(ResultCode.CONFLICT, "方案中的道具未绑定所属工厂，暂时无法下单");
        }
        return supplierUserId;
    }

    private Long currentDemanderUserId() {
        if (!"demander".equals(requireCurrentRole())) {
            throw new BizException(ResultCode.FORBIDDEN, "当前角色无权创建租赁订单");
        }
        return currentUserSupport.requireCurrentUserId();
    }

    private String requireCurrentRole() {
        String currentRole = currentUserSupport.getCurrentRole();
        if (!"demander".equals(currentRole) && !"supplier".equals(currentRole)) {
            throw new BizException(ResultCode.FORBIDDEN, "当前角色无权访问订单接口");
        }
        return currentRole;
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    private int calculateRentalDays(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "请选择租赁起止日期");
        }
        if (endDate.isBefore(startDate)) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "租赁结束日期不能早于开始日期");
        }
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (days < 1) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "租赁天数不能小于 1 天");
        }
        if (days > 365) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "租赁天数不能超过 365 天");
        }
        return Math.toIntExact(days);
    }

    private int normalizeRentalDays(Integer rentalDays) {
        if (rentalDays == null || rentalDays < 1) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "租赁天数不能小于 1 天");
        }
        if (rentalDays > 365) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "租赁天数不能超过 365 天");
        }
        return rentalDays;
    }

    private int normalizeScore(Integer score) {
        if (score == null || score < 1) {
            return 5;
        }
        return Math.min(score, 5);
    }

    private String normalizeRequiredText(String value, String message) {
        String normalized = normalizeOptionalText(value);
        if (normalized.isBlank()) {
            throw new BizException(ResultCode.VALIDATION_ERROR, message);
        }
        return normalized;
    }

    private String normalizeOptionalText(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isTrue(Integer value) {
        return value != null && value == 1;
    }

    private int defaultCreditScore(UserEntity user) {
        if (isTrue(user.getStudentVerified())) {
            return 98;
        }
        if (isTrue(user.getRealnameVerified())) {
            return 90;
        }
        return 80;
    }

    private String blankToDefault(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
