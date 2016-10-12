package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigStatus;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
@CacheNamespace
public interface IPhone7LotteryConfigMapper {

    @Insert({
            "insert into iphone7_lottery_config (",
            "period, lottery_number, created_by, created_time, status",
            ") values (",
            "#{period}, #{lotteryNumber}, #{createdBy}, #{createdTime}, 'TO_APPROVE'",
            ")"
    })
    @Options(useGeneratedKeys = true)
    int create(IPhone7LotteryConfigModel model);

    @Results(id = "iphone7LotteryConfigResultMap", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "period", property = "period"),
            @Result(column = "lottery_number", property = "lotteryNumber"),
            @Result(column = "effective_time", property = "effectiveTime"),
            @Result(column = "created_by", property = "createdBy"),
            @Result(column = "created_time", property = "createdTime"),
            @Result(column = "audited_by", property = "auditedBy"),
            @Result(column = "audited_time", property = "auditedTime"),
            @Result(column = "status", property = "status")
    })
    @Select("select * from iphone7_lottery_config where status = #{status} order by period")
    List<IPhone7LotteryConfigModel> findByStatus(@Param("status") IPhone7LotteryConfigStatus status);

    @Delete("delete from iphone7_lottery_config where period = #{period} and status='APPROVED'")
    int removeApprovedConfig(@Param("period") int period);

    @Delete("delete from iphone7_lottery_config where id = #{id} and status<>'APPROVED' and status<>'EFFECTIVE'")
    int removeUnApprovedConfig(@Param("id") long id);

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
}
