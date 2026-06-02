package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.ProjectSchemeItemEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProjectSchemeItemMapper {

    @Select("SELECT prop_id FROM project_scheme_item WHERE project_id = #{projectId} ORDER BY id ASC")
    List<Long> selectPropIdsByProjectId(@Param("projectId") Long projectId);

    @Select("SELECT * FROM project_scheme_item WHERE project_id = #{projectId} ORDER BY id ASC")
    List<ProjectSchemeItemEntity> selectByProjectId(@Param("projectId") Long projectId);

    @Insert("INSERT INTO project_scheme_item (project_id, prop_id, quantity, created_at) VALUES (#{projectId}, #{propId}, #{quantity}, NOW()) ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)")
    int insertOrIncrease(@Param("projectId") Long projectId, @Param("propId") Long propId, @Param("quantity") Integer quantity);

    @Update("UPDATE project_scheme_item SET quantity = #{quantity} WHERE project_id = #{projectId} AND prop_id = #{propId}")
    int updateQuantity(@Param("projectId") Long projectId, @Param("propId") Long propId, @Param("quantity") Integer quantity);

    @Delete("DELETE FROM project_scheme_item WHERE project_id = #{projectId} AND prop_id = #{propId}")
    int deleteByProjectIdAndPropId(@Param("projectId") Long projectId, @Param("propId") Long propId);
}
