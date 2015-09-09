package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RetrievePasswordDto;

public interface RetrievePasswordService {
    boolean mobileRetrievePassword(RetrievePasswordDto retrievePasswordDto);
}
