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
    public void updateUserExperienceBalanceByLoginName(long experienceAmount, String loginName, ExperienceBillOperationType experienceBillOperationType, ExperienceBillBusinessType experienceBusinessType) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        long experienceBalance = userModel.getExperienceBalance();
        experienceBalance = experienceBillOperationType == ExperienceBillOperationType.IN ? experienceBalance + experienceAmount : experienceBalance - experienceAmount;
        userModel.setExperienceBalance(experienceBalance);
        userMapper.updateUser(userModel);

        ExperienceBillModel experienceBillModel = new ExperienceBillModel(loginName,
                experienceBillOperationType,
                experienceAmount,
                experienceBusinessType,
                MessageFormat.format(this.experienceBillNote(experienceBusinessType),
                        AmountConverter.convertCentToString(experienceAmount),
                        new Date()));

        experienceBillMapper.create(experienceBillModel);
    }

    private String experienceBillNote(ExperienceBillBusinessType experienceBusinessType) {
        switch (experienceBusinessType) {
            case INVEST_LOAN:
                return "您投资了拓天体验金项目，投资体验金金额：{0}元, 投资时间：{1}";
            case REGISTER:
                return "新手注册成功，获得体验金：{0}元, 注册时间：{1}";
            case MONEY_TREE:
                return "恭喜您在摇钱树活动中摇中了：{0}元体验金，摇奖时间：{1}";
        }
        return "";

    }
}
