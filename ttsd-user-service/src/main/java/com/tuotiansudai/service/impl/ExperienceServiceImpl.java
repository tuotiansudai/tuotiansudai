package com.tuotiansudai.service.impl;

import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.repository.mapper.ExperienceAccountMapper;
import com.tuotiansudai.repository.mapper.ExperienceBillMapper;
import com.tuotiansudai.repository.model.ExperienceBillModel;
import com.tuotiansudai.service.ExperienceBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExperienceServiceImpl implements ExperienceBillService {

    @Autowired
    private ExperienceAccountMapper experienceAccountMapper;

    @Autowired
    private ExperienceBillMapper experienceBillMapper;

    @Override
    @Transactional
    public void updateUserExperienceBalanceByLoginName(long experienceAmount, String loginName, ExperienceBillOperationType experienceBillOperationType, ExperienceBillBusinessType experienceBusinessType, String note) {
        if (experienceBillOperationType == ExperienceBillOperationType.OUT) {
            experienceAccountMapper.addBalance(loginName, -experienceAmount);
        } else {
            experienceAccountMapper.addBalance(loginName, experienceAmount);
        }

        ExperienceBillModel experienceBillModel = new ExperienceBillModel(loginName,
                experienceBillOperationType,
                experienceAmount,
                experienceBusinessType,
                note);

        experienceBillMapper.create(experienceBillModel);
    }
}
