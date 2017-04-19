package com.tuotiansudai.service.impl;

import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.repository.mapper.ExperienceBillMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ExperienceBillModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.ExperienceBillService;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class ExperienceServiceImpl implements ExperienceBillService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ExperienceBillMapper experienceBillMapper;

    @Override
    @Transactional
    public void updateUserExperienceBalanceByLoginName(long experienceAmount, String loginName, ExperienceBillOperationType experienceBillOperationType, ExperienceBillBusinessType experienceBusinessType,String note) {
        UserModel userModel = userMapper.lockByLoginName(loginName);
        long experienceBalance = userModel.getExperienceBalance();
        experienceBalance = experienceBillOperationType == ExperienceBillOperationType.IN ? experienceBalance + experienceAmount : experienceBalance - experienceAmount;
        userModel.setExperienceBalance(experienceBalance);
        userMapper.updateUser(userModel);

        ExperienceBillModel experienceBillModel = new ExperienceBillModel(loginName,
                experienceBillOperationType,
                experienceAmount,
                experienceBusinessType,
                note);

        experienceBillMapper.create(experienceBillModel);
    }


}
