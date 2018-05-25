package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.SuperScholarRewardModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SuperScholarRewardMapper {

    void create(SuperScholarRewardModel superScholarReward);

    void update(SuperScholarRewardModel superScholarRewardModel);

    int updateUserAnswer(@Param(value = "id") long id,
                         @Param(value = "userAnswer") String userAnswer,
                         @Param(value = "userRight") int userRight,
                         @Param(value = "couponId") long couponId);

    SuperScholarRewardModel findById(@Param(value = "id") long id);

    SuperScholarRewardModel findByLoginNameAndAnswerTime(@Param(value = "loginName") String loginName,
                                                         @Param(value = "answerTime") Date answerTime);

    SuperScholarRewardModel findByLoginNameAndCreatedTime(@Param(value = "loginName") String loginName,
                                                          @Param(value = "createdTime") Date createdTime);

    List<SuperScholarRewardModel> findByAnswerTime(@Param(value = "answerTime") Date answerTime);

}
