package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferModel;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvestServiceImpl implements InvestService{

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;



    @Override
    public BaseDto<PayFormDataDto> invest(InvestDto dto) {
        long loanId = Long.parseLong(dto.getLoanId());
        // TODO: 查找对应标的
        //LoanDetailDto loanDetailDto = loanService.getLoanDetail(loanId);
        //if (loanDetailDto == null) {
        //    throw new LoanNotFoundException("查找不到对应的标的信息:" + loanId);
        //}

        // 检查标的可投余额
        // 已投金额
        long investedAmount = getSuccessInvestedAmountByLoanId(loanId);
        // TODO: 剩余可投金额
        //long remainInvestableAmount = loanDetailDto.getLoanAmount() - investedAmount;
        long remainInvestableAmount = 100;
        if(remainInvestableAmount < amount){
            throw new InsufficientBalanceException(String.format("投资金额超过了该标的的可投金额",amount,remainInvestableAmount));
        }

        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        InvestModel investModel = new InvestModel(dto);
        investModel.setId(idGenerator.generate());
        ProjectTransferModel requestModel = new ProjectTransferModel(
                dto.getLoanId(),
                String.valueOf(investModel.getId()),
                accountModel.getPayUserId(),
                dto.getAmount());
        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
            investMapper.create(investModel);
            return baseDto;
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }
}
