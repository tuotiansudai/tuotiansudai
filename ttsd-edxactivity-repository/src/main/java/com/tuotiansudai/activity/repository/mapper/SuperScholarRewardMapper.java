package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.SuperScholarRewardModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuperScholarRewardMapper {

    void create(SuperScholarRewardModel superScholarReward);

    void update(SuperScholarRewardModel superScholarRewardModel);

    SuperScholarRewardModel findByLoginNameAndAnswerTime(@Param(value = "loginName") String loginName,
                                                         @Param(value = "answerTime") String answerTime);

    SuperScholarRewardModel findByLoginNameAndCreatedTime(@Param(value = "loginName") String loginName,
                                                          @Param(value = "createdTime") String createdTime);

    List<SuperScholarRewardModel> findByAnswerTime(@Param(value = "answerTime") String answerTime);

}
