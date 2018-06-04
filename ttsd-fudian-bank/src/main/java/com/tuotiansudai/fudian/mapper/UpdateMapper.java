package com.tuotiansudai.fudian.mapper;

import com.tuotiansudai.fudian.dto.response.LoanCallbackInvestItemContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UpdateMapper {

    @Update({"<script>",
            "<foreach collection='investItems' item='investItem' separator=';'>",
            "UPDATE loan_callback_invest ",
            "<set> ",
            "`ret_code` = #{investItem.retCode}, `ret_msg` = #{investItem.retMsg} ",
            "</set>",
            "<where>",
            "`order_no` = #{investItem.orderNo} ",
            "</where>",
            "</foreach>",
            "</script>"})
    void updateLoanCallbackInvestItems(@Param("investItems") List<LoanCallbackInvestItemContentDto> investItems);

    @Update("UPDATE ${tableName} SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now()  WHERE `order_no` = #{dto.content.orderNo} and `notify_response_data` is null")
    int updateNotifyResponseData(@Param(value = "tableName") String tableName,
                                 @Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE ${tableName} SET `return_response_data` = #{dto.reqData} WHERE `order_no` = #{dto.content.orderNo} AND return_response_data is null")
    void updateReturnResponse(@Param(value = "tableName") String tableName,
                              @Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE ${tableName} SET `query_response_data` = #{dto.reqData}, `query_time` = now() WHERE `order_no` = #{dto.content.queryOrderNo} AND `query_response_data` is null")
    void updateQueryResponse(@Param(value = "tableName") String tableName,
                            @Param(value = "dto") ResponseDto responseDto);

}
