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

@Component
public class MockWechatAuthGateway implements WechatAuthGateway {

    private final MuhouAppProperties properties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public MockWechatAuthGateway(MuhouAppProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthLoginResult login(String code) {
        return login(code, false);
    }

    @Override
    public AuthLoginResult login(String code, boolean forceReal) {
        if (properties.getWechat().isMockEnabled() && !forceReal) {
            String suffix = code == null ? "demo" : code;
            return new AuthLoginResult("mock-openid-" + suffix, null, "mock-session-key", "mock-token-" + suffix, true);
        }
        return loginByCode2Session(code);
    }

    private AuthLoginResult loginByCode2Session(String code) {
        String appId = properties.getWechat().getAppId();
        String appSecret = properties.getWechat().getAppSecret();
        if (blank(appId) || blank(appSecret)) {
            String missing = blank(appId) && blank(appSecret) ? "appId/appSecret" : (blank(appId) ? "appId" : "appSecret");
            throw new BizException("微信登录未配置 " + missing + "，请先完善 muhou.wechat 配置");
        }
        if (blank(code)) {
            throw new BizException("微信登录缺少 code");
        }

        String url = "https://api.weixin.qq.com/sns/jscode2session"
            + "?appid=" + encode(appId)
            + "&secret=" + encode(appSecret)
            + "&js_code=" + encode(code)
            + "&grant_type=authorization_code";

        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            JsonNode root = objectMapper.readTree(response.body());
            if (root.hasNonNull("errcode") && root.get("errcode").asInt() != 0) {
                throw new BizException("微信登录失败：" + root.path("errmsg").asText("未知错误"));
            }
            return new AuthLoginResult(
                root.path("openid").asText(),
                root.path("unionid").asText(null),
                root.path("session_key").asText(),
                null,
                false
            );
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new BizException("调用微信登录接口失败：" + ex.getMessage());
        }
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
