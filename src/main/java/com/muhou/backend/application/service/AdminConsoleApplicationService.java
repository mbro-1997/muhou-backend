package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.exception.BizException;
import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.infrastructure.persistence.entity.DisputeReasonConfigEntity;
import com.muhou.backend.infrastructure.persistence.entity.OrderAdminActionEntity;
import com.muhou.backend.infrastructure.persistence.mapper.AdminConsoleMapper;
import com.muhou.backend.infrastructure.persistence.mapper.DisputeReasonConfigMapper;
import com.muhou.backend.infrastructure.persistence.mapper.OrderAdminActionMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserRoleMapper;
import com.muhou.backend.web.request.AdminOrderActionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminConsoleApplicationService {

    private final AdminConsoleMapper adminConsoleMapper;
    private final OrderAdminActionMapper orderAdminActionMapper;
    private final DisputeReasonConfigMapper disputeReasonConfigMapper;
    private final CurrentUserSupport currentUserSupport;
    private final UserRoleMapper userRoleMapper;

    public AdminConsoleApplicationService(AdminConsoleMapper adminConsoleMapper,
                                          OrderAdminActionMapper orderAdminActionMapper,
                                          DisputeReasonConfigMapper disputeReasonConfigMapper,
                                          CurrentUserSupport currentUserSupport,
                                          UserRoleMapper userRoleMapper) {
        this.adminConsoleMapper = adminConsoleMapper;
        this.orderAdminActionMapper = orderAdminActionMapper;
        this.disputeReasonConfigMapper = disputeReasonConfigMapper;
        this.currentUserSupport = currentUserSupport;
        this.userRoleMapper = userRoleMapper;
    }

    public Map<String, Object> dashboard() {
        requireAdminRole();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("pendingDisputeCount", adminConsoleMapper.countPendingDisputes());
        data.put("serviceFollowCount", adminConsoleMapper.countServiceFollowOrders());
        data.put("pendingSettlementCount", adminConsoleMapper.countPendingSettlementOrders());
        data.put("abnormalCancelCount", adminConsoleMapper.countAbnormalCancelledOrders());
        data.put("outboundTimeoutCount", adminConsoleMapper.countOutboundTimeoutOrders());
        data.put("returnTimeoutCount", adminConsoleMapper.countReturnTimeoutOrders());
        return data;
    }

    public List<Map<String, Object>> listOrders(String orderNo,
                                                String demanderKeyword,
                                                String supplierKeyword,
                                                String orderStatus,
                                                String payStatus,
                                                String refundStatus,
                                                Boolean hasDispute,
                                                Boolean hasPendingDispute,
                                                int page,
                                                int pageSize) {
        requireAdminRole();
        int safePageSize = normalizePageSize(pageSize);
        return adminConsoleMapper.selectOrders(orderNo, demanderKeyword, supplierKeyword, orderStatus, payStatus,
            refundStatus, hasDispute, hasPendingDispute, safePageSize, offset(page, safePageSize));
    }

    public Map<String, Object> getOrderDetail(Long orderId) {
        requireAdminRole();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("base", adminConsoleMapper.selectOrderDetail(orderId));
        data.put("instances", adminConsoleMapper.selectOrderInstances(orderId));
        data.put("actions", orderAdminActionMapper.selectByOrderId(orderId));
        return data;
    }

    public List<OrderAdminActionEntity> listOrderActions(Long orderId) {
        requireAdminRole();
        return orderAdminActionMapper.selectByOrderId(orderId);
    }

    @Transactional
    public List<OrderAdminActionEntity> addOrderAction(Long orderId, AdminOrderActionRequest request) {
        requireAdminRole();
        OrderAdminActionEntity entity = new OrderAdminActionEntity();
        entity.setOrderId(orderId);
        entity.setActionType(blankToDefault(request.getActionType(), "add_note"));
        entity.setActionStatus("done");
        entity.setAmountFen(request.getAmount() == null ? null : request.getAmount().movePointRight(2).intValue());
        entity.setReason(request.getReason());
        entity.setInternalNote(request.getInternalNote());
        entity.setOperatorUserId(currentUserSupport.requireCurrentUserId());
        orderAdminActionMapper.insert(entity);
        return orderAdminActionMapper.selectByOrderId(orderId);
    }

    public List<Map<String, Object>> listFundFlows(String orderNo,
                                                   String flowType,
                                                   String receiverRole,
                                                   String channelStatus,
                                                   int page,
                                                   int pageSize) {
        requireAdminRole();
        int safePageSize = normalizePageSize(pageSize);
        return adminConsoleMapper.selectFundFlows(orderNo, flowType, receiverRole, channelStatus, safePageSize, offset(page, safePageSize));
    }

    public List<Map<String, Object>> listPropInstances(String keyword,
                                                       Long supplierId,
                                                       Long propId,
                                                       String instanceStatus,
                                                       Long currentOrderId,
                                                       int page,
                                                       int pageSize) {
        requireAdminRole();
        int safePageSize = normalizePageSize(pageSize);
        return adminConsoleMapper.selectPropInstances(keyword, supplierId, propId, instanceStatus, currentOrderId, safePageSize, offset(page, safePageSize));
    }

    public List<OrderAdminActionEntity> listActions(String orderNo,
                                                    String actionType,
                                                    Long operatorUserId,
                                                    String targetRole,
                                                    Long targetUserId,
                                                    int page,
                                                    int pageSize) {
        requireAdminRole();
        int safePageSize = normalizePageSize(pageSize);
        return orderAdminActionMapper.selectAdminPage(orderNo, actionType, operatorUserId, targetRole, targetUserId, safePageSize, offset(page, safePageSize));
    }

    public List<DisputeReasonConfigEntity> listDisputeReasons(String stage, String role) {
        if (stage != null && !stage.isBlank() && role != null && !role.isBlank()) {
            return disputeReasonConfigMapper.selectEnabled(stage, role);
        }
        requireAdminRole();
        return disputeReasonConfigMapper.selectAll();
    }

    private void requireAdminRole() {
        String currentRole = currentUserSupport.getCurrentRole();
        Long currentUserId = currentUserSupport.requireCurrentUserId();
        if (!"admin".equals(currentRole) && !userRoleMapper.selectRoleCodesByUserId(currentUserId).contains("admin")) {
            throw new BizException(ResultCode.FORBIDDEN, "当前账号不是管理员，无法访问电脑端后台");
        }
    }

    private int normalizePageSize(int pageSize) {
        if (pageSize <= 0) {
            return 20;
        }
        return Math.min(pageSize, 100);
    }

    private int offset(int page, int pageSize) {
        return Math.max(0, (Math.max(1, page) - 1) * pageSize);
    }

    private String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }
}
