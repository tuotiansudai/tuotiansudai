package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.UserBillDetailListRequestDto;
import com.tuotiansudai.api.dto.UserBillRecordResponseDataDto;

import java.util.List;

public interface MobileAppUserBillListService {
    BaseResponseDto queryUserBillList(UserBillDetailListRequestDto userBillDetailListRequestDto);
}
