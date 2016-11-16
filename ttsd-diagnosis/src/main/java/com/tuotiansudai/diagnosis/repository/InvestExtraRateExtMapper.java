package com.tuotiansudai.diagnosis.repository;

import com.tuotiansudai.repository.model.InvestExtraRateModel;
import org.apache.ibatis.annotations.*;

@Mapper
public interface InvestExtraRateExtMapper {

    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "loan_id", property = "loanId"),
            @Result(column = "invest_id", property = "investId"),
            @Result(column = "is_transfer", property = "isTransfer"),
            @Result(column = "amount", property = "amount"),
            @Result(column = "login_name", property = "loginName"),
            @Result(column = "extra_rate", property = "extraRate"),
            @Result(column = "expected_interest", property = "expectedInterest"),
            @Result(column = "expected_fee", property = "expectedFee"),
            @Result(column = "actual_interest", property = "actualInterest"),
            @Result(column = "actual_fee", property = "actualFee"),
            @Result(column = "repay_amount", property = "repayAmount"),
            @Result(column = "repay_date", property = "repayDate"),
            @Result(column = "actual_repay_date", property = "actualRepayDate"),
            @Result(column = "created_time", property = "createdTime"),
    })
    @Select("select * from invest_extra_rate where id=#{id}")
    InvestExtraRateModel findById(@Param("id") long id);
}
