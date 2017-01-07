package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.InvestRewardModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvestRewardMapper {

    void create(InvestRewardModel investRewardModel);


    List<InvestRewardModel> findInvestRewardModels(@Param(value = "mobile") String mobile,
                                                   @Param(value = "index") Integer index,
                                                   @Param(value = "pageSize") Integer pageSize);

    long findInvestRwardCount(@Param(value = "mobile") String mobile);

    InvestRewardModel findByMobile(@Param(value = "mobile") String mobile);

    void update(InvestRewardModel annualPrizeModel);
}
