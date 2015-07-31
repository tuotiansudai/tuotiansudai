package com.ttsd.api.service;

import com.esoft.archer.user.model.User;
import com.ttsd.api.dto.*;

public interface MobilePersonalInfoAppService {
    public PersonalInfoResponseDto getPersonalInfoData(PersonalInfoRequestDto personalInfoRequestDto);

    public PersonalInfoDataDto generatePersonalInfoData(User user);

    public boolean verifyCertification(String userName);

    public boolean isBindedBankCard(String userName);

}
