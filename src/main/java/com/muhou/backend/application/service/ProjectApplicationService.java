package com.muhou.backend.application.service;

import com.muhou.backend.common.api.ResultCode;
import com.muhou.backend.common.exception.BizException;
import com.muhou.backend.common.support.CurrentUserSupport;
import com.muhou.backend.common.support.StatusTextHelper;
import com.muhou.backend.common.util.TimeUtils;
import com.muhou.backend.infrastructure.persistence.entity.ProjectSchemeEntity;
import com.muhou.backend.infrastructure.persistence.entity.ProjectSchemeItemEntity;
import com.muhou.backend.infrastructure.persistence.entity.PropEntity;
import com.muhou.backend.infrastructure.persistence.mapper.ProjectSchemeItemMapper;
import com.muhou.backend.infrastructure.persistence.mapper.ProjectSchemeMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropInstanceMapper;
import com.muhou.backend.infrastructure.persistence.mapper.PropMapper;
import com.muhou.backend.web.request.ProjectUpdateRequest;
import com.muhou.backend.web.response.ProjectResponse;
import com.muhou.backend.web.response.PropResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectApplicationService {

    private final ProjectSchemeMapper projectSchemeMapper;
    private final ProjectSchemeItemMapper projectSchemeItemMapper;
    private final PropMapper propMapper;
    private final PropInstanceMapper propInstanceMapper;
    private final PropApplicationService propApplicationService;
    private final CurrentUserSupport currentUserSupport;

    public ProjectApplicationService(ProjectSchemeMapper projectSchemeMapper,
                                     ProjectSchemeItemMapper projectSchemeItemMapper,
                                     PropMapper propMapper,
                                     PropInstanceMapper propInstanceMapper,
                                     PropApplicationService propApplicationService,
                                     CurrentUserSupport currentUserSupport) {
        this.projectSchemeMapper = projectSchemeMapper;
        this.projectSchemeItemMapper = projectSchemeItemMapper;
        this.propMapper = propMapper;
        this.propInstanceMapper = propInstanceMapper;
        this.propApplicationService = propApplicationService;
        this.currentUserSupport = currentUserSupport;
    }

    public List<ProjectResponse> listProjects() {
        return projectSchemeMapper.selectByUserId(currentDemanderUserId())
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public ProjectResponse getProject(Long projectId) {
        return toResponse(getOwnedProject(projectId));
    }

    @Transactional
    public ProjectResponse createProject(ProjectUpdateRequest request) {
        ProjectSchemeEntity created = new ProjectSchemeEntity();
        created.setUserId(currentDemanderUserId());
        created.setProjectName(request.getName());
        created.setProjectDescription(request.getDescription());
        created.setProjectStatus("editing");
        projectSchemeMapper.insert(created);
        return getProject(created.getId());
    }

    @Transactional
    public ProjectResponse addPropToEditingProject(Long propId) {
        return addPropToEditingProject(propId, 1);
    }

    @Transactional
    public ProjectResponse addPropToEditingProject(Long propId, Integer quantity) {
        PropEntity prop = requireProp(propId);
        ProjectSchemeEntity editingProject = ensureEditingProject();
        int safeQuantity = normalizeQuantity(quantity);
        ensureAvailableStock(prop, currentProjectQuantity(editingProject.getId(), prop.getId()) + safeQuantity);
        projectSchemeItemMapper.insertOrIncrease(editingProject.getId(), prop.getId(), safeQuantity);
        return getProject(editingProject.getId());
    }

    @Transactional
    public ProjectResponse addPropToProject(Long projectId, Long propId) {
        return addPropToProject(projectId, propId, 1);
    }

    @Transactional
    public ProjectResponse addPropToProject(Long projectId, Long propId, Integer quantity) {
        ProjectSchemeEntity project = getOwnedProject(projectId);
        ensureEditable(project);
        PropEntity prop = requireProp(propId);
        int safeQuantity = normalizeQuantity(quantity);
        ensureAvailableStock(prop, currentProjectQuantity(project.getId(), prop.getId()) + safeQuantity);
        projectSchemeItemMapper.insertOrIncrease(project.getId(), prop.getId(), safeQuantity);
        return getProject(project.getId());
    }

    @Transactional
    public ProjectResponse removePropFromProject(Long projectId, Long propId) {
        ProjectSchemeEntity project = getOwnedProject(projectId);
        ensureEditable(project);
        projectSchemeItemMapper.deleteByProjectIdAndPropId(projectId, propId);
        return getProject(projectId);
    }

    @Transactional
    public ProjectResponse updatePropQuantity(Long projectId, Long propId, Integer quantity) {
        ProjectSchemeEntity project = getOwnedProject(projectId);
        ensureEditable(project);
        PropEntity prop = requireProp(propId);
        int safeQuantity = normalizeQuantity(quantity);
        ensureAvailableStock(prop, safeQuantity);
        projectSchemeItemMapper.updateQuantity(projectId, propId, safeQuantity);
        return getProject(projectId);
    }

    @Transactional
    public ProjectResponse updateProject(Long projectId, ProjectUpdateRequest request) {
        ProjectSchemeEntity entity = getOwnedProject(projectId);
        ensureEditable(entity);
        entity.setProjectName(request.getName());
        entity.setProjectDescription(request.getDescription());
        projectSchemeMapper.updateBasic(entity);
        return getProject(projectId);
    }

    @Transactional
    public void markProjectOrdered(Long projectId) {
        projectSchemeMapper.markOrdered(projectId, LocalDateTime.now());
    }

    public ProjectSchemeEntity getOwnedProject(Long projectId) {
        ProjectSchemeEntity entity = projectSchemeMapper.selectById(projectId);
        if (entity == null || !Objects.equals(entity.getUserId(), currentDemanderUserId())) {
            throw new BizException(ResultCode.NOT_FOUND, "方案不存在");
        }
        return entity;
    }

    private ProjectSchemeEntity ensureEditingProject() {
        Long userId = currentDemanderUserId();
        ProjectSchemeEntity found = projectSchemeMapper.selectEditingByUserId(userId);
        if (found != null) {
            return found;
        }

        ProjectSchemeEntity created = new ProjectSchemeEntity();
        created.setUserId(userId);
        created.setProjectName("方案" + System.currentTimeMillis());
        created.setProjectDescription("");
        created.setProjectStatus("editing");
        projectSchemeMapper.insert(created);
        return created;
    }

    private PropEntity requireProp(Long propId) {
        PropEntity prop = propMapper.selectById(propId);
        if (prop == null) {
            throw new BizException(ResultCode.NOT_FOUND, "道具不存在");
        }
        if (!"filled".equals(prop.getFillStatus())
            || !"approved".equals(prop.getAuditStatus())
            || !"idle".equals(prop.getPropStatus())) {
            throw new BizException(ResultCode.NOT_FOUND, "道具不存在");
        }
        return prop;
    }

    private int normalizeQuantity(Integer quantity) {
        if (quantity == null) {
            return 1;
        }
        if (quantity < 1) {
            throw new BizException(ResultCode.VALIDATION_ERROR, "quantity must be greater than 0");
        }
        return quantity;
    }

    private int currentProjectQuantity(Long projectId, Long propId) {
        return projectSchemeItemMapper.selectByProjectId(projectId).stream()
            .filter(item -> Objects.equals(item.getPropId(), propId))
            .map(ProjectSchemeItemEntity::getQuantity)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(0);
    }

    private void ensureAvailableStock(PropEntity prop, int quantity) {
        int availableStock = propInstanceMapper.countByPropIdAndStatus(prop.getId(), "idle");
        if (availableStock < quantity) {
            throw new BizException(ResultCode.CONFLICT, "道具库存不足");
        }
    }

    private void ensureEditable(ProjectSchemeEntity project) {
        if (!"editing".equals(project.getProjectStatus())) {
            throw new BizException(ResultCode.CONFLICT, "当前方案已下单，不能继续编辑");
        }
    }

    private ProjectResponse toResponse(ProjectSchemeEntity entity) {
        List<ProjectSchemeItemEntity> schemeItems = projectSchemeItemMapper.selectByProjectId(entity.getId());
        List<Long> propIds = schemeItems.stream().map(ProjectSchemeItemEntity::getPropId).toList();
        List<PropEntity> propEntities =
            propIds.isEmpty() ? Collections.emptyList() : propMapper.selectByIds(propIds);
        List<PropResponse> props =
            propEntities.stream().map(prop -> {
                PropResponse response = propApplicationService.toResponse(prop);
                schemeItems.stream()
                    .filter(item -> item.getPropId().equals(prop.getId()))
                    .findFirst()
                    .ifPresent(item -> response.setQuantity(item.getQuantity()));
                return response;
            }).collect(Collectors.toList());
        int unavailableCount = (int) props.stream()
            .filter(item -> !item.isCanRent()
                || item.getQuantity() == null
                || item.getAvailableStock() == null
                || item.getQuantity() > item.getAvailableStock())
            .count();

        ProjectResponse response = new ProjectResponse();
        response.setId(entity.getId());
        response.setName(entity.getProjectName());
        response.setDescription(entity.getProjectDescription());
        response.setStatus(entity.getProjectStatus());
        response.setStatusText(StatusTextHelper.projectStatusText(entity.getProjectStatus()));
        response.setUnavailableCount(unavailableCount);
        response.setCanSubmit("editing".equals(entity.getProjectStatus()) && !props.isEmpty() && unavailableCount == 0);
        response.setCreatedAt(TimeUtils.toEpochMilli(entity.getCreatedAt()));
        response.setUpdatedAt(TimeUtils.toEpochMilli(entity.getUpdatedAt()));
        response.setPropIds(propIds);
        response.setProps(props);
        return response;
    }

    private Long currentDemanderUserId() {
        if (!"demander".equals(currentUserSupport.getCurrentRole())) {
            throw new BizException(ResultCode.FORBIDDEN, "当前角色无权访问方案接口");
        }
        return currentUserSupport.requireCurrentUserId();
    }
}
