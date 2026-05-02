package com.muhou.backend.web.controller;

import com.muhou.backend.application.service.ProjectApplicationService;
import com.muhou.backend.common.api.ApiResponse;
import com.muhou.backend.web.request.AddProjectItemRequest;
import com.muhou.backend.web.request.ProjectUpdateRequest;
import com.muhou.backend.web.response.ProjectResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectApplicationService projectApplicationService;

    public ProjectController(ProjectApplicationService projectApplicationService) {
        this.projectApplicationService = projectApplicationService;
    }

    @GetMapping
    public ApiResponse<List<ProjectResponse>> list() {
        return ApiResponse.success(projectApplicationService.listProjects());
    }

    @GetMapping("/{projectId}")
    public ApiResponse<ProjectResponse> detail(@PathVariable Long projectId) {
        return ApiResponse.success(projectApplicationService.getProject(projectId));
    }

    @PostMapping
    public ApiResponse<ProjectResponse> create(@Valid @RequestBody ProjectUpdateRequest request) {
        return ApiResponse.success(projectApplicationService.createProject(request));
    }

    @PostMapping("/editing/items")
    public ApiResponse<ProjectResponse> addToEditing(@Valid @RequestBody AddProjectItemRequest request) {
        return ApiResponse.success(projectApplicationService.addPropToEditingProject(request.getPropId()));
    }

    @PostMapping("/{projectId}/items")
    public ApiResponse<ProjectResponse> addToProject(@PathVariable Long projectId, @Valid @RequestBody AddProjectItemRequest request) {
        return ApiResponse.success(projectApplicationService.addPropToProject(projectId, request.getPropId()));
    }

    @DeleteMapping("/{projectId}/items/{propId}")
    public ApiResponse<ProjectResponse> removeItem(@PathVariable Long projectId, @PathVariable Long propId) {
        return ApiResponse.success(projectApplicationService.removePropFromProject(projectId, propId));
    }

    @PutMapping("/{projectId}")
    public ApiResponse<ProjectResponse> update(@PathVariable Long projectId, @Valid @RequestBody ProjectUpdateRequest request) {
        return ApiResponse.success(projectApplicationService.updateProject(projectId, request));
    }
}