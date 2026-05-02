package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.RentalOrderItemEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RentalOrderItemMapper {

    @Insert({
        "<script>",
        "INSERT INTO rental_order_item (",
        "order_id, prop_id, prop_name_snapshot, image_url_snapshot, daily_rent_price_fen_snapshot, deposit_amount_fen_snapshot, outbound_status, return_status",
        ") VALUES ",
        "<foreach collection='items' item='item' separator=','>",
        "(",
        "#{item.orderId}, #{item.propId}, #{item.propNameSnapshot}, #{item.imageUrlSnapshot}, #{item.dailyRentPriceFenSnapshot}, #{item.depositAmountFenSnapshot}, #{item.outboundStatus}, #{item.returnStatus}",
        ")",
        "</foreach>",
        "</script>"
    })
    int batchInsert(@Param("items") List<RentalOrderItemEntity> items);

    @Select("SELECT * FROM rental_order_item WHERE order_id = #{orderId} ORDER BY id ASC")
    List<RentalOrderItemEntity> selectByOrderId(@Param("orderId") Long orderId);

    @Update("UPDATE rental_order_item SET outbound_status = 'scanned', outbound_scanned_at = #{scannedAt} WHERE order_id = #{orderId} AND prop_id = #{propId}")
    int markOutboundScanned(@Param("orderId") Long orderId, @Param("propId") Long propId, @Param("scannedAt") LocalDateTime scannedAt);

    @Update("UPDATE rental_order_item SET return_status = 'scanned', return_scanned_at = #{scannedAt} WHERE order_id = #{orderId} AND prop_id = #{propId}")
    int markReturnScanned(@Param("orderId") Long orderId, @Param("propId") Long propId, @Param("scannedAt") LocalDateTime scannedAt);

    @Select("SELECT COUNT(1) FROM rental_order_item WHERE order_id = #{orderId}")
    int countByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT COUNT(1) FROM rental_order_item WHERE order_id = #{orderId} AND outbound_status = 'scanned'")
    int countOutboundScannedByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT COUNT(1) FROM rental_order_item WHERE order_id = #{orderId} AND return_status = 'scanned'")
    int countReturnScannedByOrderId(@Param("orderId") Long orderId);
}
