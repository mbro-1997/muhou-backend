package com.muhou.backend.common.constants;

import java.util.List;

public final class PropOptionConstants {

    public static final List<String> STYLE_OPTIONS = List.of(
        "现代",
        "古风",
        "欧式",
        "中式",
        "科幻",
        "赛博朋克",
        "复古",
        "通用"
    );

    public static final List<String> TYPE_OPTIONS = List.of(
        "舞台道具",
        "景片模组",
        "布景道具",
        "特效设备",
        "幕布软景",
        "桌椅家具",
        "装置艺术",
        "其他道具"
    );

    public static final List<String> FIRE_RESISTANT_OPTIONS = List.of(
        "是",
        "否",
        "可喷阻燃液"
    );

    private PropOptionConstants() {
    }
}
