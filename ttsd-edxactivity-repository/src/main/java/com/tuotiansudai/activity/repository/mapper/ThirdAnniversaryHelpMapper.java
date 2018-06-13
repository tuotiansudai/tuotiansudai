package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.ThirdAnniversaryHelpModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ThirdAnniversaryHelpMapper {

    void create(ThirdAnniversaryHelpModel thirdAnniversaryHelpModel);

    ThirdAnniversaryHelpModel findById(@Param(value = "id") long id);

    ThirdAnniversaryHelpModel findByLoginName(@Param(value = "loginName") String loginName);

    ThirdAnniversaryHelpModel lockByLoginName(@Param(value = "loginName") String loginName);

    List<ThirdAnniversaryHelpModel> findAll();

}
