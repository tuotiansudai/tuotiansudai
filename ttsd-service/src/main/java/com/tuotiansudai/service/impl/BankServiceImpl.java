package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BankDto;
import com.tuotiansudai.repository.mapper.BankMapper;
import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.service.BankService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl implements BankService {

    static Logger logger = Logger.getLogger(BankServiceImpl.class);

    @Autowired
    private BankMapper bankMapper;

    @Override
    public BankModel findById(long id) {
        return bankMapper.findById(id);
    }

    @Override
    public List<BankDto> findBankList(Long singleAmount, Long singleDayAmount) {
        List<BankModel> bankModels = bankMapper.findBankList(singleAmount, singleDayAmount);
        return Lists.transform(bankModels, BankDto::new);
    }

    @Override
    public List<BankDto> findUmpBankList(Long singleAmount, Long singleDayAmount) {
        List<BankModel> bankModels = bankMapper.findUmpBankList(singleAmount, singleDayAmount);
        return Lists.transform(bankModels, BankDto::new);
    }

    @Override
    public BankModel findByBankCode(String bankCode) {
        return bankMapper.findByBankCode(bankCode);
    }
}
