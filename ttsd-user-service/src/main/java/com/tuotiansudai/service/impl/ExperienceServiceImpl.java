package com.tuotiansudai.service.impl;

import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.repository.mapper.ExperienceBillMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ExperienceBillModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.ExperienceBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExperienceServiceImpl implements ExperienceBillService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ExperienceBillMapper experienceBillMapper;

    @Override
    @Transactional
    public void updateUserExperienceBalanceByLoginName(long experienceAmount, String loginName, ExperienceBillOperationType experienceBillOperationType, ExperienceBillBusinessType experienceBusinessType, String note) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        long experienceBalance = userModel.getExperienceBalance();
        experienceBalance = experienceBillOperationType == ExperienceBillOperationType.IN ? experienceBalance + experienceAmount : experienceBalance - experienceAmount;
        userMapper.updateExperienceBalance(loginName, experienceBillOperationType, experienceBalance);

        ExperienceBillModel experienceBillModel = new ExperienceBillModel(loginName,
                experienceBillOperationType,
                experienceAmount,
                experienceBusinessType,
                note);

        experienceBillMapper.create(experienceBillModel);
    }
}
