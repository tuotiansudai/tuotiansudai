package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.ThirdAnniversaryDrawModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThirdAnniversaryDrawMapper {

    void create(List<ThirdAnniversaryDrawModel> models);

    int countDrawByLoginName(@Param(value = "loginName") String loginName);

    List<ThirdAnniversaryDrawModel> findByLoginName(@Param(value = "loginName") String loginName);

    List<String> findLoginNameByCollectTopFour(List<String> topFour);
}
