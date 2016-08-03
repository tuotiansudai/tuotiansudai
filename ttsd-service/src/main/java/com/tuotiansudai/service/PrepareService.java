package com.tuotiansudai.service;


import com.tuotiansudai.dto.ActivityRegisterRequestDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.PrepareRegisterRequestDto;

public interface PrepareService {

    BaseDataDto prepareRegister(PrepareRegisterRequestDto requestDto);

    BaseDataDto register(ActivityRegisterRequestDto requestDto);
}
