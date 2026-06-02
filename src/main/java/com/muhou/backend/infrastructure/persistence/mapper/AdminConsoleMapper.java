package com.muhou.backend.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminConsoleMapper {

    @Select("SELECT COUNT(1) FROM order_dispute WHERE dispute_status = 'pending'")
    int countPendingDisputes();

    @Select("SELECT COUNT(1) FROM rental_order WHERE order_status = 'wait_review' AND pay_status = 'paid'")
    int countPendingSettlementOrders();

    @Select("SELECT COUNT(1) FROM rental_order WHERE cancel_type IS NOT NULL")
    int countAbnormalCancelledOrders();

    @Select("SELECT COUNT(1) FROM rental_order WHERE order_status = 'wait_pickup' AND confirmed_at IS NOT NULL AND confirmed_at <= DATE_SUB(NOW(), INTERVAL 1 DAY)")
    int countOutboundTimeoutOrders();

    @Select("SELECT COUNT(1) FROM rental_order WHERE order_status = 'renting' AND rental_end_date IS NOT NULL AND rental_end_date < CURDATE()")
    int countReturnTimeoutOrders();

    @Select("SELECT COUNT(1) FROM order_admin_action WHERE action_type = 'add_note'")
    int countServiceFollowOrders();

    @Select("""
        <script>
        SELECT
          o.id AS orderId,
          o.order_no AS orderNo,
          du.nickname AS demanderName,
          su.nickname AS supplierName,
          o.order_status AS orderStatus,
          o.order_status AS orderStatusText,
          o.pay_status AS payStatus,
          o.refund_status AS refundStatus,
          o.rent_amount_fen AS rentAmount,
          o.deposit_amount_fen AS depositAmount,
          o.total_amount_fen AS totalAmount,
          o.rental_start_date AS rentalStartDate,
          o.rental_end_date AS rentalEndDate,
          COALESCE(oi.outbound_scanned_count, 0) AS outboundScannedCount,
          COALESCE(oi.return_scanned_count, 0) AS returnScannedCount,
          COALESCE(oi.prop_count, 0) AS propCount,
          COALESCE(d.dispute_count, 0) AS disputeCount,
          COALESCE(d.pending_dispute_count, 0) AS pendingDisputeCount,
          o.updated_at AS lastUpdatedAt
        FROM rental_order o
        LEFT JOIN sys_user du ON du.id = o.demander_user_id
        LEFT JOIN sys_user su ON su.id = o.supplier_user_id
        LEFT JOIN (
          SELECT order_id,
                 COUNT(1) AS prop_count,
                 SUM(CASE WHEN outbound_status = 'scanned' THEN 1 ELSE 0 END) AS outbound_scanned_count,
                 SUM(CASE WHEN return_status = 'scanned' THEN 1 ELSE 0 END) AS return_scanned_count
          FROM order_item_instance
          GROUP BY order_id
        ) oi ON oi.order_id = o.id
        LEFT JOIN (
          SELECT order_id,
                 COUNT(1) AS dispute_count,
                 SUM(CASE WHEN dispute_status = 'pending' THEN 1 ELSE 0 END) AS pending_dispute_count
          FROM order_dispute
          GROUP BY order_id
        ) d ON d.order_id = o.id
        WHERE 1 = 1
        <if test='orderNo != null and orderNo != ""'>AND o.order_no LIKE CONCAT('%', #{orderNo}, '%')</if>
        <if test='demanderKeyword != null and demanderKeyword != ""'>AND (du.nickname LIKE CONCAT('%', #{demanderKeyword}, '%') OR du.phone LIKE CONCAT('%', #{demanderKeyword}, '%'))</if>
        <if test='supplierKeyword != null and supplierKeyword != ""'>AND su.nickname LIKE CONCAT('%', #{supplierKeyword}, '%')</if>
        <if test='orderStatus != null and orderStatus != ""'>AND o.order_status = #{orderStatus}</if>
        <if test='payStatus != null and payStatus != ""'>AND o.pay_status = #{payStatus}</if>
        <if test='refundStatus != null and refundStatus != ""'>AND o.refund_status = #{refundStatus}</if>
        <if test='hasPendingDispute != null and hasPendingDispute'>AND COALESCE(d.pending_dispute_count, 0) &gt; 0</if>
        <if test='hasDispute != null and hasDispute'>AND COALESCE(d.dispute_count, 0) &gt; 0</if>
        ORDER BY o.created_at DESC, o.id DESC
        LIMIT #{limit} OFFSET #{offset}
        </script>
        """)
    List<Map<String, Object>> selectOrders(@Param("orderNo") String orderNo,
                                           @Param("demanderKeyword") String demanderKeyword,
                                           @Param("supplierKeyword") String supplierKeyword,
                                           @Param("orderStatus") String orderStatus,
                                           @Param("payStatus") String payStatus,
                                           @Param("refundStatus") String refundStatus,
                                           @Param("hasDispute") Boolean hasDispute,
                                           @Param("hasPendingDispute") Boolean hasPendingDispute,
                                           @Param("limit") int limit,
                                           @Param("offset") int offset);

    @Select("""
        SELECT
          o.*,
          du.nickname AS demander_name,
          du.phone AS demander_phone,
          su.nickname AS supplier_name
        FROM rental_order o
        LEFT JOIN sys_user du ON du.id = o.demander_user_id
        LEFT JOIN sys_user su ON su.id = o.supplier_user_id
        WHERE o.id = #{orderId}
        """)
    Map<String, Object> selectOrderDetail(@Param("orderId") Long orderId);

    @Select("""
        SELECT
          oi.*,
          p.qr_code_id AS qr_code_id,
          p.instance_no AS instance_no,
          p.instance_status AS instance_status,
          p.current_order_id AS current_order_id
        FROM order_item_instance oi
        LEFT JOIN prop_instance p ON p.id = oi.prop_instance_id
        WHERE oi.order_id = #{orderId}
        ORDER BY oi.prop_id, oi.id
        """)
    List<Map<String, Object>> selectOrderInstances(@Param("orderId") Long orderId);

    @Select("""
        <script>
        SELECT
          f.*,
          o.order_no AS orderNo
        FROM order_fund_flow f
        LEFT JOIN rental_order o ON o.id = f.order_id
        WHERE 1 = 1
        <if test='orderNo != null and orderNo != ""'>AND o.order_no LIKE CONCAT('%', #{orderNo}, '%')</if>
        <if test='flowType != null and flowType != ""'>AND f.flow_type = #{flowType}</if>
        <if test='receiverRole != null and receiverRole != ""'>AND f.receiver_role = #{receiverRole}</if>
        <if test='channelStatus != null and channelStatus != ""'>AND f.channel_status = #{channelStatus}</if>
        ORDER BY f.created_at DESC, f.id DESC
        LIMIT #{limit} OFFSET #{offset}
        </script>
        """)
    List<Map<String, Object>> selectFundFlows(@Param("orderNo") String orderNo,
                                              @Param("flowType") String flowType,
                                              @Param("receiverRole") String receiverRole,
                                              @Param("channelStatus") String channelStatus,
                                              @Param("limit") int limit,
                                              @Param("offset") int offset);

    @Select("""
        <script>
        SELECT
          pi.id AS instanceId,
          pi.prop_id AS propId,
          p.prop_name AS propName,
          pi.qr_code_id AS qrCodeId,
          pi.instance_no AS instanceNo,
          su.nickname AS supplierName,
          pi.instance_status AS instanceStatus,
          pi.current_order_id AS currentOrderId,
          o.order_no AS currentOrderNo,
          pi.created_at AS createdAt,
          pi.updated_at AS updatedAt
        FROM prop_instance pi
        LEFT JOIN prop_info p ON p.id = pi.prop_id
        LEFT JOIN sys_user su ON su.id = p.supplier_user_id
        LEFT JOIN rental_order o ON o.id = pi.current_order_id
        WHERE 1 = 1
        <if test='keyword != null and keyword != ""'>AND (p.prop_name LIKE CONCAT('%', #{keyword}, '%') OR pi.qr_code_id LIKE CONCAT('%', #{keyword}, '%') OR pi.instance_no LIKE CONCAT('%', #{keyword}, '%'))</if>
        <if test='supplierId != null'>AND p.supplier_user_id = #{supplierId}</if>
        <if test='propId != null'>AND pi.prop_id = #{propId}</if>
        <if test='instanceStatus != null and instanceStatus != ""'>AND pi.instance_status = #{instanceStatus}</if>
        <if test='currentOrderId != null'>AND pi.current_order_id = #{currentOrderId}</if>
        ORDER BY pi.updated_at DESC, pi.id DESC
        LIMIT #{limit} OFFSET #{offset}
        </script>
        """)
    List<Map<String, Object>> selectPropInstances(@Param("keyword") String keyword,
                                                  @Param("supplierId") Long supplierId,
                                                  @Param("propId") Long propId,
                                                  @Param("instanceStatus") String instanceStatus,
                                                  @Param("currentOrderId") Long currentOrderId,
                                                  @Param("limit") int limit,
                                                  @Param("offset") int offset);
}
