package com.tuotiansudai.api.service.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.PromotionListResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.PromotionRequestDto;

public interface MobileAppPromotionListsV2Service {

    BaseResponseDto<PromotionListResponseDataDto> generatePromotionList(PromotionRequestDto promotionRequestDto);

}
