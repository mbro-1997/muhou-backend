package com.muhou.backend.application.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDisputeStageRuleTest {

    @Test
    void disputeApplyStageRoleMatrixIsRestricted() throws Exception {
        OrderApplicationService service = new OrderApplicationService(
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null
        );
        Method method = OrderApplicationService.class
            .getDeclaredMethod("canRoleApplyDisputeAtStage", String.class, String.class);
        method.setAccessible(true);

        assertThat((Boolean) method.invoke(service, "pending_factory_confirm", "demander")).isFalse();
        assertThat((Boolean) method.invoke(service, "wait_pickup", "supplier")).isFalse();
        assertThat((Boolean) method.invoke(service, "renting", "demander")).isTrue();
        assertThat((Boolean) method.invoke(service, "renting", "supplier")).isFalse();
        assertThat((Boolean) method.invoke(service, "wait_review", "supplier")).isTrue();
        assertThat((Boolean) method.invoke(service, "wait_review", "demander")).isFalse();
        assertThat((Boolean) method.invoke(service, "completed", "demander")).isFalse();
    }
}
