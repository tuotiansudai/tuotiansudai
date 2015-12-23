package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.AgentDto;
import com.tuotiansudai.exception.CreateAgentException;
import com.tuotiansudai.repository.mapper.AgentLevelRateMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AgentLevelRateModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgentServiceImpl implements AgentService {

    @Value("#{'${pay.staff.reward}'.split('\\|')}")
    private List<Double> referrerStaffRoleReward;
    @Autowired
    private AgentLevelRateMapper agentLevelRateMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public int findAgentLevelRateCount(String loginName) {
        return agentLevelRateMapper.findAgentLevelRateCount(loginName);
    }

    @Override
    public List<AgentLevelRateModel> findAgentLevelRate(String loginName, int index, int pageSize) {
        return agentLevelRateMapper.findAgentLevelRate(loginName, index, pageSize);
    }

    @Override
    public void create(AgentDto agentDto) throws CreateAgentException {

        this.checkAgentDto(agentDto);

        AgentLevelRateModel agentLevelRateModel = new AgentLevelRateModel(agentDto);

        agentLevelRateMapper.create(agentLevelRateModel);

    }

    @Override
    @Transactional
    public void update(AgentDto agentDto) {
        AgentLevelRateModel agentLevelRateModel = new AgentLevelRateModel(agentDto);
        agentLevelRateMapper.update(agentLevelRateModel);
    }

    @Override
    public void delete(long id) {
        agentLevelRateMapper.delete(id);
    }

    private void checkAgentDto(AgentDto agentDto) throws CreateAgentException{
        int level = Integer.parseInt(agentDto.getLevel());
        int maxLevel = referrerStaffRoleReward.size();
        String loginName = agentDto.getLoginName();
        UserModel userModel = userMapper.findByLoginName(loginName);
        if(userModel == null){
            throw new CreateAgentException("代理人" + loginName+"在系统中未进行维护,不能新增层级和收益比例!");
        }
        if(level > maxLevel){
            throw new CreateAgentException("代理人" + agentDto.getLoginName() + "增加层级不能超过系统配置最高" + maxLevel+"层级");
        }
        AgentLevelRateModel agentLevelRateModel = agentLevelRateMapper.findAgentLevelRateByLoginNameAndLevel(loginName,level);

        if(agentLevelRateModel != null){
            throw new CreateAgentException("代理人"+ loginName +"的层级" + level +"已经进行了配置!");
        }

    }
}
