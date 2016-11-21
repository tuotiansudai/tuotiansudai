package com.tuotiansudai.diagnosis.repository;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserBillExtMapper {

    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "login_name", property = "loginName"),
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "amount", property = "amount"),
            @Result(column = "balance", property = "balance"),
            @Result(column = "freeze", property = "freeze"),
            @Result(column = "operation_type", property = "operationType"),
            @Result(column = "business_type", property = "businessType"),
            @Result(column = "operator_login_name", property = "operatorLoginName"),
            @Result(column = "intervention_reason", property = "interventionReason"),
            @Result(column = "created_time", property = "createdTime")
    })
    @Select("select * from user_bill where order_id=#{orderId} and business_type=#{businessType}")
    List<UserBillModel> findByOrderIdAndBusinessType(@Param("orderId") long orderId, @Param("businessType") UserBillBusinessType businessType);

    @Select("select distinct login_name from user_bill where order_id is not null")
    List<String> findAllLoginName();

    @Select("select distinct login_name from user_bill where created_time < #{endDate}")
    List<String> findLoginNameUntil(@Param("endDate") Date endDate);

    @Select("select distinct login_name from user_bill where created_time >= #{beginDate} and created_time < #{endDate}")
    List<String> findLoginNameByTime(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);
}
