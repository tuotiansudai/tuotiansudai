package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;

public interface RetrievePasswordService {
    boolean mobileRetrievePassword(String mobile, String captcha, String password);
}
