package com.tuotiansudai.service;

import com.tuotiansudai.dto.request.RegisterRequestDto;
import com.tuotiansudai.repository.model.UserModel;

public interface RegisterUserService {

    UserModel register(RegisterRequestDto registerDto);
}
