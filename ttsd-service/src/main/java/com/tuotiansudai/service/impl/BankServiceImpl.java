package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
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
    public boolean updateBank(BankModel bankModel) {
        if(bankModel == null){
            return false;
        }
        bankMapper.update(bankModel);
        return true;
    }

    @Override
    public List<BankDto> findBankList() {
        List<BankModel> bankModels = bankMapper.findBankList();

        List<BankDto> records = Lists.transform(bankModels, new Function<BankModel, BankDto>() {
            @Override
            public BankDto apply(BankModel input) {
                return new BankDto(input);
            }
        });
        return records;
    }

}
