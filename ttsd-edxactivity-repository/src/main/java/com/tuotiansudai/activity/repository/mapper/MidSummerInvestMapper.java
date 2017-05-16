package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.MidSummerInvestModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MidSummerInvestMapper {

    void create(MidSummerInvestModel model);

    List<MidSummerInvestModel> findByReferrerLoginName(@Param(value = "referrerLoginName") String referrerLoginName);
}
