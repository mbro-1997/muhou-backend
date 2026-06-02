package com.muhou.backend.web.controller;

import com.muhou.backend.common.api.ApiResponse;
import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.exception.BizException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private static final Path IMAGE_ROOT = Path.of("uploads/images").toAbsolutePath().normalize();
    private static final Path LEGACY_DISPUTE_EVIDENCE_ROOT = Path.of("uploads/dispute-evidence").toAbsolutePath().normalize();
    private static final long MAX_IMAGE_BYTES = 10L * 1024 * 1024;
    private static final Set<String> BIZ_TYPES = Set.of("prop", "factory-license", "dispute", "order-evidence", "avatar");

    @PostMapping("/images")
    public ApiResponse<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file,
                                                        @RequestParam("bizType") String bizType) {
        return saveImage(file, bizType);
    }

    @PostMapping("/dispute-evidence")
    public ApiResponse<Map<String, String>> uploadDisputeEvidence(@RequestParam("file") MultipartFile file) {
        return saveImage(file, "dispute");
    }

    @GetMapping("/images/{bizType}/{date}/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String bizType,
                                             @PathVariable String date,
                                             @PathVariable String fileName) {
        String normalizedBizType = normalizeBizType(bizType);
        if (date == null || !date.matches("\\d{8}")) {
            throw new BizException(ResultCode.NOT_FOUND, "图片不存在");
        }
        Path file = IMAGE_ROOT.resolve(normalizedBizType).resolve(date).resolve(fileName).normalize();
        if (!file.startsWith(IMAGE_ROOT) || !Files.isRegularFile(file)) {
            throw new BizException(ResultCode.NOT_FOUND, "图片不存在");
        }
        return imageResponse(file);
    }

    @GetMapping("/dispute-evidence/{fileName:.+}")
    public ResponseEntity<Resource> getDisputeEvidence(@PathVariable String fileName) {
        Path legacyFile = LEGACY_DISPUTE_EVIDENCE_ROOT.resolve(fileName).normalize();
        if (legacyFile.startsWith(LEGACY_DISPUTE_EVIDENCE_ROOT) && Files.isRegularFile(legacyFile)) {
            return imageResponse(legacyFile);
        }
        throw new BizException(ResultCode.NOT_FOUND, "图片不存在");
    }

    private ApiResponse<Map<String, String>> saveImage(MultipartFile file, String bizType) {
        if (file == null || file.isEmpty()) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "图片不能为空");
        }
        if (file.getSize() > MAX_IMAGE_BYTES) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "图片不能超过 10MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "只能上传图片");
        }

        String normalizedBizType = normalizeBizType(bizType);
        String ext = resolveExtension(file.getOriginalFilename(), contentType);
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        Path targetDir = IMAGE_ROOT.resolve(normalizedBizType).resolve(date).normalize();
        if (!targetDir.startsWith(IMAGE_ROOT)) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "业务类型不合法");
        }

        try {
            Files.createDirectories(targetDir);
            String fileName = UUID.randomUUID() + ext;
            Path target = targetDir.resolve(fileName).normalize();
            if (!target.startsWith(targetDir)) {
                throw new BizException(ResultCode.VALIDATION_ERROR, "文件名非法");
            }
            file.transferTo(target);
            return ApiResponse.success(Map.of("url", "/api/files/images/" + normalizedBizType + "/" + date + "/" + fileName));
        } catch (IOException ex) {
            throw new BizException("保存图片失败：" + ex.getMessage());
        }
    }

    private String normalizeBizType(String bizType) {
        String normalized = bizType == null ? "" : bizType.trim().toLowerCase();
        if (!BIZ_TYPES.contains(normalized)) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "不支持的图片业务类型");
        }
        return normalized;
    }

    private String resolveExtension(String originalFileName, String contentType) {
        if (originalFileName != null) {
            String lower = originalFileName.toLowerCase();
            int dotIndex = lower.lastIndexOf('.');
            if (dotIndex >= 0 && dotIndex < lower.length() - 1) {
                String ext = lower.substring(dotIndex);
                if (ext.matches("\\.(png|jpg|jpeg|gif|webp)")) {
                    return ext;
                }
            }
        }
        if ("image/png".equalsIgnoreCase(contentType)) {
            return ".png";
        }
        if ("image/gif".equalsIgnoreCase(contentType)) {
            return ".gif";
        }
        if ("image/webp".equalsIgnoreCase(contentType)) {
            return ".webp";
        }
        return ".jpg";
    }

    private ResponseEntity<Resource> imageResponse(Path file) {
        String fileName = file.getFileName().toString();
        return ResponseEntity.ok()
            .contentType(resolveMediaType(fileName))
            .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic())
            .body(new FileSystemResource(file));
    }

    private MediaType resolveMediaType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (lower.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        }
        if (lower.endsWith(".webp")) {
            return MediaType.valueOf("image/webp");
        }
        return MediaType.IMAGE_JPEG;
    }
}
