package com.muhou.backend.common.support;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.config.properties.MuhouAppProperties;
import com.muhou.backend.common.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class CurrentUserSupport {

    private static final String TOKEN_PREFIX = "demo:";
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final MuhouAppProperties properties;

    public CurrentUserSupport(MuhouAppProperties properties) {
        this.properties = properties;
    }

    public String generateToken(Long userId, String role) {
        String payload = TOKEN_PREFIX + userId + ":" + role + ":" + System.currentTimeMillis();
        String payloadEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String signatureEncoded = Base64.getUrlEncoder().withoutPadding().encodeToString(sign(payloadEncoded));
        return payloadEncoded + "." + signatureEncoded;
    }

    public CurrentUserSession getCurrentSession() {
        HttpServletRequest request = currentRequest();
        if (request == null) {
            return null;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.isBlank()) {
            return null;
        }

        String rawToken = authorization.startsWith("Bearer ")
            ? authorization.substring(7).trim()
            : authorization.trim();
        return parseToken(rawToken);
    }

    public Long requireCurrentUserId() {
        CurrentUserSession session = getCurrentSession();
        if (session == null || session.getUserId() == null) {
            throw new BizException(ResultCode.UNAUTHORIZED, "当前未登录，请先从角色页进入系统");
        }
        return session.getUserId();
    }

    public String getCurrentRole() {
        CurrentUserSession session = getCurrentSession();
        return session == null ? null : session.getRole();
    }

    public CurrentUserSession parseToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            String[] sections = token.split("\\.");
            if (sections.length != 2) {
                return null;
            }

            String payloadEncoded = sections[0];
            String signatureEncoded = sections[1];
            byte[] expectedSignature = sign(payloadEncoded);
            byte[] actualSignature = Base64.getUrlDecoder().decode(signatureEncoded);
            if (!MessageDigest.isEqual(expectedSignature, actualSignature)) {
                return null;
            }

            byte[] bytes = Base64.getUrlDecoder().decode(payloadEncoded);
            String decoded = new String(bytes, StandardCharsets.UTF_8);
            if (!decoded.startsWith(TOKEN_PREFIX)) {
                return null;
            }
            String[] parts = decoded.split(":");
            if (parts.length < 4) {
                return null;
            }
            Long userId = Long.valueOf(parts[1]);
            String role = parts[2];
            return new CurrentUserSession(userId, role, token);
        } catch (Exception ex) {
            return null;
        }
    }

    private byte[] sign(String payloadEncoded) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(properties.getAuth().getTokenSecret().getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
            mac.init(keySpec);
            return mac.doFinal(payloadEncoded.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to sign token", ex);
        }
    }

    private HttpServletRequest currentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }
}
