package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanApplicationDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanApplicationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.LoanApplicationModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoanApplicationService {

    private static Logger logger = Logger.getLogger(LoanApplicationService.class);

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    LoanApplicationMapper loanApplicationMapper;

    @Autowired
    UserMapper userMapper;

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
        LoanApplicationModel loanApplicationModel = new LoanApplicationModel(loanApplicationDto);
        UserModel userModel = userMapper.findByLoginName(loanApplicationDto.getLoginName());
        loanApplicationModel.setMobile(userModel.getMobile());
        loanApplicationModel.setUserName(userModel.getUserName());
        try {
            loanApplicationMapper.create(loanApplicationModel);
        } catch (Exception e) {
            logger.debug(e);
        }
        return new BaseDto<>(new BaseDataDto(true));
    }

    public BaseDto<BaseDataDto> comment(LoanApplicationModel loanApplicationModel) {
        LoanApplicationModel dbLoanApplicationModel = loanApplicationMapper.findById(loanApplicationModel.getId());
        if (null == dbLoanApplicationModel) {
            return new BaseDto<>(new BaseDataDto(false, "该借款申请不存在"));
        }

        dbLoanApplicationModel.setComment(loanApplicationModel.getComment());
        dbLoanApplicationModel.setUpdatedBy(loanApplicationModel.getUpdatedBy());
        dbLoanApplicationModel.setUpdatedTime(new Date());

        loanApplicationMapper.update(dbLoanApplicationModel);

        return new BaseDto<>(new BaseDataDto(true));
    }

    public BasePaginationDataDto<LoanApplicationModel> getPagination(int index, int pageSize) {
        long count = loanApplicationMapper.findCount();
        List<LoanApplicationModel> loanApplicationModels = loanApplicationMapper.findPagination(PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return new BasePaginationDataDto<>(index, pageSize, count, loanApplicationModels);
    }
}
