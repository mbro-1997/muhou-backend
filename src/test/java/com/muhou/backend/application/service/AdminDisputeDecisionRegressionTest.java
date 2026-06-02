package com.muhou.backend.application.service;

import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.infrastructure.persistence.entity.DisputeEntity;
import com.muhou.backend.infrastructure.persistence.entity.OrderAdminActionEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropAuditEntity;
import com.muhou.backend.infrastructure.persistence.entity.RentalOrderEntity;
import com.muhou.backend.infrastructure.persistence.mapper.DisputeMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderAdminActionMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderReviewMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropAuditMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropImageMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropMapper;
import com.muhou.backend.infrastructure.persistence.mapper.RentalOrderMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserRoleMapper;
import com.muhou.backend.web.controller.AdminController;
import com.muhou.backend.web.response.FactoryInviteCreateResponse;
import com.muhou.backend.web.response.PropResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminDisputeDecisionRegressionTest {

    @Mock private PropAuditMapper propAuditMapper;
    @Mock private DisputeMapper disputeMapper;
    @Mock private OrderReviewMapper orderReviewMapper;
    @Mock private PropMapper propMapper;
    @Mock private PropImageMapper propImageMapper;
    @Mock private RentalOrderMapper rentalOrderMapper;
    @Mock private UserApplicationService userApplicationService;
    @Mock private CurrentUserSupport currentUserSupport;
    @Mock private FactoryOnboardingApplicationService factoryOnboardingApplicationService;
    @Mock private UserRoleMapper userRoleMapper;
    @Mock private OrderSettlementApplicationService orderSettlementApplicationService;
    @Mock private OrderAdminActionMapper orderAdminActionMapper;
    @Mock private PropApplicationService propApplicationService;

    private AdminApplicationService service;

    @BeforeEach
    void setUp() {
        service = new AdminApplicationService(
            propAuditMapper,
            disputeMapper,
            orderReviewMapper,
            propMapper,
            propImageMapper,
            rentalOrderMapper,
            userApplicationService,
            currentUserSupport,
            factoryOnboardingApplicationService,
            userRoleMapper,
            orderSettlementApplicationService,
            orderAdminActionMapper,
            propApplicationService
        );
    }

    @Test
    void oldResolveEndpointIsNotExposed() {
        boolean hasOldResolve = Arrays.stream(AdminController.class.getDeclaredMethods())
            .map(method -> method.getAnnotation(PostMapping.class))
            .filter(annotation -> annotation != null)
            .flatMap(annotation -> Arrays.stream(annotation.value()))
            .anyMatch(path -> path.contains("/disputes/{id}/resolve"));

        assertThat(hasOldResolve).isFalse();
    }

    @Test
    void rentingStageDecisionStaysPendingSettlementEvenIfOrderAlreadyWaitReview() {
        stubAdminDecisionDependencies();
        DisputeEntity dispute = pendingDispute("renting", "demander");
        RentalOrderEntity order = order("wait_review");
        when(disputeMapper.selectById(10L)).thenReturn(dispute);
        when(rentalOrderMapper.selectById(100L)).thenReturn(order);
        when(disputeMapper.decide(eq(10L), eq("resolved"), any(), eq("approved"), eq("refund_reserved"),
            eq(2000), eq("pending_settlement"), eq("approve_demander_compensation"), eq(3L))).thenReturn(1);

        service.decideDispute(10L, "approve_demander_compensation", 2000, "renting stage compensation");

        verify(rentalOrderMapper, never()).markReviewed(eq(100L), any());
        verify(orderSettlementApplicationService, never()).settleFinalOrder(any(), any());
    }

    @Test
    void waitReviewSupplierDecisionRunsFinalSettlement() {
        stubAdminDecisionDependencies();
        DisputeEntity dispute = pendingDispute("wait_review", "supplier");
        DisputeEntity decided = pendingDispute("wait_review", "supplier");
        decided.setDisputeStatus("resolved");
        decided.setAdminDecisionAmountFen(3000);
        RentalOrderEntity order = order("wait_review");
        when(disputeMapper.selectById(10L)).thenReturn(dispute, decided);
        when(rentalOrderMapper.selectById(100L)).thenReturn(order);
        when(disputeMapper.decide(eq(10L), eq("resolved"), any(), eq("approved"), eq("refund_reserved"),
            eq(3000), eq("pending_settlement"), eq("approve_supplier_compensation"), eq(3L))).thenReturn(1);
        when(disputeMapper.selectByOrderId(100L)).thenReturn(List.of(decided));

        service.decideDispute(10L, "approve_supplier_compensation", 3000, "wait review compensation");

        verify(rentalOrderMapper).markReviewed(eq(100L), any());
        verify(orderSettlementApplicationService).settleFinalOrder(eq(order), any());
    }

    @Test
    void bindRoleWritesGlobalAdminAction() {
        stubAdminUserActionDependencies();
        when(userApplicationService.listAdminUsers()).thenReturn(List.of());

        service.bindUserRole(88L, "supplier");

        org.mockito.ArgumentCaptor<OrderAdminActionEntity> captor =
            org.mockito.ArgumentCaptor.forClass(OrderAdminActionEntity.class);
        verify(orderAdminActionMapper).insert(captor.capture());
        OrderAdminActionEntity action = captor.getValue();
        assertThat(action.getOrderId()).isNull();
        assertThat(action.getActionType()).isEqualTo("bind_user_role");
        assertThat(action.getTargetRole()).isEqualTo("user");
        assertThat(action.getTargetUserId()).isEqualTo(88L);
        assertThat(action.getInternalNote()).contains("role=supplier");
    }

    @Test
    void propAuditReviewWritesGlobalAdminAction() {
        stubAdminUserActionDependencies();
        PropAuditEntity audit = new PropAuditEntity();
        audit.setId(7L);
        audit.setPropId(9L);
        audit.setActionType("create");
        audit.setSupplierUserId(22L);
        when(propAuditMapper.selectById(7L)).thenReturn(audit);
        when(propAuditMapper.selectPendingList()).thenReturn(List.of());

        service.reviewPropAudit(7L, true, "ok");

        org.mockito.ArgumentCaptor<OrderAdminActionEntity> captor =
            org.mockito.ArgumentCaptor.forClass(OrderAdminActionEntity.class);
        verify(orderAdminActionMapper).insert(captor.capture());
        OrderAdminActionEntity action = captor.getValue();
        assertThat(action.getOrderId()).isNull();
        assertThat(action.getActionType()).isEqualTo("review_prop_audit_approved");
        assertThat(action.getTargetRole()).isEqualTo("supplier");
        assertThat(action.getTargetUserId()).isEqualTo(22L);
        assertThat(action.getInternalNote()).contains("propAuditId=7", "propId=9");
    }

    @Test
    void qrAndInviteAdminActionsAreAudited() {
        stubAdminUserActionDependencies();
        PropResponse prop = new PropResponse();
        prop.setId(11L);
        prop.setQrCodeId("QR-001");
        when(propApplicationService.createPendingFillProp()).thenReturn(prop);
        FactoryInviteCreateResponse invite = new FactoryInviteCreateResponse();
        invite.setId(12L);
        invite.setCodeSuffix("ABCD");
        when(factoryOnboardingApplicationService.createInviteCode(7, "remark")).thenReturn(invite);

        service.createPendingFillProp();
        service.createFactoryInviteCode(7, "remark");

        org.mockito.ArgumentCaptor<OrderAdminActionEntity> captor =
            org.mockito.ArgumentCaptor.forClass(OrderAdminActionEntity.class);
        verify(orderAdminActionMapper, atLeastOnce()).insert(captor.capture());
        assertThat(captor.getAllValues())
            .extracting(OrderAdminActionEntity::getActionType)
            .contains("generate_prop_qr_code", "generate_factory_invite_code");
        assertThat(captor.getAllValues())
            .allSatisfy(action -> assertThat(action.getOrderId()).isNull());
    }

    private void stubAdminDecisionDependencies() {
        when(currentUserSupport.getCurrentRole()).thenReturn("admin");
        when(currentUserSupport.requireCurrentUserId()).thenReturn(3L);
        when(userApplicationService.resolveAdminUserId()).thenReturn(3L);
        when(disputeMapper.selectAll()).thenReturn(List.of());
    }

    private void stubAdminUserActionDependencies() {
        when(currentUserSupport.getCurrentRole()).thenReturn("admin");
        when(currentUserSupport.requireCurrentUserId()).thenReturn(3L);
        when(userApplicationService.resolveAdminUserId()).thenReturn(3L);
    }

    private DisputeEntity pendingDispute(String applyStage, String applicantRole) {
        DisputeEntity entity = new DisputeEntity();
        entity.setId(10L);
        entity.setOrderId(100L);
        entity.setApplyUserId("supplier".equals(applicantRole) ? 20L : 30L);
        entity.setApplicantRole(applicantRole);
        entity.setApplyStage(applyStage);
        entity.setDisputeStatus("pending");
        return entity;
    }

    private RentalOrderEntity order(String status) {
        RentalOrderEntity entity = new RentalOrderEntity();
        entity.setId(100L);
        entity.setOrderNo("RO-100");
        entity.setOrderStatus(status);
        entity.setPayStatus("paid");
        entity.setRentAmountFen(10000);
        entity.setDepositAmountFen(5000);
        return entity;
    }
}
