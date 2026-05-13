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
import com.muhou.backend.infrastructure.persistence.entity.PaymentOrderLinkEntity;
import com.muhou.backend.infrastructure.persistence.entity.OrderReviewEntity;
import com.muhou.backend.infrastructure.persistence.entity.ProjectSchemeEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropEntity;
import com.muhou.backend.infrastructure.persistence.entity.RentalOrderEntity;
import com.muhou.backend.infrastructure.persistence.entity.RentalOrderItemEntity;
import com.muhou.backend.infrastructure.persistence.entity.UserEntity;
import com.muhou.backend.infrastructure.persistence.entity.DisputeEntity;
import com.muhou.backend.infrastructure.persistence.mapper.DisputeMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderPaymentMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PaymentOrderLinkMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderReviewMapper;
import com.muhou.backend.infrastructure.persistence.mapper.ProjectSchemeItemMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropQrCodeMapper;
import com.muhou.backend.infrastructure.persistence.mapper.RentalOrderItemMapper;
import com.muhou.backend.infrastructure.persistence.mapper.RentalOrderMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserMapper;
import com.muhou.backend.web.request.CreateOrderRequest;
import com.muhou.backend.web.request.OrderDisputeRequest;
import com.muhou.backend.web.request.OrderPayRequest;
import com.muhou.backend.web.request.OrderReviewRequest;
import com.muhou.backend.web.response.OrderItemResponse;
import com.muhou.backend.web.response.OrderResponse;
import com.muhou.backend.web.response.PayResponse;
import com.muhou.backend.web.response.PropResponse;
import com.muhou.backend.web.response.DisputeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderApplicationService {

    private final RentalOrderMapper rentalOrderMapper;
    private final RentalOrderItemMapper rentalOrderItemMapper;
    private final OrderPaymentMapper orderPaymentMapper;
    private final PaymentOrderLinkMapper paymentOrderLinkMapper;
    private final OrderReviewMapper orderReviewMapper;
    private final OrderScanLogApplicationService orderScanLogApplicationService;
    private final DisputeMapper disputeMapper;
    private final ProjectSchemeItemMapper projectSchemeItemMapper;
    private final PropMapper propMapper;
    private final PropQrCodeMapper propQrCodeMapper;
    private final UserMapper userMapper;
    private final ProjectApplicationService projectApplicationService;
    private final PropApplicationService propApplicationService;
    private final WechatPaymentGateway wechatPaymentGateway;
    private final OrderSettlementApplicationService orderSettlementApplicationService;
    private final MuhouAppProperties properties;
    private final CurrentUserSupport currentUserSupport;

    public OrderApplicationService(RentalOrderMapper rentalOrderMapper,
                                   RentalOrderItemMapper rentalOrderItemMapper,
                                   OrderPaymentMapper orderPaymentMapper,
                                   PaymentOrderLinkMapper paymentOrderLinkMapper,
                                   OrderReviewMapper orderReviewMapper,
                                   OrderScanLogApplicationService orderScanLogApplicationService,
                                   DisputeMapper disputeMapper,
                                   ProjectSchemeItemMapper projectSchemeItemMapper,
                                   PropMapper propMapper,
                                   PropQrCodeMapper propQrCodeMapper,
                                   UserMapper userMapper,
                                   ProjectApplicationService projectApplicationService,
                                   PropApplicationService propApplicationService,
                                   WechatPaymentGateway wechatPaymentGateway,
                                   OrderSettlementApplicationService orderSettlementApplicationService,
                                   MuhouAppProperties properties,
                                   CurrentUserSupport currentUserSupport) {
        this.rentalOrderMapper = rentalOrderMapper;
        this.rentalOrderItemMapper = rentalOrderItemMapper;
        this.orderPaymentMapper = orderPaymentMapper;
        this.paymentOrderLinkMapper = paymentOrderLinkMapper;
        this.orderReviewMapper = orderReviewMapper;
        this.orderScanLogApplicationService = orderScanLogApplicationService;
        this.disputeMapper = disputeMapper;
        this.projectSchemeItemMapper = projectSchemeItemMapper;
        this.propMapper = propMapper;
        this.propQrCodeMapper = propQrCodeMapper;
        this.userMapper = userMapper;
        this.projectApplicationService = projectApplicationService;
        this.propApplicationService = propApplicationService;
        this.wechatPaymentGateway = wechatPaymentGateway;
        this.orderSettlementApplicationService = orderSettlementApplicationService;
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
    public OrderResponse applyDispute(Long orderId, OrderDisputeRequest request) {
        RentalOrderEntity order = getAccessibleOrder(orderId);
        String currentRole = requireCurrentRole();
        Long currentUserId = currentUserSupport.requireCurrentUserId();
        if (!List.of("wait_pickup", "renting", "wait_review").contains(order.getOrderStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前订单节点不能发起仲裁");
        }
        if (disputeMapper.countByOrderAndApplicant(orderId, currentUserId, currentRole) > 0) {
            throw new BizException(ResultCode.CONFLICT, "当前订单当前角色已经申请过仲裁，不能重复申请");
        }
        int claimAmountFen = yuanToFen(request.getClaimAmount());
        int maxClaimFen = "supplier".equals(currentRole)
            ? defaultZero(order.getDepositAmountFen())
            : defaultZero(order.getRentAmountFen());
        if (claimAmountFen > maxClaimFen) {
            throw new BizException(ResultCode.VALIDATION_ERROR,
                "supplier".equals(currentRole) ? "工厂向用户索赔金额不能超过押金" : "用户向工厂索赔金额不能超过实际费用");
        }

        DisputeEntity entity = new DisputeEntity();
        entity.setOrderId(orderId);
        entity.setApplyUserId(currentUserId);
        entity.setApplicantRole(currentRole);
        entity.setApplyStage(order.getOrderStatus());
        entity.setTitle("订单仲裁申请-" + order.getOrderNo());
        entity.setContent(normalizeRequiredText(request.getReason(), "仲裁原因不能为空"));
        entity.setClaimAmountFen(claimAmountFen);
        entity.setDepositAmountFenSnapshot(defaultZero(order.getDepositAmountFen()));
        entity.setOrderStatusSnapshot(order.getOrderStatus());
        entity.setDemanderUserId(order.getDemanderUserId());
        entity.setSupplierUserId(order.getSupplierUserId());
        entity.setEvidenceUrls(normalizeOptionalText(request.getEvidenceUrls()));
        entity.setDisputeStatus("pending");
        disputeMapper.insert(entity);
        return getOrder(orderId);
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
    public PayResponse checkoutProjectOrders(CreateOrderRequest request) {
        Long demanderUserId = currentDemanderUserId();
        ProjectSchemeEntity project = projectApplicationService.getOwnedProject(request.getProjectId());
        if (!"editing".equals(project.getProjectStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该方案已下单，不能重复提交");
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

        LocalDate rentalStartDate = request.getRentalStartDate();
        LocalDate rentalEndDate = request.getRentalEndDate();
        int rentalDays = calculateRentalDays(rentalStartDate, rentalEndDate);
        Map<Long, List<PropEntity>> propsBySupplier = new LinkedHashMap<>();
        for (PropEntity prop : props) {
            if (prop.getSupplierUserId() == null) {
                throw new BizException(ResultCode.CONFLICT, "方案中的道具未绑定所属工厂，暂时无法下单");
            }
            propsBySupplier.computeIfAbsent(prop.getSupplierUserId(), key -> new ArrayList<>()).add(prop);
        }

        List<RentalOrderEntity> orders = new ArrayList<>();
        int totalAmountFen = 0;
        int index = 1;
        String batchNo = "BATCH-" + System.currentTimeMillis();
        for (Map.Entry<Long, List<PropEntity>> entry : propsBySupplier.entrySet()) {
            RentalOrderEntity order = buildOrderEntity(
                project,
                demanderUserId,
                entry.getKey(),
                entry.getValue(),
                rentalStartDate,
                rentalEndDate,
                rentalDays,
                request,
                batchNo,
                index++
            );
            rentalOrderMapper.insert(order);
            rentalOrderItemMapper.batchInsert(buildOrderItems(order.getId(), entry.getValue()));
            orders.add(order);
            totalAmountFen += defaultZero(order.getTotalAmountFen());
        }

        OrderPaymentEntity payment = buildPayment(null, "batch_order_payment", totalAmountFen, "MUHOU 方案批量订单支付", "projectId=" + project.getId() + ";batchNo=" + batchNo, new OrderPayRequest());
        payment.setDescription("MUHOU 道具租赁订单支付");
        orderPaymentMapper.insert(payment);

        List<PaymentOrderLinkEntity> links = orders.stream().map(order -> {
            PaymentOrderLinkEntity link = new PaymentOrderLinkEntity();
            link.setPaymentId(payment.getId());
            link.setOrderId(order.getId());
            link.setOrderNo(order.getOrderNo());
            link.setSupplierUserId(order.getSupplierUserId());
            link.setRentAmountFen(order.getRentAmountFen());
            link.setDepositAmountFen(order.getDepositAmountFen());
            link.setTotalAmountFen(order.getTotalAmountFen());
            link.setRefundAmountFen(0);
            link.setSettlementAmountFen(0);
            return link;
        }).toList();
        paymentOrderLinkMapper.batchInsert(links);

        PaymentCreateResult payResult = wechatPaymentGateway.createPayment(payment);
        LocalDateTime now = LocalDateTime.now();
        orderPaymentMapper.markSuccess(payment.getId(), "success", payResult.getTransactionId(), payResult.getPrepayId(), now, payResult.getRawResponse());
        for (RentalOrderEntity order : orders) {
            rentalOrderMapper.markPaid(order.getId(), payment.getId(), "paid", order.getTotalAmountFen(), now);
            List<RentalOrderItemEntity> items = rentalOrderItemMapper.selectByOrderId(order.getId());
            for (RentalOrderItemEntity item : items) {
                propMapper.updateStatus(item.getPropId(), "locked");
            }
        }
        projectApplicationService.markProjectOrdered(project.getId());

        PayResponse response = buildPayResponse(payment, payResult, orders.get(0).getId());
        response.setOrderIds(orders.stream().map(RentalOrderEntity::getId).toList());
        response.setOrders(orders.stream().map(order -> toResponse(getOrderEntity(order.getId()))).toList());
        response.setSplitOrderCount(orders.size());
        response.setProjectName(project.getProjectName());
        return response;
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

        OrderPaymentEntity payment = buildPayment(
            orderId,
            "rent_deposit",
            order.getTotalAmountFen(),
            "MUHOU 道具租赁订单支付",
            "orderId=" + orderId,
            request
        );
        payment.setDescription("MUHOU 道具租赁订单支付");
        orderPaymentMapper.insert(payment);
        paymentOrderLinkMapper.batchInsert(List.of(buildPaymentOrderLink(payment.getId(), order)));

        PaymentCreateResult payResult = wechatPaymentGateway.createPayment(payment);
        LocalDateTime now = LocalDateTime.now();
        orderPaymentMapper.markSuccess(payment.getId(), "success", payResult.getTransactionId(), payResult.getPrepayId(), now, payResult.getRawResponse());
        rentalOrderMapper.markPaid(orderId, payment.getId(), "paid", order.getTotalAmountFen(), now);

        List<RentalOrderItemEntity> items = rentalOrderItemMapper.selectByOrderId(orderId);
        for (RentalOrderItemEntity item : items) {
            propMapper.updateStatus(item.getPropId(), "locked");
        }
        projectApplicationService.markProjectOrdered(order.getProjectId());

        PayResponse response = buildPayResponse(payment, payResult, orderId);
        response.setOrderIds(List.of(orderId));
        response.setOrders(List.of(toResponse(getOrderEntity(orderId))));
        response.setSplitOrderCount(1);
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
        if (updated > 0) {
            paymentOrderLinkMapper.addRefundAmount(order.getId(), defaultZero(order.getTotalAmountFen()));
            rentalOrderMapper.updateSettlementSummary(order.getId(), "refunded", defaultZero(order.getTotalAmountFen()));
        }
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
        try {
            return doScanOutbound(orderId, propId, null, "propId=" + propId);
        } catch (RuntimeException ex) {
            logScanFailure(orderId, propId, null, "outbound", "propId=" + propId, ex);
            throw ex;
        }
    }

    private OrderResponse doScanOutbound(Long orderId, Long propId, String qrCodeId, String rawScanResult) {
        RentalOrderEntity order = getSupplierOwnedOrder(orderId);
        if (!"wait_pickup".equals(order.getOrderStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前订单状态不能扫码出库");
        }
        int total = rentalOrderItemMapper.countByOrderId(orderId);
        int outboundScanned = rentalOrderItemMapper.countOutboundScannedByOrderId(orderId);
        if (total > 0 && outboundScanned >= total) {
            throw new BizException(ResultCode.CONFLICT, "该订单已完成全部出库扫码，不能重复出库");
        }
        RentalOrderItemEntity item = requireOrderItem(orderId, propId);
        PropEntity prop = requireOrderScanProp(order, propId);
        if (!"renting".equals(prop.getPropStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具当前不是待出库状态，不能出库");
        }
        if ("scanned".equals(item.getOutboundStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具已经完成出库扫码");
        }
        rentalOrderItemMapper.markOutboundScanned(orderId, propId, LocalDateTime.now());
        orderScanLogApplicationService.logSuccess(order.getId(), item.getId(), prop.getId(), firstNonBlank(qrCodeId, prop.getQrCodeId()), "outbound", order.getSupplierUserId(), rawScanResult);
        outboundScanned = rentalOrderItemMapper.countOutboundScannedByOrderId(orderId);
        if (total > 0 && total == outboundScanned) {
            rentalOrderMapper.markPickedUp(orderId, LocalDateTime.now());
        }
        return getOrder(orderId);
    }

    @Transactional
    public OrderResponse scanReturn(Long orderId, Long propId) {
        try {
            return doScanReturn(orderId, propId, null, "propId=" + propId);
        } catch (RuntimeException ex) {
            logScanFailure(orderId, propId, null, "return", "propId=" + propId, ex);
            throw ex;
        }
    }

    private OrderResponse doScanReturn(Long orderId, Long propId, String qrCodeId, String rawScanResult) {
        RentalOrderEntity order = getSupplierOwnedOrder(orderId);
        if (!"renting".equals(order.getOrderStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前订单状态不能扫码归还");
        }
        int total = rentalOrderItemMapper.countByOrderId(orderId);
        int outboundScanned = rentalOrderItemMapper.countOutboundScannedByOrderId(orderId);
        if (total <= 0 || outboundScanned < total) {
            throw new BizException(ResultCode.CONFLICT, "订单道具尚未全部出库，不能开始归还扫码");
        }
        int returnScanned = rentalOrderItemMapper.countReturnScannedByOrderId(orderId);
        if (returnScanned >= total) {
            throw new BizException(ResultCode.CONFLICT, "该订单已完成全部归还扫码，不能重复归还");
        }
        RentalOrderItemEntity item = requireOrderItem(orderId, propId);
        PropEntity prop = requireOrderScanProp(order, propId);
        if (!"renting".equals(prop.getPropStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具当前不是借出状态，不能归还");
        }
        if (!"scanned".equals(item.getOutboundStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具尚未完成出库，不能直接归还");
        }
        if ("scanned".equals(item.getReturnStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具已经完成归还扫码");
        }
        rentalOrderItemMapper.markReturnScanned(orderId, propId, LocalDateTime.now());
        orderScanLogApplicationService.logSuccess(order.getId(), item.getId(), prop.getId(), firstNonBlank(qrCodeId, prop.getQrCodeId()), "return", order.getSupplierUserId(), rawScanResult);
        propMapper.updateStatus(propId, "idle");
        returnScanned = rentalOrderItemMapper.countReturnScannedByOrderId(orderId);
        if (total > 0 && total == returnScanned) {
            rentalOrderMapper.markReturned(orderId, LocalDateTime.now());
        }
        return getOrder(orderId);
    }

    @Transactional
    public OrderResponse scanOutboundByQrCode(Long orderId, String qrCodeId) {
        Long propId = null;
        String normalizedQrCodeId = normalizeQrCodeId(qrCodeId);
        try {
            propId = resolvePropIdByQrCode(normalizedQrCodeId);
            return doScanOutbound(orderId, propId, normalizedQrCodeId, "qrCodeId=" + normalizedQrCodeId);
        } catch (RuntimeException ex) {
            logScanFailure(orderId, propId, normalizedQrCodeId, "outbound", "qrCodeId=" + normalizedQrCodeId, ex);
            throw ex;
        }
    }

    @Transactional
    public OrderResponse scanReturnByQrCode(Long orderId, String qrCodeId) {
        Long propId = null;
        String normalizedQrCodeId = normalizeQrCodeId(qrCodeId);
        try {
            propId = resolvePropIdByQrCode(normalizedQrCodeId);
            return doScanReturn(orderId, propId, normalizedQrCodeId, "qrCodeId=" + normalizedQrCodeId);
        } catch (RuntimeException ex) {
            logScanFailure(orderId, propId, normalizedQrCodeId, "return", "qrCodeId=" + normalizedQrCodeId, ex);
            throw ex;
        }
    }

    @Transactional
    public OrderResponse reviewOrder(Long orderId, OrderReviewRequest request) {
        RentalOrderEntity order = getAccessibleOrder(orderId);
        if (!"wait_review".equals(order.getOrderStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前订单状态不能评价");
        }
        if (disputeMapper.countPendingByOrderId(orderId) > 0) {
            throw new BizException(ResultCode.CONFLICT, "当前订单存在待处理仲裁，裁定前不能评价完成订单");
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
            if (disputeMapper.countPendingByOrderId(orderId) > 0) {
                return;
            }
            rentalOrderMapper.markReviewed(orderId, LocalDateTime.now());
            orderSettlementApplicationService.settleNormalOrder(getOrderEntity(orderId));
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
        if (propId == null) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "扫码结果未识别到道具");
        }
        RentalOrderItemEntity item = rentalOrderItemMapper.selectByOrderIdAndPropId(orderId, propId);
        if (item == null) {
            throw new BizException(ResultCode.NOT_FOUND, "扫描的道具不在当前订单中，不能操作");
        }
        return item;
    }

    private String normalizeQrCodeId(String qrCodeId) {
        if (qrCodeId == null || qrCodeId.isBlank()) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "扫码结果未识别到二维码标识");
        }
        return qrCodeId.trim();
    }

    private Long resolvePropIdByQrCode(String qrCodeId) {
        if (qrCodeId == null || qrCodeId.isBlank()) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "扫码结果未识别到二维码标识");
        }
        var qrCode = propQrCodeMapper.selectByQrCodeId(qrCodeId);
        if (qrCode == null || "revoked".equals(qrCode.getStatus())) {
            throw new BizException(ResultCode.NOT_FOUND, "二维码不存在或已作废");
        }
        return qrCode.getPropId();
    }

    private PropEntity requireOrderScanProp(RentalOrderEntity order, Long propId) {
        PropEntity prop = propMapper.selectById(propId);
        if (prop == null) {
            throw new BizException(ResultCode.NOT_FOUND, "道具不存在");
        }
        if (prop.getSupplierUserId() == null || !prop.getSupplierUserId().equals(order.getSupplierUserId())) {
            throw new BizException(ResultCode.FORBIDDEN, "扫描的道具不属于当前工厂，不能操作");
        }
        if (!"filled".equals(prop.getFillStatus()) || !"approved".equals(prop.getAuditStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具尚未通过审核，不能操作订单扫码");
        }
        if ("offline".equals(prop.getPropStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具已下架，不能操作订单扫码");
        }
        if ("locked".equals(prop.getPropStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具仍处于锁定状态，不能操作订单扫码");
        }
        return prop;
    }

    private void logScanFailure(Long orderId, Long propId, String qrCodeId, String scanType, String rawScanResult, RuntimeException ex) {
        Long supplierUserId = null;
        try {
            RentalOrderEntity order = rentalOrderMapper.selectById(orderId);
            supplierUserId = order == null ? null : order.getSupplierUserId();
        } catch (RuntimeException ignored) {
            // Failure logging must not hide the original scan error.
        }
        try {
            orderScanLogApplicationService.logFailure(orderId, propId, qrCodeId, scanType, supplierUserId, rawScanResult, ex.getMessage());
        } catch (RuntimeException ignored) {
            // Audit logging is best-effort on infrastructure failures; business error remains primary.
        }
    }

    private String firstNonBlank(String first, String second) {
        return first != null && !first.isBlank() ? first : second;
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
        response.setSupplierUserId(order.getSupplierUserId());
        UserEntity supplier = userMapper.selectById(order.getSupplierUserId());
        response.setSupplierName(supplier == null ? "工厂 " + order.getSupplierUserId() : supplier.getNickname());
        response.setPaymentId(order.getCurrentPaymentId());
        if (order.getCurrentPaymentId() != null) {
            OrderPaymentEntity payment = orderPaymentMapper.selectById(order.getCurrentPaymentId());
            if (payment != null) {
                response.setPaymentNo(payment.getPaymentNo());
            }
        }
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
        int propCount = itemEntities.size();
        int outboundScannedCount = (int) itemEntities.stream().filter(item -> "scanned".equals(item.getOutboundStatus())).count();
        int returnScannedCount = (int) itemEntities.stream().filter(item -> "scanned".equals(item.getReturnStatus())).count();
        response.setPropCount(propCount);
        response.setOutboundScannedCount(outboundScannedCount);
        response.setReturnScannedCount(returnScannedCount);
        response.setCanFactoryConfirm("pending_factory_confirm".equals(order.getOrderStatus()) && "paid".equals(order.getPayStatus()));
        response.setCanFactoryReject("pending_factory_confirm".equals(order.getOrderStatus()) && "paid".equals(order.getPayStatus()));
        response.setCanScanOutbound("wait_pickup".equals(order.getOrderStatus()) && propCount > 0 && outboundScannedCount < propCount);
        response.setCanScanReturn("renting".equals(order.getOrderStatus()) && propCount > 0 && outboundScannedCount >= propCount && returnScannedCount < propCount);
        boolean demanderReviewed = orderReviewMapper.selectByOrderIdAndRole(order.getId(), "demander") != null;
        boolean supplierReviewed = orderReviewMapper.selectByOrderIdAndRole(order.getId(), "supplier") != null;
        response.setDemanderReviewed(demanderReviewed);
        response.setSupplierReviewed(supplierReviewed);
        String currentRole = currentUserSupport.getCurrentRole();
        response.setCanReview("wait_review".equals(order.getOrderStatus()) &&
            (("demander".equals(currentRole) && !demanderReviewed) || ("supplier".equals(currentRole) && !supplierReviewed)));
        response.setCanApplyDispute(canApplyDispute(order, currentRole));
        response.setDisputeCount(disputeMapper.countByOrderId(order.getId()));
        response.setDisputes(disputeMapper.selectByOrderId(order.getId()).stream().map(this::toDisputeResponse).toList());
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

    private RentalOrderEntity buildOrderEntity(ProjectSchemeEntity project,
                                               Long demanderUserId,
                                               Long supplierUserId,
                                               List<PropEntity> props,
                                               LocalDate rentalStartDate,
                                               LocalDate rentalEndDate,
                                               int rentalDays,
                                               CreateOrderRequest request,
                                               String batchNo,
                                               int splitIndex) {
        int dailyRentAmountFen = props.stream().mapToInt(item -> defaultZero(item.getDailyRentPriceFen())).sum();
        long rentAmountFenLong = (long) dailyRentAmountFen * rentalDays;
        if (rentAmountFenLong > Integer.MAX_VALUE) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "租赁金额超出系统限制");
        }
        int rentAmountFen = (int) rentAmountFenLong;
        int depositAmountFen = props.stream().mapToInt(item -> defaultZero(item.getDepositAmountFen())).sum();

        RentalOrderEntity order = new RentalOrderEntity();
        order.setOrderNo("ORD-" + System.currentTimeMillis() + "-" + splitIndex + "-" + UUID.randomUUID().toString().substring(0, 6));
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
        order.setRemark("来自方案《" + project.getProjectName() + "》，批次 " + batchNo + "，按商家拆分生成");
        order.setCreatedBy(demanderUserId);
        return order;
    }

    private List<RentalOrderItemEntity> buildOrderItems(Long orderId, List<PropEntity> props) {
        return props.stream().map(prop -> {
            RentalOrderItemEntity item = new RentalOrderItemEntity();
            item.setOrderId(orderId);
            item.setPropId(prop.getId());
            item.setPropNameSnapshot(prop.getPropName());
            item.setImageUrlSnapshot(prop.getImageUrl());
            item.setDailyRentPriceFenSnapshot(prop.getDailyRentPriceFen());
            item.setDepositAmountFenSnapshot(prop.getDepositAmountFen());
            item.setOutboundStatus("pending");
            item.setReturnStatus("pending");
            return item;
        }).collect(Collectors.toList());
    }

    private OrderPaymentEntity buildPayment(Long orderId,
                                            String payScene,
                                            Integer amountFen,
                                            String description,
                                            String attachData,
                                            OrderPayRequest request) {
        OrderPayRequest safeRequest = request == null ? new OrderPayRequest() : request;
        OrderPaymentEntity payment = new OrderPaymentEntity();
        payment.setPaymentNo("PAY-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6));
        payment.setOrderId(orderId);
        payment.setPayScene(payScene);
        payment.setPaymentChannel("wechat_miniapp");
        payment.setPaymentStatus("created");
        payment.setMerchantMchid(properties.getPayment().getMchId());
        payment.setAppid(blankToDefault(properties.getPayment().getAppId(), "demo-miniapp"));
        payment.setOpenid(blankToDefault(safeRequest.getOpenId(), "mock-openid"));
        payment.setMerchantOutTradeNo("OUT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        payment.setDescription(description);
        payment.setAmountFen(amountFen);
        payment.setCurrency("CNY");
        payment.setClientIp(safeRequest.getClientIp());
        payment.setNotifyUrl(properties.getPayment().getNotifyUrl());
        payment.setAttachData(attachData);
        payment.setTimeExpire(LocalDateTime.now().plusMinutes(15));
        return payment;
    }

    private PayResponse buildPayResponse(OrderPaymentEntity payment, PaymentCreateResult payResult, Long primaryOrderId) {
        PayResponse response = new PayResponse();
        response.setOrderId(primaryOrderId);
        response.setPaymentId(payment.getId());
        response.setPaymentNo(payment.getPaymentNo());
        response.setMerchantOutTradeNo(payment.getMerchantOutTradeNo());
        response.setPrepayId(payResult.getPrepayId());
        response.setTransactionId(payResult.getTransactionId());
        response.setPayStatus("paid");
        response.setMock(payResult.isMock());
        return response;
    }

    private PaymentOrderLinkEntity buildPaymentOrderLink(Long paymentId, RentalOrderEntity order) {
        PaymentOrderLinkEntity link = new PaymentOrderLinkEntity();
        link.setPaymentId(paymentId);
        link.setOrderId(order.getId());
        link.setOrderNo(order.getOrderNo());
        link.setSupplierUserId(order.getSupplierUserId());
        link.setRentAmountFen(defaultZero(order.getRentAmountFen()));
        link.setDepositAmountFen(defaultZero(order.getDepositAmountFen()));
        link.setTotalAmountFen(defaultZero(order.getTotalAmountFen()));
        link.setRefundAmountFen(defaultZero(order.getTotalRefundedFen()));
        link.setSettlementAmountFen(0);
        return link;
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

    private boolean canApplyDispute(RentalOrderEntity order, String currentRole) {
        if (!List.of("demander", "supplier").contains(currentRole)) {
            return false;
        }
        if (!List.of("wait_pickup", "renting", "wait_review").contains(order.getOrderStatus())) {
            return false;
        }
        Long currentUserId = currentUserSupport.requireCurrentUserId();
        return disputeMapper.countByOrderAndApplicant(order.getId(), currentUserId, currentRole) <= 0;
    }

    private DisputeResponse toDisputeResponse(DisputeEntity entity) {
        DisputeResponse response = new DisputeResponse();
        response.setId(entity.getId());
        response.setOrderId(entity.getOrderId());
        response.setOrderNo(entity.getOrderNo());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setApplicant(entity.getApplicantName());
        response.setApplicantRole(entity.getApplicantRole());
        response.setApplicantRoleText("supplier".equals(entity.getApplicantRole()) ? "工厂方" : "租赁方");
        response.setApplyStage(entity.getApplyStage());
        response.setApplyStageText(disputeStageText(entity.getApplyStage()));
        response.setClaimAmount(MoneyUtils.fenToYuan(entity.getClaimAmountFen()));
        response.setDepositAmount(MoneyUtils.fenToYuan(entity.getDepositAmountFenSnapshot()));
        response.setEvidenceUrls(entity.getEvidenceUrls());
        response.setEvidenceImages(parseEvidenceImages(entity.getEvidenceUrls()));
        response.setStatus(entity.getDisputeStatus());
        response.setStatusText(disputeStatusText(entity.getDisputeStatus()));
        response.setResolution(entity.getResolution());
        response.setResolutionType(entity.getResolutionType());
        response.setResolutionTypeText(resolutionTypeText(entity.getResolutionType()));
        response.setRefundStatus(entity.getRefundStatus());
        response.setCreatedAt(TimeUtils.format(entity.getCreatedAt()));
        response.setResolvedAt(TimeUtils.format(entity.getResolvedAt()));
        return response;
    }

    private List<String> parseEvidenceImages(String evidenceUrls) {
        if (evidenceUrls == null || evidenceUrls.isBlank()) {
            return List.of();
        }
        return java.util.Arrays.stream(evidenceUrls.split("[,;\\n]"))
            .map(String::trim)
            .filter(item -> !item.isBlank())
            .distinct()
            .toList();
    }

    private String disputeStatusText(String status) {
        if ("resolved".equals(status)) return "已同意";
        if ("closed".equals(status)) return "已拒绝";
        return "待仲裁";
    }

    private String disputeStageText(String stage) {
        if ("wait_pickup".equals(stage)) return "待取货";
        if ("renting".equals(stage)) return "租赁中";
        if ("wait_review".equals(stage)) return "待评价";
        return "未知节点";
    }

    private String resolutionTypeText(String type) {
        if ("approved".equals(type)) return "同意仲裁";
        if ("rejected".equals(type)) return "拒绝仲裁";
        return "";
    }

    private int yuanToFen(BigDecimal yuan) {
        if (yuan == null) {
            return 0;
        }
        if (yuan.compareTo(BigDecimal.ZERO) < 0) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "金额不能小于 0");
        }
        BigDecimal fen = yuan.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP);
        if (fen.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "金额超出系统限制");
        }
        return fen.intValueExact();
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
