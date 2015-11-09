package com.ttsd.api.service;


import com.esoft.archer.user.model.UserBill;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.UserBillDetailListRequestDto;
import com.ttsd.api.dto.UserBillRecordResponseDataDto;

import java.util.List;

public interface MobileAppUserBillListService {
    BaseResponseDto queryUserBillList(UserBillDetailListRequestDto userBillDetailListRequestDto);

    List<UserBillRecordResponseDataDto> convertUserBillRecordDto(List<UserBill> userBillList);
}
