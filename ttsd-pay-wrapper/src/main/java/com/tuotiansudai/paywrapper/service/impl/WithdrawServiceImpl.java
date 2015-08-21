package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CustWithdrawalsMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.CustWithdrawalsModel;
import com.tuotiansudai.paywrapper.service.WithdrawService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WithdrawServiceImpl implements WithdrawService{

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> withdraw(WithdrawDto withdrawDto) {
        AccountModel accountModel = accountMapper.findByLoginName(withdrawDto.getLoginName());
        WithdrawModel withdrawModel = new WithdrawModel(withdrawDto);
        withdrawModel.setId(idGenerator.generate());
        CustWithdrawalsModel requestModel = new CustWithdrawalsModel(String.valueOf(withdrawModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(withdrawModel.getAmount()));
        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(CustWithdrawalsMapper.class, requestModel);
            withdrawMapper.create(withdrawModel);
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
