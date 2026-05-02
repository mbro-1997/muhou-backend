package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.constants.PropOptionConstants;
import com.muhou.backend.common.exception.BizException;
import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.common.support.StatusTextHelper;
import com.muhou.backend.common.util.MoneyUtils;
import com.muhou.backend.infrastructure.persistence.entity.PropAuditEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropImageEntity;
import com.muhou.backend.infrastructure.persistence.mapper.PropAuditMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropImageMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserRoleMapper;
import com.muhou.backend.web.request.CreatePropRequest;
import com.muhou.backend.web.response.PropResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class PropApplicationService {

    private static final String REAL_PROP_IMAGE_URL = "/images/stage-prop-real.jpg";

    private final PropMapper propMapper;
    private final PropAuditMapper propAuditMapper;
    private final PropImageMapper propImageMapper;
    private final CurrentUserSupport currentUserSupport;
    private final UserRoleMapper userRoleMapper;

    public PropApplicationService(PropMapper propMapper,
                                  PropAuditMapper propAuditMapper,
                                  PropImageMapper propImageMapper,
                                  CurrentUserSupport currentUserSupport,
                                  UserRoleMapper userRoleMapper) {
        this.propMapper = propMapper;
        this.propAuditMapper = propAuditMapper;
        this.propImageMapper = propImageMapper;
        this.currentUserSupport = currentUserSupport;
        this.userRoleMapper = userRoleMapper;
    }

    public List<PropResponse> listProps(String keyword, String style, String type, String status, String sortBy, String sortOrder) {
        return propMapper.selectList(keyword, style, type, status, sortBy, sortOrder)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public PropResponse getProp(Long id) {
        return toResponse(requireProp(id));
    }

    public PropResponse getPublicProp(Long id) {
        PropEntity prop = requireProp(id);
        if (!"filled".equals(prop.getFillStatus())
            || !"approved".equals(prop.getAuditStatus())
            || "offline".equals(prop.getPropStatus())) {
            throw new BizException(ResultCode.NOT_FOUND, "道具不存在");
        }
        return toResponse(prop);
    }

    public PropResponse getWarehouseProp(Long id) {
        return toResponse(requireOwnedProp(id));
    }

    public List<PropResponse> listWarehouseProps() {
        return propMapper.selectWarehouseList(currentSupplierUserId())
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<PropResponse> listPendingFillProps() {
        currentSupplierUserId();
        return propMapper.selectPendingFillList()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<PropResponse> listAdminPendingFillProps() {
        requireAdminRole();
        return propMapper.selectPendingFillList()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<PropResponse> listAdminQrCodeProps() {
        requireAdminRole();
        return propMapper.selectQrCodeList()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public PropResponse createProp(CreatePropRequest request) {
        PropEntity entity = buildFilledEntity(new PropEntity(), request);
        entity.setSupplierUserId(currentSupplierUserId());
        propMapper.insert(entity);
        replaceImages(entity.getId(), normalizeRequestImages(request));
        createAuditRecord(entity.getId(), "create", "新增道具待管理员确认");
        return getProp(entity.getId());
    }

    @Transactional
    public PropResponse createPendingFillProp() {
        LocalDateTime now = LocalDateTime.now();
        String qrCodeId = "QR-" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + (int) (Math.random() * 9000 + 1000);

        PropEntity entity = new PropEntity();
        entity.setSupplierUserId(null);
        entity.setPropName("待扫码录入道具");
        entity.setImageUrl(REAL_PROP_IMAGE_URL);
        entity.setPropStatus("offline");
        entity.setAuditStatus("pending");
        entity.setQrCodeId(qrCodeId);
        entity.setQrCodeUrl(qrCodeId);
        entity.setFillStatus("pending_fill");
        entity.setRemark("管理员预生成二维码，待工厂扫码录入后再绑定归属");
        propMapper.insertPendingFill(entity);
        replaceImages(entity.getId(), List.of(REAL_PROP_IMAGE_URL));
        return getProp(entity.getId());
    }

    @Transactional
    public PropResponse completePendingFill(Long propId, CreatePropRequest request) {
        PropEntity existing = requireProp(propId);
        if (!"pending_fill".equals(existing.getFillStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该道具不是待扫码录入状态");
        }
        if (existing.getSupplierUserId() != null) {
            throw new BizException(ResultCode.CONFLICT, "该道具已绑定工厂，不可重复录入");
        }

        PropEntity entity = buildFilledEntity(existing, request);
        entity.setId(existing.getId());
        entity.setSupplierUserId(currentSupplierUserId());
        entity.setQrCodeId(existing.getQrCodeId());
        entity.setQrCodeUrl(existing.getQrCodeUrl());
        entity.setRemark("工厂扫码录入并提交审核");
        propMapper.updatePendingFill(entity);
        replaceImages(entity.getId(), normalizeRequestImages(request));
        createAuditRecord(entity.getId(), "create", "工厂补全扫码入库资料，待管理员确认");
        return getProp(entity.getId());
    }

    @Transactional
    public PropResponse updatePropStatus(Long id, String status) {
        PropEntity prop = requireOwnedProp(id);
        if ("pending".equals(prop.getAuditStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前道具正在审核中，暂不可重复提交");
        }
        if ("locked".equals(prop.getPropStatus()) || "renting".equals(prop.getPropStatus())) {
            throw new BizException(ResultCode.CONFLICT, "租赁中或已锁定道具不可操作");
        }

        String actionType = "offline".equals(status) ? "down" : "up";
        String applyRemark = "down".equals(actionType) ? "工厂端提交了下架申请" : "工厂端提交了上架申请";

        propMapper.updateAuditResult(id, "pending", "offline");
        createAuditRecord(id, actionType, applyRemark);
        return getProp(id);
    }

    public PropResponse toResponse(PropEntity entity) {
        PropResponse response = new PropResponse();
        List<String> images = loadImages(entity);
        response.setId(entity.getId());
        response.setName(entity.getPropName());
        response.setImage(images.get(0));
        response.setImageUrl(images.get(0));
        response.setImages(images);
        response.setStyle(entity.getStyleCode());
        response.setType(entity.getTypeCode());
        response.setSize(entity.getSizeDesc());
        response.setLengthCm(entity.getLengthCm());
        response.setWidthCm(entity.getWidthCm());
        response.setHeightCm(entity.getHeightCm());
        response.setMaterial(entity.getMaterialDesc());
        response.setPrice(MoneyUtils.fenToYuan(entity.getDailyRentPriceFen()));
        response.setDeposit(MoneyUtils.fenToYuan(entity.getDepositAmountFen()));
        response.setFireResistantOption(entity.getFireResistantOption());
        response.setWeight(entity.getWeightDesc());
        response.setTransportSuggestion(entity.getTransportSuggestion());
        response.setStatus(entity.getPropStatus());
        response.setStatusText(resolveStatusText(entity));
        response.setAuditStatus(entity.getAuditStatus());
        response.setAuditStatusText(StatusTextHelper.auditStatusText(entity.getAuditStatus()));
        response.setQrCodeId(entity.getQrCodeId());
        response.setQrCodeUrl(entity.getQrCodeUrl());
        response.setFillStatus(entity.getFillStatus());
        return response;
    }

    private PropEntity buildFilledEntity(PropEntity entity, CreatePropRequest request) {
        entity.setPropName(request.getName().trim());
        entity.setImageUrl(normalizeRequestImages(request).get(0));
        entity.setStyleCode(requireOption(request.getStyle(), PropOptionConstants.STYLE_OPTIONS, "风格"));
        entity.setTypeCode(requireOption(request.getType(), PropOptionConstants.TYPE_OPTIONS, "类型"));
        entity.setSizeDesc(request.getSize());
        entity.setLengthCm(request.getLengthCm());
        entity.setWidthCm(request.getWidthCm());
        entity.setHeightCm(request.getHeightCm());
        entity.setMaterialDesc(request.getMaterial());
        entity.setDailyRentPriceFen(yuanToFen(request.getPrice()));
        entity.setDepositAmountFen(yuanToFen(request.getDeposit()));
        entity.setFireResistantOption(requireOption(request.getFireResistantOption(), PropOptionConstants.FIRE_RESISTANT_OPTIONS, "是否阻燃"));
        entity.setWeightDesc(request.getWeight().trim());
        entity.setTransportSuggestion(request.getTransportSuggestion().trim());
        entity.setPropStatus("offline");
        entity.setAuditStatus("pending");
        entity.setFillStatus("filled");
        return entity;
    }

    private PropEntity requireProp(Long propId) {
        PropEntity prop = propMapper.selectById(propId);
        if (prop == null) {
            throw new BizException(ResultCode.NOT_FOUND, "道具不存在");
        }
        return prop;
    }

    private PropEntity requireOwnedProp(Long propId) {
        PropEntity prop = requireProp(propId);
        Long currentSupplierUserId = currentSupplierUserId();
        if (prop.getSupplierUserId() == null || !currentSupplierUserId.equals(prop.getSupplierUserId())) {
            throw new BizException(ResultCode.FORBIDDEN, "当前工厂无权操作该道具");
        }
        return prop;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private List<String> loadImages(PropEntity entity) {
        List<String> images = propImageMapper.selectByPropId(entity.getId()).stream()
            .map(PropImageEntity::getImageUrl)
            .filter(item -> item != null && !item.isBlank())
            .toList();
        return images.isEmpty() ? parseImages(entity.getImageUrl()) : images;
    }

    private void replaceImages(Long propId, List<String> images) {
        propImageMapper.deleteByPropId(propId);
        if (images != null && !images.isEmpty()) {
            propImageMapper.batchInsert(propId, images);
        }
    }

    private List<String> normalizeRequestImages(CreatePropRequest request) {
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<String> images = request.getImages().stream()
                .filter(item -> item != null && !item.isBlank())
                .map(String::trim)
                .distinct()
                .limit(8)
                .toList();
            if (!images.isEmpty()) {
                return images;
            }
        }
        return parseImages(firstNonBlank(request.getImageUrl(), request.getImage(), REAL_PROP_IMAGE_URL));
    }

    private String requireOption(String value, List<String> options, String label) {
        if (value == null || value.isBlank()) {
            throw new BizException(ResultCode.VALIDATION_ERROR, label + "不能为空");
        }
        String trimmed = value.trim();
        if (!options.contains(trimmed)) {
            throw new BizException(ResultCode.VALIDATION_ERROR, label + "选项不合法");
        }
        return trimmed;
    }

    private int yuanToFen(BigDecimal yuan) {
        if (yuan == null) {
            return 0;
        }
        return yuan.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private void createAuditRecord(Long propId, String actionType, String applyRemark) {
        PropAuditEntity auditEntity = new PropAuditEntity();
        auditEntity.setPropId(propId);
        auditEntity.setActionType(actionType);
        auditEntity.setAuditStatus("pending");
        auditEntity.setApplyRemark(applyRemark);
        propAuditMapper.insert(auditEntity);
    }

    private Long currentSupplierUserId() {
        Long currentUserId = currentUserSupport.requireCurrentUserId();
        List<String> roleBindings = userRoleMapper.selectRoleCodesByUserId(currentUserId);
        if (!roleBindings.contains("supplier")) {
            throw new BizException(ResultCode.FORBIDDEN, "当前账号未绑定工厂身份，无法操作工厂道具数据");
        }
        return currentUserId;
    }

    private void requireAdminRole() {
        List<String> roleBindings = userRoleMapper.selectRoleCodesByUserId(currentUserSupport.requireCurrentUserId());
        if (!roleBindings.contains("admin")) {
            throw new BizException(ResultCode.FORBIDDEN, "当前账号不是管理员，无法查看二维码列表");
        }
    }

    private String resolveStatusText(PropEntity entity) {
        if ("pending".equals(entity.getAuditStatus())) {
            return "待审核";
        }
        if ("rejected".equals(entity.getAuditStatus())) {
            return "已驳回";
        }
        return StatusTextHelper.propStatusText(entity.getPropStatus());
    }

    private List<String> parseImages(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return List.of(REAL_PROP_IMAGE_URL);
        }
        return Arrays.stream(imageUrl.split("[,;\\n]"))
            .map(String::trim)
            .filter(item -> !item.isBlank())
            .distinct()
            .toList();
    }
}
