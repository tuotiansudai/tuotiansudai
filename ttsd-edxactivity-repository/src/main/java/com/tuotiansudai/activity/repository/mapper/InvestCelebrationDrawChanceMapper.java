package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.InvestDrawChanceModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestCelebrationDrawChanceMapper {

    void create(InvestDrawChanceModel investDrawChanceModel);

    void updateChance(@Param(value="loginName") String loginName,
                      @Param(value="amount") int amount);

    int findChanceAmountByLoginName(String loginName);

    InvestDrawChanceModel findByLoginName(String loginName);

}
