package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.BankModel;

import java.util.List;

public interface BankService {

    BankModel findById(long id);

    boolean updateBank(BankModel bankModel);

    List<BankModel> findBankList();

    BankModel findByShorterName(String shorterName);
}
