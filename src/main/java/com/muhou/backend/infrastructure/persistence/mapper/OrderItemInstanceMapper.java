package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.OrderItemInstanceEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderItemInstanceMapper {

    @Insert({
        "<script>",
        "INSERT INTO order_item_instance (",
        "order_id, order_item_id, prop_id, prop_instance_id, outbound_status, return_status, created_at, updated_at",
        ") VALUES ",
        "<foreach collection='items' item='item' separator=','>",
        "(",
        "#{item.orderId}, #{item.orderItemId}, #{item.propId}, #{item.propInstanceId}, #{item.outboundStatus}, #{item.returnStatus}, NOW(), NOW()",
        ")",
        "</foreach>",
        "</script>"
    })
    int batchInsert(@Param("items") List<OrderItemInstanceEntity> items);

    @Select("SELECT * FROM order_item_instance WHERE order_id = #{orderId} AND prop_instance_id = #{propInstanceId} LIMIT 1")
    OrderItemInstanceEntity selectByOrderIdAndInstanceId(@Param("orderId") Long orderId, @Param("propInstanceId") Long propInstanceId);

    @Select("SELECT * FROM order_item_instance WHERE order_id = #{orderId} AND prop_id = #{propId} ORDER BY id ASC")
    List<OrderItemInstanceEntity> selectByOrderIdAndPropId(@Param("orderId") Long orderId, @Param("propId") Long propId);

    @Select("SELECT COUNT(1) FROM order_item_instance WHERE order_id = #{orderId}")
    int countByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT COUNT(1) FROM order_item_instance WHERE order_id = #{orderId} AND outbound_status = 'scanned'")
    int countOutboundScannedByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT COUNT(1) FROM order_item_instance WHERE order_id = #{orderId} AND return_status = 'scanned'")
    int countReturnScannedByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT COUNT(1) FROM order_item_instance WHERE order_item_id = #{orderItemId} AND outbound_status = 'scanned'")
    int countOutboundByOrderItemId(@Param("orderItemId") Long orderItemId);

    @Select("SELECT COUNT(1) FROM order_item_instance WHERE order_item_id = #{orderItemId} AND return_status = 'scanned'")
    int countReturnByOrderItemId(@Param("orderItemId") Long orderItemId);

    @Update("UPDATE order_item_instance SET outbound_status = 'scanned', outbound_scanned_at = #{scannedAt}, updated_at = NOW() WHERE id = #{id} AND outbound_status = 'pending'")
    int markOutboundScanned(@Param("id") Long id, @Param("scannedAt") LocalDateTime scannedAt);

    @Update("UPDATE order_item_instance SET return_status = 'scanned', return_scanned_at = #{scannedAt}, updated_at = NOW() WHERE id = #{id} AND return_status = 'pending'")
    int markReturnScanned(@Param("id") Long id, @Param("scannedAt") LocalDateTime scannedAt);
}
