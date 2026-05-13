package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.OrderScanLogEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderScanLogMapper {

    @Insert("""
        INSERT INTO order_scan_log (
            order_id, order_item_id, prop_id, qr_code_id, scan_type, scan_status,
            scan_user_id, supplier_user_id, raw_scan_result, fail_reason, created_at
        ) VALUES (
            #{orderId}, #{orderItemId}, #{propId}, #{qrCodeId}, #{scanType}, #{scanStatus},
            #{scanUserId}, #{supplierUserId}, #{rawScanResult}, #{failReason}, NOW()
        )
        """)
    int insert(OrderScanLogEntity entity);
}
