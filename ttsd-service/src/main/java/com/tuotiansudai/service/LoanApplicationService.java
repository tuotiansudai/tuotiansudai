package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanApplicationDto;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.LoanApplicationMapper;
import com.tuotiansudai.repository.model.LoanApplicationModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanApplicationService {

    private static Logger logger = Logger.getLogger(LoanApplicationService.class);

    @Autowired
    BankAccountMapper bankAccountMapper;

    @Autowired
    LoanApplicationMapper loanApplicationMapper;

    @Autowired
    UserMapper userMapper;

    public BaseDto<BaseDataDto> create(LoanApplicationDto loanApplicationDto) {
        if (null == bankAccountMapper.findByLoginName(loanApplicationDto.getLoginName())) {
            return new BaseDto<>(new BaseDataDto(false, "账户没有实名认证"));
        }
        if (loanApplicationDto.getAmount() <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "借款金额必须是大于等于1的整数"));
        }
        if (loanApplicationDto.getPeriod() <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "借款期限必须是大于等于1的整数"));
        }
        LoanApplicationModel loanApplicationModel = new LoanApplicationModel(loanApplicationDto);
        UserModel userModel = userMapper.findByLoginName(loanApplicationDto.getLoginName());
        loanApplicationModel.setMobile(userModel.getMobile());
        loanApplicationModel.setUserName(userModel.getUserName());
        try {
            loanApplicationMapper.create(loanApplicationModel);
        } catch (Exception e) {
            logger.info(e);
        }
        return new BaseDto<>(new BaseDataDto(true));
    }
}
