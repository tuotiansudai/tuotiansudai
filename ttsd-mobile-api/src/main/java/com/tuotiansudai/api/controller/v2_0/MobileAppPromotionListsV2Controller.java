package com.tuotiansudai.api.controller.v2_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.PromotionRequestDto;
import com.tuotiansudai.api.service.v2_0.MobileAppPromotionListsV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppPromotionListsV2Controller extends MobileAppBaseController{

    @Autowired
    private MobileAppPromotionListsV2Service mobileAppPromotionListsV2Service;

    @RequestMapping(value = "/get/pop-push", method = RequestMethod.POST)
    public BaseResponseDto queryPromotionLists(@RequestBody PromotionRequestDto promotionRequestDto) {
        promotionRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPromotionListsV2Service.generatePromotionList(promotionRequestDto);
    }

}
