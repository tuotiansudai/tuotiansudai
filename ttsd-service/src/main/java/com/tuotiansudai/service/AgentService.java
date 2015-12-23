package com.tuotiansudai.service;

import com.tuotiansudai.dto.AgentDto;
import com.tuotiansudai.exception.CreateAgentException;
import com.tuotiansudai.repository.model.AgentLevelRateModel;

import java.util.List;

public interface AgentService {

    int findAgentLevelRateCount(String loginName);

    List<AgentLevelRateModel> findAgentLevelRate(String loginName, int index, int pageSize);

    void create(AgentDto agentDto) throws CreateAgentException;

    void update(AgentDto agentDto);

    void delete(long id);


}
