package com.tuotiansudai.service;

import cn.jpush.api.utils.StringUtils;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanApplicationDto;
import com.tuotiansudai.dto.LoanConsumeApplicationDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanApplicationMapper;
import com.tuotiansudai.repository.model.LoanApplicationModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.IdentityNumberValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanApplicationService {

    private static Logger logger = Logger.getLogger(LoanApplicationService.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private UserMapper userMapper;

    public BaseDto<BaseDataDto> create(LoanApplicationDto loanApplicationDto) {
        if (null == accountMapper.findByLoginName(loanApplicationDto.getLoginName())) {
            return new BaseDto<>(new BaseDataDto(false, "账户没有实名认证"));
        }
        if (loanApplicationDto.getAmount() <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "借款金额必须是大于等于1的整数"));
        }
        if (loanApplicationDto.getPeriod() <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "借款期限必须是大于等于1的整数"));
        }
        if(StringUtils.isEmpty(loanApplicationDto.getLoanUsage())){
            return new BaseDto<>(new BaseDataDto(false, "借款用途不能为空"));
        }
        if(StringUtils.isEmpty(loanApplicationDto.getPledgeInfo())){
            return new BaseDto<>(new BaseDataDto(false, "抵押物信息不能为空"));
        }
        LoanApplicationModel loanApplicationModel = new LoanApplicationModel(loanApplicationDto);
        UserModel userModel = userMapper.findByLoginName(loanApplicationDto.getLoginName());
        loanApplicationModel.setMobile(userModel.getMobile());
        loanApplicationModel.setUserName(userModel.getUserName());
        loanApplicationModel.setIdentityNumber(userModel.getIdentityNumber());
        loanApplicationModel.setAge((short)IdentityNumberValidator.getAgeByIdentityCard(userModel.getIdentityNumber(),18));
        loanApplicationModel.setAddress(IdentityNumberValidator.getCityByIdentityCard(userModel.getIdentityNumber()));
        loanApplicationModel.setSex("MALE".equalsIgnoreCase(IdentityNumberValidator.getSexByIdentityCard(userModel.getIdentityNumber(),"MALE"))?"男":"女");
        try {
            loanApplicationMapper.create(loanApplicationModel);
        } catch (Exception e) {
            logger.info(e);
        }
        return new BaseDto<>(new BaseDataDto(true));
    }

    public BaseDto<BaseDataDto> createConsume(LoanConsumeApplicationDto loanConsumeApplicationDto){
        return new BaseDto<>(new BaseDataDto(true));
    }
}
