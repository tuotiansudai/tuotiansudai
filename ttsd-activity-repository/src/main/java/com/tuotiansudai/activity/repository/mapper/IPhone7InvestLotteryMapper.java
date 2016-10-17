package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryModel;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryStatView;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryWinnerView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace(implementation = com.tuotiansudai.cache.MybatisRedisCache.class)
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


    @Results({
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


    @Results(id = "statInvestResultMap", value = {
            @Result(column = "login_name", property = "loginName"),
            @Result(column = "invest_amount_total", property = "investAmountTotal"),
            @Result(column = "invest_count", property = "investCount"),
            @Result(column = "lottery_number_array", property = "lotteryNumberArray")
    })
    @Select({
            "<script>",
            "select login_name, sum(invest_amount) as invest_amount_total, count(*) as invest_count,",
            "group_concat(lottery_number separator ',') as lottery_number_array",
            "from iphone7_invest_lottery",
            "<where>",
            "<if test=\"loginName!=null\">login_name=#{loginName}</if>",
            "</where>",
            "group by login_name",
            "limit #{rowLimit} offset #{rowIndex}",
            "</script>"
    })
    List<IPhone7InvestLotteryStatView> statInvest(
            @Param("loginName") String loginName,
            @Param("rowIndex") int rowIndex,
            @Param("rowLimit") int rowLimit);

    @Select("select count(distinct login_name) from iphone7_invest_lottery")
    int statUserCount();

    @ResultMap("statInvestResultMap")
    @Select({
            "select login_name, sum(invest_amount) as invest_amount_total, count(*) as invest_count",
            "from iphone7_invest_lottery group by login_name"
    })
    List<IPhone7InvestLotteryStatView> allStatInvest();

    @Select("select count(*) from iphone7_invest_lottery")
    int statInvestCount();

    @Results({
            @Result(column = "login_name", property = "loginName"),
            @Result(column = "lottery_number", property = "lotteryNumber"),
            @Result(column = "effective_time", property = "effectiveTime")
    })
    @Select({
            "select a.login_name, a.lottery_number, b.effective_time",
            "from iphone7_invest_lottery a",
            "inner join iphone7_lottery_config b",
            "on a.lottery_number = b.lottery_number",
            "where b.status = 'EFFECTIVE'",
            "order by b.effective_time"
    })
    List<IPhone7InvestLotteryWinnerView> listWinner();
}
