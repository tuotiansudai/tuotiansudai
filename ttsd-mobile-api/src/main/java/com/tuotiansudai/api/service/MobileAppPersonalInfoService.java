package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PersonalInfoRequestDto;
import com.tuotiansudai.api.dto.PersonalInfoResponseDataDto;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.UserModel;

public interface MobileAppPersonalInfoService {
    BaseResponseDto getPersonalInfoData(PersonalInfoRequestDto personalInfoRequestDto);

    PersonalInfoResponseDataDto generatePersonalInfoData(UserModel user);

    boolean verifyCertification(String userName);

    boolean isBindedBankCard(String userName);

    BankCardModel queryBankCardByUserId(String userId);

    boolean isOpenFastPayment(BankCardModel bankCard);


}
