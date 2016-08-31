package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;

public interface MobileAppPointShopService {

    BaseResponseDto updateUserAddress(String loginName,String contract,String mobile,String address);


}
