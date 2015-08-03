package com.ttsd.api.service;

import com.esoft.archer.user.model.User;
import com.ttsd.api.dto.*;

public interface MobilePersonalInfoAppService {
    public BaseResponseDto getPersonalInfoData(PersonalInfoRequestDto personalInfoRequestDto);

    public PersonalInfoResponseDataDto generatePersonalInfoData(User user);

    public boolean verifyCertification(String userName);

    public boolean isBindedBankCard(String userName);

}
