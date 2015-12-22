package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AgentLevelRateModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentLevelRateMapper {
    int findAgentLevelRateCount(@Param("loginName") String loginName);

    List<AgentLevelRateModel> findAgentLevelRate(@Param("loginName") String loginName,@Param("index") int index,@Param("pageSize") int pageSize);

    void create(AgentLevelRateModel agentLevelRateModel);

    void update(AgentLevelRateModel agentLevelRateModel);

    void delete(long id);

    AgentLevelRateModel findAgentLevelRateById(long id);

    AgentLevelRateModel findAgentLevelRateByLoginNameAndLevel(@Param("loginName") String loginName,@Param("level") int level);
}
