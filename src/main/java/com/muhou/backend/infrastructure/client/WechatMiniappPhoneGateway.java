package com.muhou.backend.infrastructure.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muhou.backend.common.config.properties.MuhouAppProperties;
import com.muhou.backend.common.exception.BizException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Component
public class WechatMiniappPhoneGateway implements WechatPhoneGateway {

    private final MuhouAppProperties properties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private volatile String cachedAccessToken;
    private volatile Instant accessTokenExpireAt = Instant.EPOCH;

    public WechatMiniappPhoneGateway(MuhouAppProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public WechatPhoneNumberResult getPhoneNumber(String code) {
        if (blank(code)) {
            throw new BizException("微信手机号授权缺少 code");
        }

        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + encode(accessToken);
        String body = "{\"code\":\"" + escapeJson(code) + "\"}";
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
            .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            JsonNode root = objectMapper.readTree(response.body());
            int errcode = root.path("errcode").asInt(-1);
            if (errcode != 0) {
                throw new BizException("微信手机号授权失败：" + root.path("errmsg").asText("未知错误"));
            }
            JsonNode phoneInfo = root.path("phone_info");
            String phoneNumber = phoneInfo.path("phoneNumber").asText("");
            if (blank(phoneNumber)) {
                throw new BizException("微信手机号授权失败：未返回手机号");
            }
            return new WechatPhoneNumberResult(
                phoneNumber,
                phoneInfo.path("purePhoneNumber").asText(phoneNumber),
                phoneInfo.path("countryCode").asText("")
            );
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new BizException("调用微信手机号接口失败：" + ex.getMessage());
        }
    }

    private String getAccessToken() {
        Instant now = Instant.now();
        if (!blank(cachedAccessToken) && now.isBefore(accessTokenExpireAt.minusSeconds(60))) {
            return cachedAccessToken;
        }
        synchronized (this) {
            now = Instant.now();
            if (!blank(cachedAccessToken) && now.isBefore(accessTokenExpireAt.minusSeconds(60))) {
                return cachedAccessToken;
            }
            refreshAccessToken();
            return cachedAccessToken;
        }
    }

    private void refreshAccessToken() {
        String appId = properties.getWechat().getAppId();
        String appSecret = properties.getWechat().getAppSecret();
        if (blank(appId) || blank(appSecret)) {
            throw new BizException("微信手机号授权未配置 appId/appSecret");
        }

        String url = "https://api.weixin.qq.com/cgi-bin/token"
            + "?grant_type=client_credential"
            + "&appid=" + encode(appId)
            + "&secret=" + encode(appSecret);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            JsonNode root = objectMapper.readTree(response.body());
            if (root.hasNonNull("errcode") && root.get("errcode").asInt() != 0) {
                throw new BizException("获取微信 access_token 失败：" + root.path("errmsg").asText("未知错误"));
            }
            String token = root.path("access_token").asText("");
            if (blank(token)) {
                throw new BizException("获取微信 access_token 失败：未返回 access_token");
            }
            int expiresIn = root.path("expires_in").asInt(7200);
            cachedAccessToken = token;
            accessTokenExpireAt = Instant.now().plusSeconds(Math.max(300, expiresIn));
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new BizException("调用微信 access_token 接口失败：" + ex.getMessage());
        }
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
