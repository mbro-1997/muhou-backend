package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.ProjectSchemeEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProjectSchemeMapper {

    @Select("SELECT * FROM project_scheme WHERE user_id = #{userId} ORDER BY updated_at DESC, id DESC")
    List<ProjectSchemeEntity> selectByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM project_scheme WHERE id = #{id}")
    ProjectSchemeEntity selectById(@Param("id") Long id);

    @Select("SELECT * FROM project_scheme WHERE user_id = #{userId} AND project_status = 'editing' ORDER BY updated_at DESC, id DESC LIMIT 1")
    ProjectSchemeEntity selectEditingByUserId(@Param("userId") Long userId);

    @Insert("INSERT INTO project_scheme (user_id, project_name, project_description, project_status, created_at, updated_at) " +
        "VALUES (#{userId}, #{projectName}, #{projectDescription}, #{projectStatus}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ProjectSchemeEntity entity);

    @Update("UPDATE project_scheme SET project_name = #{projectName}, project_description = #{projectDescription}, updated_at = NOW() WHERE id = #{id}")
    int updateBasic(ProjectSchemeEntity entity);

    @Update("UPDATE project_scheme SET project_status = 'ordered', ordered_at = #{orderedAt}, updated_at = NOW() WHERE id = #{id}")
    int markOrdered(@Param("id") Long id, @Param("orderedAt") LocalDateTime orderedAt);
}
