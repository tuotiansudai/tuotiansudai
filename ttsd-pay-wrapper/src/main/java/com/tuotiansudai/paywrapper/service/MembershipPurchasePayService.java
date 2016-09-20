package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.dto.MembershipPurchaseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface MembershipPurchasePayService {

    BaseDto<PayFormDataDto> purchase(MembershipPurchaseDto dto);

    String purchaseCallback(Map<String, String> paramsMap, String originalQueryString);
}
