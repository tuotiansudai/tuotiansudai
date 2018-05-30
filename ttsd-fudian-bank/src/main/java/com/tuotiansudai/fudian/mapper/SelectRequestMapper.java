package com.tuotiansudai.fudian.mapper;

import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanInvestRequestDto;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SelectRequestMapper {

    @Results(id = "baseResponseDtoMap", value = {
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "order_date", property = "orderDate")
    })
    @Select("select order_no, order_date from ${tableName} " +
            "where status = 'SENT' and DATE_ADD(request_time, INTERVAL 1 HOUR) > now() and DATE_ADD(request_time, INTERVAL 5 MINUTE) < now()")
    List<BaseRequestDto> selectResponseInOneHour(@Param("tableName") String tableName);
}
