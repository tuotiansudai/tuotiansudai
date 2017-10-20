package com.tuotiansudai.service;

import com.tuotiansudai.dto.request.RegisterRequestDto;

public interface RegisterUserService {

    boolean register(RegisterRequestDto registerDto);
}
