package com.muhou.backend.common.support;

public final class StatusTextHelper {

    private StatusTextHelper() {
    }

    public static String propStatusText(String status) {
        return switch (status) {
            case "idle" -> "空闲";
            case "locked" -> "已锁定";
            case "renting" -> "租赁中";
            case "offline" -> "已下架";
            default -> status == null ? "未知状态" : status;
        };
    }

    public static String auditStatusText(String status) {
        return switch (status) {
            case "approved" -> "已通过";
            case "rejected" -> "已驳回";
            case "pending" -> "待审核";
            default -> status == null ? "未知状态" : status;
        };
    }

    public static String projectStatusText(String status) {
        return switch (status) {
            case "editing" -> "编辑中";
            case "ordered" -> "已下单";
            default -> status == null ? "未知状态" : status;
        };
    }

    public static String orderStatusText(String status) {
        return switch (status) {
            case "pending_factory_confirm" -> "待工厂确认";
            case "wait_pickup" -> "待取货";
            case "renting" -> "租赁中";
            case "wait_review" -> "待评价";
            case "completed" -> "已完成";
            case "cancelled_timeout" -> "超时取消";
            case "cancelled_manual" -> "人工取消";
            default -> status == null ? "未知状态" : status;
        };
    }

    public static boolean isPropOrderable(String status) {
        return "idle".equals(status);
    }
}
