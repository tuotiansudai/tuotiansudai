package com.ttsd.api.service;

import com.esoft.archer.user.model.User;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.ttsd.api.dto.*;

public interface MobileAppPersonalInfoService {
    BaseResponseDto getPersonalInfoData(PersonalInfoRequestDto personalInfoRequestDto);

    PersonalInfoResponseDataDto generatePersonalInfoData(User user);

    boolean verifyCertification(String userName);

    boolean isBindedBankCard(String userName);

    BankCard queryBankCardByUserId(String userId);

    boolean isOpenFastPayment(BankCard bankCard);


}
