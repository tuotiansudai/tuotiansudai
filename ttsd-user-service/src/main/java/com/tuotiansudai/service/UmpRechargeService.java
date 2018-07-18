package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.request.UmpRechargeRequestDto;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UmpRechargeService {

    private static Logger logger = LoggerFactory.getLogger(UmpRechargeService.class);

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    private final RechargeMapper rechargeMapper;

    private final AccountMapper accountMapper;

    @Autowired
    public UmpRechargeService(RechargeMapper rechargeMapper, AccountMapper accountMapper) {
        this.rechargeMapper = rechargeMapper;
        this.accountMapper = accountMapper;
    }

    public UmpAsyncMessage recharge(UmpRechargeRequestDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        RechargeModel rechargeModel = new RechargeModel(dto);
        rechargeModel.setId(IdGenerator.generate());
        rechargeMapper.create(rechargeModel);
        return bankWrapperClient.umpRecharge(dto.getLoginName(), accountModel.getPayUserId(), rechargeModel.getId(), rechargeModel.getAmount(), rechargeModel.isFastPay(), rechargeModel.getBankCode());
    }
}
