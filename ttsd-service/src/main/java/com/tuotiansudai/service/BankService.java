package com.tuotiansudai.service;

import com.tuotiansudai.dto.BankDto;
import com.tuotiansudai.repository.model.BankModel;

import java.util.List;

public interface BankService {

    BankModel findById(long id);

    List<BankDto> findBankList(Long singleAmount, Long singleDayAmount);

    List<BankDto> findUmpBankList(Long singleAmount, Long singleDayAmount);

    BankModel findByBankCode(String bankCode);
}
