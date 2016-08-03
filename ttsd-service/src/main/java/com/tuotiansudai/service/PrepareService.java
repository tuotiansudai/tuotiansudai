package com.tuotiansudai.service;


import com.tuotiansudai.dto.ActivityRegisterRequestDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.PrepareRegisterRequestDto;
import com.tuotiansudai.dto.PrepareUserDto;

import java.util.Date;
import java.util.List;

public interface PrepareService {

    BaseDataDto prepareRegister(PrepareRegisterRequestDto requestDto);

    List<PrepareUserDto> findPrepareUser(String mobile, Date beginTime, Date endTime, int index, int pageSize);

    long findPrepareUserCount(String mobile, Date beginTime, Date endTime);

    BaseDataDto register(ActivityRegisterRequestDto requestDto);
}
