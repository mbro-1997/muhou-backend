package com.muhou.backend.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtils {

    private MoneyUtils() {
    }

    public static BigDecimal fenToYuan(Integer fen) {
        if (fen == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(fen).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
