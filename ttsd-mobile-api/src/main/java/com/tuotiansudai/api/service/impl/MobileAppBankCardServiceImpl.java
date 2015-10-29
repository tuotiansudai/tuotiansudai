package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BankCardReplaceRequestDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppBankCardService;
import com.tuotiansudai.repository.model.BankCardModel;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppBankCardServiceImpl implements MobileAppBankCardService {

    @Override
    public boolean queryBindAndSginStatus(String userId, String operationType) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BaseResponseDto generateBankCardResponse(BankCardReplaceRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public void save(BankCardModel bankCard) {
        throw new NotImplementedException(getClass().getName());
    }
}
