package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.exception.BizException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class QrCodeArchiveService {

    private static final String STORAGE_ROOT = "uploads/qrcode";

    public QrCodeArchiveResult save(String qrCodeId, byte[] imageBytes) {
        if (qrCodeId == null || qrCodeId.isBlank()) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "二维码标识不能为空");
        }
        if (imageBytes == null || imageBytes.length == 0) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "二维码图片不能为空");
        }

        try {
            Path root = Path.of(STORAGE_ROOT).toAbsolutePath().normalize();
            Files.createDirectories(root);
            String fileName = safeFileName(qrCodeId) + ".png";
            Path file = root.resolve(fileName).normalize();
            if (!file.startsWith(root)) {
                throw new BizException(ResultCode.VALIDATION_ERROR, "二维码文件名非法");
            }
            Files.write(file, imageBytes);
            return new QrCodeArchiveResult(
                "/api/qrcodes/" + qrCodeId + "/image",
                file.toString(),
                sha256(imageBytes)
            );
        } catch (IOException ex) {
            throw new BizException("保存二维码图片失败：" + ex.getMessage());
        }
    }

    public Resource loadRequired(String storageKey) {
        if (storageKey == null || storageKey.isBlank()) {
            throw new BizException(ResultCode.NOT_FOUND, "二维码图片已遗失");
        }
        Path file = Path.of(storageKey).toAbsolutePath().normalize();
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new BizException(ResultCode.NOT_FOUND, "二维码图片已遗失");
        }
        return new FileSystemResource(file);
    }

    private String safeFileName(String value) {
        return value.replaceAll("[^A-Za-z0-9_-]", "_");
    }

    private String sha256(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(bytes));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 not available", ex);
        }
    }

    public record QrCodeArchiveResult(String imageUrl, String storageKey, String sha256) {
    }
}
