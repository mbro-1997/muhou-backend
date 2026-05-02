package com.muhou.backend.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectSchemeItemMapper {

    @Select("SELECT prop_id FROM project_scheme_item WHERE project_id = #{projectId} ORDER BY id ASC")
    List<Long> selectPropIdsByProjectId(@Param("projectId") Long projectId);

    @Insert("INSERT IGNORE INTO project_scheme_item (project_id, prop_id, created_at) VALUES (#{projectId}, #{propId}, NOW())")
    int insertIgnore(@Param("projectId") Long projectId, @Param("propId") Long propId);

    @Delete("DELETE FROM project_scheme_item WHERE project_id = #{projectId} AND prop_id = #{propId}")
    int deleteByProjectIdAndPropId(@Param("projectId") Long projectId, @Param("propId") Long propId);
}
