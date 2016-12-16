package com.tuotiansudai.console.service;

import com.tuotiansudai.repository.mapper.BankMapper;
import com.tuotiansudai.repository.model.BankModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsoleBankService {

    @Autowired
    private BankMapper bankMapper;

    public boolean updateBank(BankModel bankModel) {
        if(bankModel == null){
            return false;
        }
        bankMapper.update(bankModel);
        return true;
    }
}
