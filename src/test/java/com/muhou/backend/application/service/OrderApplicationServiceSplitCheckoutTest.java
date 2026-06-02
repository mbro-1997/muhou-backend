package com.muhou.backend.application.service;

import com.muhou.backend.common.config.properties.MuhouAppProperties;
import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.infrastructure.client.PaymentCreateResult;
import com.muhou.backend.infrastructure.client.WechatPaymentGateway;
import com.muhou.backend.infrastructure.persistence.entity.OrderItemInstanceEntity;
import com.muhou.backend.infrastructure.persistence.entity.OrderPaymentEntity;
import com.muhou.backend.infrastructure.persistence.entity.PaymentOrderLinkEntity;
import com.muhou.backend.infrastructure.persistence.entity.ProjectSchemeEntity;
import com.muhou.backend.infrastructure.persistence.entity.ProjectSchemeItemEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropEntity;
import com.muhou.backend.infrastructure.persistence.entity.RentalOrderEntity;
import com.muhou.backend.infrastructure.persistence.entity.RentalOrderItemEntity;
import com.muhou.backend.infrastructure.persistence.entity.UserEntity;
import com.muhou.backend.infrastructure.persistence.mapper.DisputeMapper;
import com.muhou.backend.infrastructure.persistence.mapper.DisputeReasonConfigMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderAdminActionMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderItemInstanceMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderPaymentMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderReviewMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PaymentOrderLinkMapper;
import com.muhou.backend.infrastructure.persistence.mapper.ProjectSchemeItemMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropInstanceMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropQrCodeMapper;
import com.muhou.backend.infrastructure.persistence.mapper.RentalOrderItemMapper;
import com.muhou.backend.infrastructure.persistence.mapper.RentalOrderMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserMapper;
import com.muhou.backend.web.request.CreateOrderRequest;
import com.muhou.backend.web.response.PayResponse;
import com.muhou.backend.web.response.PropResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderApplicationServiceSplitCheckoutTest {

    @Mock private RentalOrderMapper rentalOrderMapper;
    @Mock private RentalOrderItemMapper rentalOrderItemMapper;
    @Mock private OrderItemInstanceMapper orderItemInstanceMapper;
    @Mock private OrderPaymentMapper orderPaymentMapper;
    @Mock private PaymentOrderLinkMapper paymentOrderLinkMapper;
    @Mock private OrderReviewMapper orderReviewMapper;
    @Mock private OrderScanLogApplicationService orderScanLogApplicationService;
    @Mock private DisputeMapper disputeMapper;
    @Mock private DisputeReasonConfigMapper disputeReasonConfigMapper;
    @Mock private OrderAdminActionMapper orderAdminActionMapper;
    @Mock private ProjectSchemeItemMapper projectSchemeItemMapper;
    @Mock private PropInstanceMapper propInstanceMapper;
    @Mock private PropMapper propMapper;
    @Mock private PropQrCodeMapper propQrCodeMapper;
    @Mock private UserMapper userMapper;
    @Mock private ProjectApplicationService projectApplicationService;
    @Mock private PropApplicationService propApplicationService;
    @Mock private WechatPaymentGateway wechatPaymentGateway;
    @Mock private OrderSettlementApplicationService orderSettlementApplicationService;
    @Mock private CurrentUserSupport currentUserSupport;

    private OrderApplicationService service;
    private final Map<Long, RentalOrderEntity> ordersById = new HashMap<>();
    private final Map<Long, List<RentalOrderItemEntity>> itemsByOrderId = new HashMap<>();
    private final Map<Long, PropEntity> propsById = new HashMap<>();
    private final List<PaymentOrderLinkEntity> paymentLinks = new ArrayList<>();
    private final List<OrderItemInstanceEntity> boundInstances = new ArrayList<>();
    private OrderPaymentEntity insertedPayment;

    @BeforeEach
    void setUp() {
        service = new OrderApplicationService(
            rentalOrderMapper,
            rentalOrderItemMapper,
            orderItemInstanceMapper,
            orderPaymentMapper,
            paymentOrderLinkMapper,
            orderReviewMapper,
            orderScanLogApplicationService,
            disputeMapper,
            disputeReasonConfigMapper,
            orderAdminActionMapper,
            projectSchemeItemMapper,
            propInstanceMapper,
            propMapper,
            propQrCodeMapper,
            userMapper,
            projectApplicationService,
            propApplicationService,
            wechatPaymentGateway,
            orderSettlementApplicationService,
            new MuhouAppProperties(),
            currentUserSupport
        );
    }

    @Test
    void checkoutProjectOrdersSplitsBySupplierAndLinksOnePaymentToAllOrders() {
        ProjectSchemeEntity project = project(900L, 10L, "multi supplier project");
        PropEntity supplierOneProp = prop(101L, 201L, "supplier one prop", 1000, 5000);
        PropEntity supplierTwoProp = prop(202L, 202L, "supplier two prop", 2000, 8000);
        propsById.put(supplierOneProp.getId(), supplierOneProp);
        propsById.put(supplierTwoProp.getId(), supplierTwoProp);

        when(currentUserSupport.getCurrentRole()).thenReturn("demander");
        when(currentUserSupport.requireCurrentUserId()).thenReturn(10L);
        when(projectApplicationService.getOwnedProject(project.getId())).thenReturn(project);
        when(projectSchemeItemMapper.selectByProjectId(project.getId())).thenReturn(List.of(
            projectItem(project.getId(), supplierOneProp.getId(), 2),
            projectItem(project.getId(), supplierTwoProp.getId(), 1)
        ));
        when(propMapper.selectByIds(anyList())).thenAnswer(invocation -> {
            List<Long> ids = invocation.getArgument(0);
            return ids.stream().map(propsById::get).toList();
        });
        when(propInstanceMapper.countByPropIdAndStatus(supplierOneProp.getId(), "idle")).thenReturn(2);
        when(propInstanceMapper.countByPropIdAndStatus(supplierTwoProp.getId(), "idle")).thenReturn(1);
        when(wechatPaymentGateway.createPayment(any(OrderPaymentEntity.class)))
            .thenReturn(new PaymentCreateResult("prepay-test", "tx-test", "{\"mock\":true}", true));
        when(userMapper.selectById(anyLong())).thenAnswer(invocation -> user(invocation.getArgument(0)));
        when(disputeMapper.selectByOrderId(anyLong())).thenReturn(List.of());
        when(disputeMapper.countByOrderId(anyLong())).thenReturn(0);
        when(orderReviewMapper.selectByOrderIdAndRole(anyLong(), any())).thenReturn(null);
        when(orderItemInstanceMapper.countOutboundScannedByOrderId(anyLong())).thenReturn(0);
        when(orderItemInstanceMapper.countReturnScannedByOrderId(anyLong())).thenReturn(0);
        when(orderItemInstanceMapper.countOutboundByOrderItemId(anyLong())).thenReturn(0);
        when(orderItemInstanceMapper.countReturnByOrderItemId(anyLong())).thenReturn(0);
        when(propApplicationService.toResponse(any(PropEntity.class))).thenAnswer(invocation -> propResponse(invocation.getArgument(0)));

        AtomicLong orderIdSequence = new AtomicLong(1000L);
        doAnswer(invocation -> {
            RentalOrderEntity order = invocation.getArgument(0);
            order.setId(orderIdSequence.getAndIncrement());
            ordersById.put(order.getId(), order);
            return 1;
        }).when(rentalOrderMapper).insert(any(RentalOrderEntity.class));
        when(rentalOrderMapper.selectById(anyLong())).thenAnswer(invocation -> ordersById.get(invocation.getArgument(0)));
        doAnswer(invocation -> {
            OrderPaymentEntity payment = invocation.getArgument(0);
            payment.setId(500L);
            insertedPayment = payment;
            return 1;
        }).when(orderPaymentMapper).insert(any(OrderPaymentEntity.class));
        when(orderPaymentMapper.selectById(500L)).thenAnswer(invocation -> insertedPayment);
        doAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            List<PaymentOrderLinkEntity> links = invocation.getArgument(0);
            paymentLinks.addAll(links);
            return links.size();
        }).when(paymentOrderLinkMapper).batchInsert(anyList());
        AtomicLong itemIdSequence = new AtomicLong(7000L);
        doAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            List<RentalOrderItemEntity> items = invocation.getArgument(0);
            for (RentalOrderItemEntity item : items) {
                item.setId(itemIdSequence.getAndIncrement());
                itemsByOrderId.computeIfAbsent(item.getOrderId(), key -> new ArrayList<>()).add(item);
            }
            return items.size();
        }).when(rentalOrderItemMapper).batchInsert(anyList());
        when(rentalOrderItemMapper.selectByOrderId(anyLong())).thenAnswer(invocation -> itemsByOrderId.getOrDefault(invocation.getArgument(0), List.of()));
        when(propInstanceMapper.selectIdleIdsForUpdate(eq(supplierOneProp.getId()), eq(2))).thenReturn(List.of(10001L, 10002L));
        when(propInstanceMapper.selectIdleIdsForUpdate(eq(supplierTwoProp.getId()), eq(1))).thenReturn(List.of(20001L));
        when(propInstanceMapper.lockInstances(anyList(), anyLong())).thenAnswer(invocation -> ((List<?>) invocation.getArgument(0)).size());
        doAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            List<OrderItemInstanceEntity> items = invocation.getArgument(0);
            boundInstances.addAll(items);
            return items.size();
        }).when(orderItemInstanceMapper).batchInsert(anyList());
        doAnswer(invocation -> {
            Long orderId = invocation.getArgument(0);
            Long paymentId = invocation.getArgument(1);
            String payStatus = invocation.getArgument(2);
            Integer totalPaidFen = invocation.getArgument(3);
            RentalOrderEntity order = ordersById.get(orderId);
            order.setCurrentPaymentId(paymentId);
            order.setPayStatus(payStatus);
            order.setTotalPaidFen(totalPaidFen);
            return 1;
        }).when(rentalOrderMapper).markPaid(anyLong(), anyLong(), any(), any(), any());

        PayResponse response = service.checkoutProjectOrders(createRequest(project.getId()));

        assertThat(response.getSplitOrderCount()).isEqualTo(2);
        assertThat(response.getOrderIds()).containsExactlyElementsOf(ordersById.keySet());
        assertThat(ordersById.values()).hasSize(2);
        assertThat(ordersById.values()).extracting(RentalOrderEntity::getSupplierUserId).containsExactly(201L, 202L);
        assertThat(insertedPayment.getAmountFen()).isEqualTo(30000);
        assertThat(paymentLinks).hasSize(2);
        assertThat(paymentLinks).allSatisfy(link -> assertThat(link.getPaymentId()).isEqualTo(500L));
        assertThat(paymentLinks).extracting(PaymentOrderLinkEntity::getOrderId).containsExactlyElementsOf(ordersById.keySet());

        RentalOrderEntity supplierOneOrder = ordersById.values().stream()
            .filter(order -> order.getSupplierUserId().equals(201L))
            .findFirst()
            .orElseThrow();
        RentalOrderEntity supplierTwoOrder = ordersById.values().stream()
            .filter(order -> order.getSupplierUserId().equals(202L))
            .findFirst()
            .orElseThrow();
        assertThat(supplierOneOrder.getRentAmountFen()).isEqualTo(6000);
        assertThat(supplierOneOrder.getDepositAmountFen()).isEqualTo(10000);
        assertThat(supplierOneOrder.getTotalAmountFen()).isEqualTo(16000);
        assertThat(supplierTwoOrder.getRentAmountFen()).isEqualTo(6000);
        assertThat(supplierTwoOrder.getDepositAmountFen()).isEqualTo(8000);
        assertThat(supplierTwoOrder.getTotalAmountFen()).isEqualTo(14000);

        assertThat(itemsByOrderId.get(supplierOneOrder.getId())).singleElement().satisfies(item -> {
            assertThat(item.getPropId()).isEqualTo(supplierOneProp.getId());
            assertThat(item.getQuantity()).isEqualTo(2);
        });
        assertThat(itemsByOrderId.get(supplierTwoOrder.getId())).singleElement().satisfies(item -> {
            assertThat(item.getPropId()).isEqualTo(supplierTwoProp.getId());
            assertThat(item.getQuantity()).isEqualTo(1);
        });
        assertThat(boundInstances).hasSize(3);
        assertThat(boundInstances).filteredOn(instance -> instance.getOrderId().equals(supplierOneOrder.getId())).hasSize(2);
        assertThat(boundInstances).filteredOn(instance -> instance.getOrderId().equals(supplierTwoOrder.getId())).hasSize(1);

        verify(projectApplicationService).markProjectOrdered(project.getId());
    }

    private CreateOrderRequest createRequest(Long projectId) {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setProjectId(projectId);
        request.setRentalStartDate(LocalDate.of(2026, 6, 1));
        request.setRentalEndDate(LocalDate.of(2026, 6, 3));
        request.setContactAddress("Shanghai");
        request.setUseScene("show");
        request.setSpecialRemark("none");
        return request;
    }

    private ProjectSchemeEntity project(Long id, Long userId, String name) {
        ProjectSchemeEntity entity = new ProjectSchemeEntity();
        entity.setId(id);
        entity.setUserId(userId);
        entity.setProjectName(name);
        entity.setProjectStatus("editing");
        return entity;
    }

    private ProjectSchemeItemEntity projectItem(Long projectId, Long propId, Integer quantity) {
        ProjectSchemeItemEntity entity = new ProjectSchemeItemEntity();
        entity.setProjectId(projectId);
        entity.setPropId(propId);
        entity.setQuantity(quantity);
        return entity;
    }

    private PropEntity prop(Long id, Long supplierUserId, String name, Integer dailyRentFen, Integer depositFen) {
        PropEntity entity = new PropEntity();
        entity.setId(id);
        entity.setSupplierUserId(supplierUserId);
        entity.setPropName(name);
        entity.setImageUrl("/image/" + id + ".png");
        entity.setDailyRentPriceFen(dailyRentFen);
        entity.setDepositAmountFen(depositFen);
        entity.setFillStatus("filled");
        entity.setAuditStatus("approved");
        entity.setPropStatus("idle");
        return entity;
    }

    private UserEntity user(Long id) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setNickname("user-" + id);
        entity.setCreditScore(100);
        return entity;
    }

    private PropResponse propResponse(PropEntity prop) {
        PropResponse response = new PropResponse();
        response.setId(prop.getId());
        response.setName(prop.getPropName());
        response.setImage(prop.getImageUrl());
        response.setImageUrl(prop.getImageUrl());
        response.setPrice(BigDecimal.valueOf(prop.getDailyRentPriceFen(), 2));
        response.setDeposit(BigDecimal.valueOf(prop.getDepositAmountFen(), 2));
        response.setSupplierUserId(prop.getSupplierUserId());
        response.setCanRent(true);
        response.setAvailableStock(10);
        return response;
    }
}
