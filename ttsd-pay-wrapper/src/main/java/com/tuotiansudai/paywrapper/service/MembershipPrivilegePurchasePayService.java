package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.dto.MembershipPrivilegePurchaseDto;

import java.util.Map;

public interface MembershipPrivilegePurchasePayService {

    BaseDto<PayFormDataDto> purchase(MembershipPrivilegePurchaseDto dto);

    String purchaseCallback(Map<String, String> paramsMap, String originalQueryString);
}
