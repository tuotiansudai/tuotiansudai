package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace
public interface IPhone7InvestLotteryMapper {

    @Insert({
            "insert into iphone7_invest_lottery (",
            "invest_id, login_name, lottery_number, invest_amount, lottery_time, status",
            ") values (",
            "#{investId}, #{loginName}, #{lotteryNumber}, #{investAmount}, #{lotteryTime}, 'WAITING'",
            ")"
    })
    @Options(useGeneratedKeys = true)
    int create(IPhone7InvestLotteryModel model);


    @Results(id = "iphone7InvestLotteryResultMap", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "invest_id", property = "investId"),
            @Result(column = "login_name", property = "loginName"),
            @Result(column = "lottery_number", property = "lotteryNumber"),
            @Result(column = "invest_amount", property = "investAmount"),
            @Result(column = "lottery_time", property = "lotteryTime"),
            @Result(column = "status", property = "status")
    })
    @Select("select * from iphone7_invest_lottery where login_name=#{loginName} order by id")
    List<IPhone7InvestLotteryModel> findByLoginName(@Param("loginName") String loginName);


}
