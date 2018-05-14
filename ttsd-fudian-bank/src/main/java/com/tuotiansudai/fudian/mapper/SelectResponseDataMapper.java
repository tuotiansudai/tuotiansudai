package com.tuotiansudai.fudian.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SelectResponseDataMapper {

    @Select("SELECT `response_data` FROM `${tableName}` where `order_no` = #{orderNo}")
    String selectResponseData(@Param("tableName") String tableName,
                              @Param(value = "orderNo") String orderNo);
}
