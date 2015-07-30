package com.ttsd.api.service;

import com.esoft.archer.user.model.User;
import com.ttsd.api.dto.LogInDataDto;
import com.ttsd.api.dto.LogInRequestDto;
import com.ttsd.api.dto.LogInResponseDto;

public interface MobileLogInAppService {
    public LogInResponseDto logIn(LogInRequestDto logInRequestDto);

    public LogInDataDto generateLogInData(User user);

    public boolean verifyCertification(String userName);

    public boolean isBindedBankCard(String userName);

}
