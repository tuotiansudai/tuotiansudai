package com.tuotiansudai.fudian.mapper.fudian;

import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SelectMapper {

    @Select("SELECT `notify_response_data` FROM `${tableName}` where `order_no` = #{orderNo}")
    String selectNotifyResponseData(@Param("tableName") String tableName,
                                    @Param(value = "orderNo") String orderNo);

    @Select("SELECT `query_response_data` FROM `${tableName}` where `order_no` = #{orderNo}")
    String selectQueryResponseData(@Param("tableName") String tableName,
                                   @Param(value = "orderNo") String orderNo);

    @Results(id = "baseResponseDtoMap", value = {
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "order_date", property = "orderDate")
    })
    @Select("select order_no, order_date from ${tableName} " +
            "where `query_response_data` is null and DATE_ADD(request_time, INTERVAL 30 MINUTE) > now()")
    List<BaseRequestDto> selectResponseInOneHour(@Param("tableName") String tableName);

    @Select("SELECT `pay_user_id` FROM `account` where `login_name` = #{loginName}")
    String selectPayUserId(@Param("loginName") String loginName);

}
