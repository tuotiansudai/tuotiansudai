package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.ThirdAnniversaryHelpInfoModel;
import com.tuotiansudai.activity.repository.model.ThirdAnniversaryHelpModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ThirdAnniversaryHelpInfoMapper {

    void create(ThirdAnniversaryHelpInfoModel thirdAnniversaryHelpInfoModel);

    List<ThirdAnniversaryHelpInfoModel> findByHelpId(@Param(value = "helpId") long helpId);

}
