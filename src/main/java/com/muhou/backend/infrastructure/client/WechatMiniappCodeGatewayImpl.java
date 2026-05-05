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
import java.util.Map;

@Component
public class WechatMiniappCodeGatewayImpl implements WechatMiniappCodeGateway {

    private final MuhouAppProperties properties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private volatile String cachedAccessToken;
    private volatile Instant accessTokenExpireAt = Instant.EPOCH;

    public WechatMiniappCodeGatewayImpl(MuhouAppProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] generateUnlimited(String scene, String page) {
        if (blank(scene) || blank(page)) {
            throw new BizException("小程序码参数不能为空");
        }

        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + encode(getAccessToken());
        try {
            String body = objectMapper.writeValueAsString(Map.of(
                "scene", scene,
                "page", page,
                "check_path", false,
                "env_version", "release"
            ));
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            byte[] bytes = response.body();
            if (looksLikeJson(bytes)) {
                JsonNode root = objectMapper.readTree(bytes);
                throw new BizException("生成微信小程序码失败：" + root.path("errmsg").asText("未知错误"));
            }
            if (bytes == null || bytes.length == 0) {
                throw new BizException("生成微信小程序码失败：微信接口未返回图片");
            }
            return bytes;
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new BizException("调用微信小程序码接口失败：" + ex.getMessage());
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
            throw new BizException("微信小程序码未配置 appId/appSecret");
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

    private boolean looksLikeJson(byte[] bytes) {
        if (bytes == null) {
            return false;
        }
        for (byte item : bytes) {
            if (Character.isWhitespace((char) item)) {
                continue;
            }
            return item == '{' || item == '[';
        }
        return false;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
