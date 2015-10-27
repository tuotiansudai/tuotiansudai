package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.BankCardModel;

public interface BindEmailService {

    boolean sendActiveEmail(String email,String url);

    String verifyEmail(String uuid);


}
