package com.muhou.backend.infrastructure.persistence.mapper;

import com.muhou.backend.infrastructure.persistence.entity.PropImageEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PropImageMapper {

    @Select("SELECT * FROM prop_image WHERE prop_id = #{propId} ORDER BY sort_order ASC, id ASC")
    List<PropImageEntity> selectByPropId(@Param("propId") Long propId);

    @Select({
        "<script>",
        "SELECT * FROM prop_image WHERE prop_id IN",
        "<foreach collection='propIds' item='propId' open='(' separator=',' close=')'>",
        "#{propId}",
        "</foreach>",
        "ORDER BY prop_id ASC, sort_order ASC, id ASC",
        "</script>"
    })
    List<PropImageEntity> selectByPropIds(@Param("propIds") List<Long> propIds);

    @Delete("DELETE FROM prop_image WHERE prop_id = #{propId}")
    int deleteByPropId(@Param("propId") Long propId);

    @Insert({
        "<script>",
        "INSERT INTO prop_image (prop_id, image_url, sort_order, main_flag, created_at) VALUES",
        "<foreach collection='images' item='image' index='index' separator=','>",
        "(#{propId}, #{image}, #{index}, CASE WHEN #{index} = 0 THEN 1 ELSE 0 END, NOW())",
        "</foreach>",
        "</script>"
    })
    int batchInsert(@Param("propId") Long propId, @Param("images") List<String> images);
}
