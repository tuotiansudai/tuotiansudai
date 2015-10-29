package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PersonalInfoRequestDto;
import com.tuotiansudai.api.dto.PersonalInfoResponseDataDto;
import com.tuotiansudai.api.service.MobileAppPersonalInfoService;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppPersonalInfoServiceImpl implements MobileAppPersonalInfoService {

    @Override
    public BaseResponseDto getPersonalInfoData(PersonalInfoRequestDto personalInfoRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public PersonalInfoResponseDataDto generatePersonalInfoData(UserModel user) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public boolean verifyCertification(String userName) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public boolean isBindedBankCard(String userName) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BankCardModel queryBankCardByUserId(String userId) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public boolean isOpenFastPayment(BankCardModel bankCard) {
        throw new NotImplementedException(getClass().getName());
    }
}
