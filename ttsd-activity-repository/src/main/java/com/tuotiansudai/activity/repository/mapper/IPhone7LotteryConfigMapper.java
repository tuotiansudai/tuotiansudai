package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
@CacheNamespace(implementation = com.tuotiansudai.cache.MybatisRedisCache.class)
public interface IPhone7LotteryConfigMapper {

    @Insert({
            "insert into iphone7_lottery_config (",
            "invest_amount, lottery_number, mobile, created_by, created_time, status",
            ") values (",
            "#{investAmount}, #{lotteryNumber}, #{mobile}, #{createdBy}, #{createdTime}, 'TO_APPROVE'",
            ")"
    })
    @Options(useGeneratedKeys = true)
    int create(IPhone7LotteryConfigModel model);

    @Results(id = "iphone7LotteryConfigResultMap", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "invest_amount", property = "investAmount"),
            @Result(column = "lottery_number", property = "lotteryNumber"),
            @Result(column = "mobile", property = "mobile"),
            @Result(column = "effective_time", property = "effectiveTime"),
            @Result(column = "created_by", property = "createdBy"),
            @Result(column = "created_time", property = "createdTime"),
            @Result(column = "audited_by", property = "auditedBy"),
            @Result(column = "audited_time", property = "auditedTime"),
            @Result(column = "status", property = "status")
    })
    @Select("select * from iphone7_lottery_config where status='EFFECTIVE' order by invest_amount")
    List<IPhone7LotteryConfigModel> effectiveList();

    @Select("select ifnull(max(invest_amount),0) from iphone7_lottery_config where status='EFFECTIVE'")
    int getCurrentLotteryInvestAmount();

    @ResultMap("iphone7LotteryConfigResultMap")
    @Select("select * from iphone7_lottery_config order by invest_amount")
    List<IPhone7LotteryConfigModel> list();

    @ResultMap("iphone7LotteryConfigResultMap")
    @Select("select * from iphone7_lottery_config where id = #{id}")
    IPhone7LotteryConfigModel findById(@Param("id") long id);

    @ResultMap("iphone7LotteryConfigResultMap")
    @Select("select * from iphone7_lottery_config where invest_amount = #{investAmount}")
    List<IPhone7LotteryConfigModel> findByInvestAmount(@Param("investAmount") long investAmount);

    @Delete("delete from iphone7_lottery_config where invest_amount = #{investAmount} and status='APPROVED'")
    int removeApprovedConfig(@Param("investAmount") long investAmount);

    @Delete("delete from iphone7_lottery_config where id = #{id} and status<>'APPROVED' and status<>'EFFECTIVE'")
    int removeUnApprovedConfig(@Param("id") long id);

    @ResultMap("iphone7LotteryConfigResultMap")
    @Select("select * from iphone7_lottery_config where lottery_number = #{lotteryNumber} and status = 'EFFECTIVE'")
    IPhone7LotteryConfigModel findEffictiveConfigByLotteryNumber(@Param("lotteryNumber") String lotteryNumber);

    @ResultMap("iphone7LotteryConfigResultMap")
    @Select("select * from iphone7_lottery_config where status = 'APPROVED' order by invest_amount")
    List<IPhone7LotteryConfigModel> findAllApproved();

    @ResultMap("iphone7LotteryConfigResultMap")
    @Select("select * from iphone7_lottery_config where status = 'APPROVED' or status = 'EFFECTIVE'")
    List<IPhone7LotteryConfigModel> findApprovedConfigByLotteryNumber(String lotteryNumber);

    @Update({
            "update iphone7_lottery_config set",
            "audited_by = #{auditedBy},",
            "audited_time = #{auditedTime},",
            "status = 'REFUSED'",
            "where id = #{id}"
    })
    int refuse(@Param("id") long id, @Param("auditedBy") String auditedBy, @Param("auditedTime") Date auditedTime);

    @Update({
            "update iphone7_lottery_config set",
            "audited_by = #{auditedBy},",
            "audited_time = #{auditedTime},",
            "status = 'APPROVED'",
            "where id = #{id}"
    })
    int approve(@Param("id") long id, @Param("auditedBy") String auditedBy, @Param("auditedTime") Date auditedTime);

    @Update({
            "update iphone7_lottery_config set",
            "effective_time = now(),",
            "status = 'EFFECTIVE'",
            "where id = #{id}"
    })
    int effective(@Param("id") long id);
}
