package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.constants.PropOptionConstants;
import com.muhou.backend.common.exception.BizException;
import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.common.support.StatusTextHelper;
import com.muhou.backend.common.util.MoneyUtils;
import com.muhou.backend.common.util.TimeUtils;
import com.muhou.backend.infrastructure.client.WechatMiniappCodeGateway;
import com.muhou.backend.infrastructure.persistence.entity.PropAuditEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropImageEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropInstanceEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropInstanceStatusLogEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropQrCodeEntity;
import com.muhou.backend.infrastructure.persistence.mapper.PropAuditMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropImageMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropInstanceMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropInstanceStatusLogMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropQrCodeMapper;
import com.muhou.backend.infrastructure.persistence.mapper.RentalOrderMapper;
import com.muhou.backend.infrastructure.persistence.mapper.UserRoleMapper;
import com.muhou.backend.web.request.CreatePropRequest;
import com.muhou.backend.web.response.PropInstanceResponse;
import com.muhou.backend.web.response.PropInstanceStatusLogResponse;
import com.muhou.backend.web.response.PropResponse;
import com.muhou.backend.web.response.QrCodeResolveResponse;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PropApplicationService {

    private static final String REAL_PROP_IMAGE_URL = "/images/stage-prop-real.jpg";

    private final PropMapper propMapper;
    private final PropAuditMapper propAuditMapper;
    private final PropImageMapper propImageMapper;
    private final PropInstanceMapper propInstanceMapper;
    private final PropInstanceStatusLogMapper propInstanceStatusLogMapper;
    private final PropQrCodeMapper propQrCodeMapper;
    private final RentalOrderMapper rentalOrderMapper;
    private final WechatMiniappCodeGateway wechatMiniappCodeGateway;
    private final QrCodeArchiveService qrCodeArchiveService;
    private final CurrentUserSupport currentUserSupport;
    private final UserRoleMapper userRoleMapper;

    public PropApplicationService(PropMapper propMapper,
                                  PropAuditMapper propAuditMapper,
                                  PropImageMapper propImageMapper,
                                  PropInstanceMapper propInstanceMapper,
                                  PropInstanceStatusLogMapper propInstanceStatusLogMapper,
                                  PropQrCodeMapper propQrCodeMapper,
                                  RentalOrderMapper rentalOrderMapper,
                                  WechatMiniappCodeGateway wechatMiniappCodeGateway,
                                  QrCodeArchiveService qrCodeArchiveService,
                                  CurrentUserSupport currentUserSupport,
                                  UserRoleMapper userRoleMapper) {
        this.propMapper = propMapper;
        this.propAuditMapper = propAuditMapper;
        this.propImageMapper = propImageMapper;
        this.propInstanceMapper = propInstanceMapper;
        this.propInstanceStatusLogMapper = propInstanceStatusLogMapper;
        this.propQrCodeMapper = propQrCodeMapper;
        this.rentalOrderMapper = rentalOrderMapper;
        this.wechatMiniappCodeGateway = wechatMiniappCodeGateway;
        this.qrCodeArchiveService = qrCodeArchiveService;
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
        return List.of();
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
        Long adminUserId = currentUserSupport.requireCurrentUserId();
        requireAdminRole();
        LocalDateTime now = LocalDateTime.now();
        String qrCodeId = "QR-" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + UUID.randomUUID().toString().substring(0, 8);
        String qrPage = "pages/qr-entry/index";
        String qrScene = "q=" + qrCodeId;

        PropEntity entity = new PropEntity();
        entity.setSupplierUserId(null);
        entity.setPropName("待扫码录入道具");
        entity.setImageUrl(REAL_PROP_IMAGE_URL);
        entity.setPropStatus("offline");
        entity.setAuditStatus("pending");
        entity.setQrCodeId(qrCodeId);
        entity.setQrCodeUrl("/api/qrcodes/" + qrCodeId + "/image");
        entity.setFillStatus("pending_fill");
        entity.setRemark("管理员预生成小程序码，待工厂扫码录入后再绑定归属");
        propMapper.insertPendingFill(entity);
        replaceImages(entity.getId(), List.of(REAL_PROP_IMAGE_URL));

        byte[] imageBytes = wechatMiniappCodeGateway.generateUnlimited(qrScene, qrPage);
        QrCodeArchiveService.QrCodeArchiveResult archiveResult = qrCodeArchiveService.save(qrCodeId, imageBytes);
        propMapper.updateQrCodeUrl(entity.getId(), archiveResult.imageUrl());

        PropQrCodeEntity qrCode = new PropQrCodeEntity();
        qrCode.setPropId(entity.getId());
        qrCode.setQrCodeId(qrCodeId);
        qrCode.setQrScene(qrScene);
        qrCode.setQrPage(qrPage);
        qrCode.setQrImageUrl(archiveResult.imageUrl());
        qrCode.setQrImageStorageKey(archiveResult.storageKey());
        qrCode.setImageSha256(archiveResult.sha256());
        qrCode.setStatus("unused");
        qrCode.setCreatedByAdminUserId(adminUserId);
        propQrCodeMapper.insert(qrCode);
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
        String requestQrCodeId = request.getQrCodeId() == null ? "" : request.getQrCodeId().trim();
        if (requestQrCodeId.isBlank()) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "请通过扫码入口录入道具");
        }
        if (!requestQrCodeId.equals(existing.getQrCodeId())) {
            throw new BizException(ResultCode.FORBIDDEN, "扫码二维码与当前录入道具不匹配，不能提交");
        }
        PropQrCodeEntity qrCode = propQrCodeMapper.selectByQrCodeId(requestQrCodeId);
        if (qrCode == null || "revoked".equals(qrCode.getStatus())) {
            throw new BizException(ResultCode.NOT_FOUND, "二维码不存在或已作废");
        }
        if (!propId.equals(qrCode.getPropId())) {
            throw new BizException(ResultCode.FORBIDDEN, "二维码绑定的道具与当前表单不一致");
        }
        if (!"unused".equals(qrCode.getStatus())) {
            throw new BizException(ResultCode.CONFLICT, "该二维码已登记或已作废，不能重复录入");
        }

        Long supplierUserId = currentSupplierUserId();
        PropEntity entity = buildFilledEntity(existing, request);
        entity.setId(existing.getId());
        entity.setSupplierUserId(supplierUserId);
        entity.setQrCodeId(existing.getQrCodeId());
        entity.setQrCodeUrl(existing.getQrCodeUrl());
        entity.setRemark("工厂扫码录入并提交审核");
        propMapper.updatePendingFill(entity);
        propQrCodeMapper.markFilled(entity.getId(), supplierUserId);
        PropInstanceEntity instance = new PropInstanceEntity();
        instance.setPropId(entity.getId());
        instance.setQrCodeId(entity.getQrCodeId());
        instance.setInstanceNo("SN-" + entity.getId() + "-1");
        instance.setInstanceStatus("idle");
        instance.setCurrentOrderId(null);
        instance.setRemark("factory scan-in bound physical instance");
        instance.setStatusChangedAt(LocalDateTime.now());
        instance.setStatusChangedBy(supplierUserId);
        propInstanceMapper.insert(instance);
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
        if (propInstanceMapper.countActiveByPropId(id) > 0) {
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
        response.setQrStatus(resolveQrStatus(entity));
        response.setFillStatus(entity.getFillStatus());
        response.setSupplierUserId(entity.getSupplierUserId());
        response.setSupplierName(entity.getSupplierUserId() == null ? "未绑定工厂" : "工厂 " + entity.getSupplierUserId());
        int assetTotalStock = propInstanceMapper.countTotalByPropId(entity.getId());
        int totalStock = propInstanceMapper.countOperatingByPropId(entity.getId());
        int availableStock = propInstanceMapper.countByPropIdAndStatus(entity.getId(), "idle");
        int lockedStock = propInstanceMapper.countByPropIdAndStatus(entity.getId(), "locked");
        int rentedStock = propInstanceMapper.countByPropIdAndStatus(entity.getId(), "renting");
        int repairingStock = propInstanceMapper.countByPropIdAndStatus(entity.getId(), "repairing");
        int lostStock = propInstanceMapper.countByPropIdAndStatus(entity.getId(), "lost");
        int scrappedStock = propInstanceMapper.countByPropIdAndStatus(entity.getId(), "scrapped");
        response.setAssetTotalStock(assetTotalStock);
        response.setTotalStock(totalStock);
        response.setAvailableStock(availableStock);
        response.setLockedStock(lockedStock);
        response.setRentedStock(rentedStock);
        response.setRepairingStock(repairingStock);
        response.setLostStock(lostStock);
        response.setScrappedStock(scrappedStock);
        response.setCanRent("approved".equals(entity.getAuditStatus())
            && "filled".equals(entity.getFillStatus())
            && "idle".equals(entity.getPropStatus())
            && availableStock > 0);
        return response;
    }

    public List<PropInstanceResponse> listFactoryPropInstances(Long propId) {
        requireOwnedProp(propId);
        return propInstanceMapper.selectByPropId(propId).stream()
            .map(item -> toInstanceResponse(item, false))
            .toList();
    }

    public List<PropInstanceResponse> listAdminPropInstances(Long propId) {
        requireAdminRole();
        return propInstanceMapper.selectByPropId(propId).stream()
            .map(item -> toInstanceResponse(item, true))
            .toList();
    }

    @Transactional
    public PropInstanceResponse updateFactoryInstanceStatus(Long instanceId, String targetStatus, String reason) {
        Long supplierUserId = currentSupplierUserId();
        PropInstanceEntity instance = requireInstance(instanceId);
        PropEntity prop = requireProp(instance.getPropId());
        if (prop.getSupplierUserId() == null || !supplierUserId.equals(prop.getSupplierUserId())) {
            throw new BizException(ResultCode.FORBIDDEN, "Current factory cannot operate this SN");
        }
        updateInstanceStatus(instance, normalizeTargetStatus(targetStatus), reason, null, "supplier", supplierUserId);
        return toInstanceResponse(propInstanceMapper.selectById(instanceId), false);
    }

    @Transactional
    public PropInstanceResponse updateAdminInstanceStatus(Long instanceId, String targetStatus, String reason, Long relatedOrderId) {
        requireAdminRole();
        Long adminUserId = currentUserSupport.requireCurrentUserId();
        PropInstanceEntity instance = requireInstance(instanceId);
        updateInstanceStatus(instance, normalizeTargetStatus(targetStatus), reason, relatedOrderId, "admin", adminUserId);
        return toInstanceResponse(propInstanceMapper.selectById(instanceId), true);
    }

    public List<PropInstanceStatusLogResponse> listFactoryInstanceStatusLogs(Long instanceId) {
        PropInstanceEntity instance = requireInstance(instanceId);
        requireOwnedProp(instance.getPropId());
        return propInstanceStatusLogMapper.selectByInstanceId(instanceId).stream()
            .map(this::toStatusLogResponse)
            .toList();
    }

    public List<PropInstanceStatusLogResponse> listAdminInstanceStatusLogs(Long instanceId) {
        requireAdminRole();
        requireInstance(instanceId);
        return propInstanceStatusLogMapper.selectByInstanceId(instanceId).stream()
            .map(this::toStatusLogResponse)
            .toList();
    }

    public QrCodeResolveResponse resolveQrCode(String qrCodeId) {
        if (qrCodeId == null || qrCodeId.isBlank()) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "二维码参数不能为空");
        }
        PropQrCodeEntity qrCode = propQrCodeMapper.selectByQrCodeId(qrCodeId.trim());
        if (qrCode == null || "revoked".equals(qrCode.getStatus())) {
            return buildQrMessage(qrCodeId, "revoked", null, "message", "二维码不存在或已作废");
        }
        PropEntity prop = propMapper.selectById(qrCode.getPropId());
        if (prop == null) {
            return buildQrMessage(qrCodeId, qrCode.getStatus(), null, "message", "二维码对应的道具不存在");
        }

        QrCodeResolveResponse response = buildQrMessage(qrCodeId, qrCode.getStatus(), prop, "message", "请按当前身份继续操作");
        if ("filled".equals(prop.getFillStatus()) && "approved".equals(prop.getAuditStatus()) && !"offline".equals(prop.getPropStatus())) {
            response.setRedirectType("prop_detail");
            response.setMessage("道具已登记，可查看道具详情");
            return response;
        }

        if (currentUserSupport.getCurrentSession() == null) {
            response.setRedirectType("login_required");
            response.setMessage("请先登录后继续扫码流程");
            return response;
        }

        Long currentUserId = currentUserSupport.requireCurrentUserId();
        List<String> roleBindings = userRoleMapper.selectRoleCodesByUserId(currentUserId);
        if ("pending_fill".equals(prop.getFillStatus())) {
            if (roleBindings.contains("supplier")) {
                response.setRedirectType("inventory_form");
                response.setMessage("请补全道具入库资料并提交审核");
                return response;
            }
            response.setRedirectType("message");
            response.setMessage("该道具尚未录入，当前账号不能填报道具资料");
            return response;
        }

        response.setRedirectType("message");
        response.setMessage("该道具当前不可通过二维码处理");
        return response;
    }

    public Resource getQrCodeImage(String qrCodeId) {
        PropQrCodeEntity qrCode = propQrCodeMapper.selectByQrCodeId(qrCodeId);
        if (qrCode == null || "revoked".equals(qrCode.getStatus())) {
            throw new BizException(ResultCode.NOT_FOUND, "二维码不存在或已作废");
        }
        Resource resource = qrCodeArchiveService.loadRequired(qrCode.getQrImageStorageKey());
        propQrCodeMapper.markDownloaded(qrCodeId);
        return resource;
    }

    @Transactional
    public List<PropResponse> revokePendingQrCode(Long propId) {
        requireAdminRole();
        PropEntity prop = requireProp(propId);
        if (!"pending_fill".equals(prop.getFillStatus())) {
            throw new BizException(ResultCode.CONFLICT, "只有未登记二维码可以作废");
        }
        int updated = propQrCodeMapper.revokeByPropId(propId, currentUserSupport.requireCurrentUserId());
        if (updated == 0) {
            throw new BizException(ResultCode.CONFLICT, "二维码已登记或已作废，不能重复作废");
        }
        propMapper.revokePendingFill(propId, "管理员已作废该二维码");
        return listAdminQrCodeProps();
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
                assertPersistedImages(images);
                return images;
            }
        }
        List<String> images = parseImages(firstNonBlank(request.getImageUrl(), request.getImage(), REAL_PROP_IMAGE_URL));
        assertPersistedImages(images);
        return images;
    }

    private void assertPersistedImages(List<String> images) {
        for (String image : images) {
            String lower = image == null ? "" : image.trim().toLowerCase();
            if (lower.startsWith("wxfile://")
                || lower.startsWith("file://")
                || lower.startsWith("blob:")
                || lower.startsWith("http://tmp")
                || lower.startsWith("https://tmp")
                || lower.contains("tempfilepath")
                || lower.contains("/tmp/")) {
                throw new BizException(ResultCode.VALIDATION_ERROR, "Image must be uploaded before submitting business data");
            }
        }
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

    private PropInstanceEntity requireInstance(Long instanceId) {
        PropInstanceEntity instance = propInstanceMapper.selectById(instanceId);
        if (instance == null) {
            throw new BizException(ResultCode.NOT_FOUND, "SN does not exist");
        }
        return instance;
    }

    private String normalizeTargetStatus(String targetStatus) {
        String normalized = targetStatus == null ? "" : targetStatus.trim().toLowerCase();
        if (List.of("idle", "repairing", "lost", "scrapped").contains(normalized)) {
            return normalized;
        }
        throw new BizException(ResultCode.VALIDATION_ERROR, "Unsupported SN target status");
    }

    private void updateInstanceStatus(PropInstanceEntity instance,
                                      String targetStatus,
                                      String reason,
                                      Long relatedOrderId,
                                      String operatorRole,
                                      Long operatorUserId) {
        String trimmedReason = reason == null ? "" : reason.trim();
        if (trimmedReason.isBlank()) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "Reason is required");
        }
        validateInstanceTransition(instance.getInstanceStatus(), targetStatus, operatorRole);
        int updated = propInstanceMapper.updateStatus(
            instance.getId(),
            instance.getInstanceStatus(),
            targetStatus,
            trimmedReason,
            operatorUserId
        );
        if (updated <= 0) {
            throw new BizException(ResultCode.CONFLICT, "SN status changed, please refresh and retry");
        }
        PropInstanceStatusLogEntity log = new PropInstanceStatusLogEntity();
        log.setPropInstanceId(instance.getId());
        log.setPropId(instance.getPropId());
        log.setInstanceNo(instance.getInstanceNo());
        log.setQrCodeId(instance.getQrCodeId());
        log.setFromStatus(instance.getInstanceStatus());
        log.setToStatus(targetStatus);
        log.setReason(trimmedReason);
        log.setOperatorUserId(operatorUserId);
        log.setOperatorRole(operatorRole);
        log.setRelatedOrderId(relatedOrderId);
        propInstanceStatusLogMapper.insert(log);
    }

    private void validateInstanceTransition(String fromStatus, String targetStatus, String operatorRole) {
        if ("supplier".equals(operatorRole)) {
            if ("idle".equals(fromStatus) && List.of("repairing", "lost", "scrapped").contains(targetStatus)) {
                return;
            }
            if ("repairing".equals(fromStatus) && List.of("idle", "scrapped").contains(targetStatus)) {
                return;
            }
            throw new BizException(ResultCode.FORBIDDEN, "Factory cannot perform this SN status transition");
        }
        if ("admin".equals(operatorRole)) {
            if ("lost".equals(fromStatus) && List.of("idle", "repairing").contains(targetStatus)) {
                return;
            }
            if ("scrapped".equals(fromStatus) && List.of("idle", "repairing").contains(targetStatus)) {
                return;
            }
            if ("idle".equals(fromStatus) && List.of("repairing", "lost", "scrapped").contains(targetStatus)) {
                return;
            }
            if ("repairing".equals(fromStatus) && List.of("idle", "scrapped").contains(targetStatus)) {
                return;
            }
            throw new BizException(ResultCode.FORBIDDEN, "Admin cannot perform this SN status transition");
        }
        throw new BizException(ResultCode.FORBIDDEN, "Unsupported operator role");
    }

    private PropInstanceResponse toInstanceResponse(PropInstanceEntity entity, boolean adminMode) {
        PropInstanceResponse response = new PropInstanceResponse();
        response.setId(entity.getId());
        response.setPropId(entity.getPropId());
        response.setInstanceNo(entity.getInstanceNo());
        response.setQrCodeId(entity.getQrCodeId());
        response.setInstanceStatus(entity.getInstanceStatus());
        response.setInstanceStatusText(instanceStatusText(entity.getInstanceStatus()));
        response.setCurrentOrderId(entity.getCurrentOrderId());
        if (entity.getCurrentOrderId() != null) {
            var order = rentalOrderMapper.selectById(entity.getCurrentOrderId());
            response.setCurrentOrderNo(order == null ? "" : order.getOrderNo());
        }
        response.setStatusRemark(entity.getStatusRemark());
        response.setStatusChangedAt(TimeUtils.format(entity.getStatusChangedAt()));
        response.setStatusChangedBy(entity.getStatusChangedBy());
        response.setCanMarkRepairing("idle".equals(entity.getInstanceStatus()) || (adminMode && "lost".equals(entity.getInstanceStatus())) || (adminMode && "scrapped".equals(entity.getInstanceStatus())));
        response.setCanMarkLost("idle".equals(entity.getInstanceStatus()));
        response.setCanMarkScrapped("idle".equals(entity.getInstanceStatus()) || "repairing".equals(entity.getInstanceStatus()));
        response.setCanRestoreIdle("repairing".equals(entity.getInstanceStatus()) || (adminMode && "lost".equals(entity.getInstanceStatus())) || (adminMode && "scrapped".equals(entity.getInstanceStatus())));
        response.setCanScrapFromRepairing("repairing".equals(entity.getInstanceStatus()));
        if (!adminMode && ("lost".equals(entity.getInstanceStatus()) || "scrapped".equals(entity.getInstanceStatus()))) {
            response.setCanRestoreIdle(false);
        }
        return response;
    }

    private PropInstanceStatusLogResponse toStatusLogResponse(PropInstanceStatusLogEntity entity) {
        PropInstanceStatusLogResponse response = new PropInstanceStatusLogResponse();
        response.setId(entity.getId());
        response.setPropInstanceId(entity.getPropInstanceId());
        response.setPropId(entity.getPropId());
        response.setInstanceNo(entity.getInstanceNo());
        response.setQrCodeId(entity.getQrCodeId());
        response.setFromStatus(entity.getFromStatus());
        response.setFromStatusText(instanceStatusText(entity.getFromStatus()));
        response.setToStatus(entity.getToStatus());
        response.setToStatusText(instanceStatusText(entity.getToStatus()));
        response.setReason(entity.getReason());
        response.setOperatorUserId(entity.getOperatorUserId());
        response.setOperatorRole(entity.getOperatorRole());
        response.setRelatedOrderId(entity.getRelatedOrderId());
        response.setCreatedAt(TimeUtils.format(entity.getCreatedAt()));
        return response;
    }

    private String instanceStatusText(String status) {
        return switch (status == null ? "" : status) {
            case "idle" -> "空闲";
            case "locked" -> "已锁定";
            case "renting" -> "租赁中";
            case "repairing" -> "维修中";
            case "lost" -> "丢失";
            case "scrapped" -> "报废";
            default -> status == null ? "未知" : status;
        };
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

    private String resolveQrStatus(PropEntity entity) {
        if (entity.getQrCodeId() == null || entity.getQrCodeId().isBlank()) {
            return null;
        }
        PropQrCodeEntity qrCode = propQrCodeMapper.selectByQrCodeId(entity.getQrCodeId());
        if (qrCode != null) {
            return qrCode.getStatus();
        }
        if ("pending_fill".equals(entity.getFillStatus())) {
            return "unused";
        }
        if ("revoked".equals(entity.getFillStatus())) {
            return "revoked";
        }
        if ("filled".equals(entity.getFillStatus())) {
            return "filled";
        }
        return null;
    }

    private QrCodeResolveResponse buildQrMessage(String qrCodeId,
                                                String qrStatus,
                                                PropEntity prop,
                                                String redirectType,
                                                String message) {
        QrCodeResolveResponse response = new QrCodeResolveResponse();
        response.setQrCodeId(qrCodeId);
        response.setQrStatus(qrStatus);
        response.setRedirectType(redirectType);
        response.setMessage(message);
        if (prop != null) {
            response.setPropId(prop.getId());
            response.setPropName(prop.getPropName());
            response.setFillStatus(prop.getFillStatus());
            response.setAuditStatus(prop.getAuditStatus());
            response.setPropStatus(prop.getPropStatus());
        }
        return response;
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
