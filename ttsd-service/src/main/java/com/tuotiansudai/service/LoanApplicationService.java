package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanApplicationDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanApplicationMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanApplicationModel;
import com.tuotiansudai.repository.model.LoanApplicationView;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoanApplicationService {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    LoanApplicationMapper loanApplicationMapper;

    public BaseDto<BaseDataDto> create(LoanApplicationDto loanApplicationDto) {
        AccountModel accountModel = accountMapper.findByLoginName(loanApplicationDto.getLoginName());
        if (null == accountModel) {
            return new BaseDto<>(new BaseDataDto(false, "账户没有实名认证"));
        }
        if (loanApplicationDto.getAmount() <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "借款金额必须是大于等于1的整数"));
        }
        if (loanApplicationDto.getPeriod() <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "借款期限必须是大于等于1的整数"));
        }
        LoanApplicationModel loanApplicationModel = new LoanApplicationModel(loanApplicationDto);
        loanApplicationMapper.create(loanApplicationModel);
        return new BaseDto<>(new BaseDataDto(true));
    }

    public BaseDto<BaseDataDto> comment(LoanApplicationView loanApplicationView) {
        LoanApplicationModel loanApplicationModel = loanApplicationMapper.findById(loanApplicationView.getId());
        if (null == loanApplicationModel) {
            return new BaseDto<>(new BaseDataDto(false, "该借款申请不存在"));
        }

        loanApplicationModel.setComment(loanApplicationView.getComment());
        loanApplicationModel.setUpdatedBy(loanApplicationView.getUpdatedBy());
        loanApplicationModel.setUpdatedTime(new Date());

        loanApplicationMapper.update(loanApplicationModel);

        return new BaseDto<>(new BaseDataDto(true));
    }

    public BasePaginationDataDto<LoanApplicationView> getPagination(int index, int pageSize) {
        long count = loanApplicationMapper.findCount();
        List<LoanApplicationView> loanApplicationViews = loanApplicationMapper.findViewPagination(PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return new BasePaginationDataDto<>(index, pageSize, count, loanApplicationViews);
    }
}
