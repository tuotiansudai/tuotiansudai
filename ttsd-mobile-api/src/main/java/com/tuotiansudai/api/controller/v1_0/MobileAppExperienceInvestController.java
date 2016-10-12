package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppExperienceInvestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
public class MobileAppExperienceInvestController extends MobileAppBaseController {

    static Logger logger = Logger.getLogger(MobileAppExperienceInvestController.class);

    @Autowired
    private MobileAppExperienceInvestService mobileAppExperienceInvestService;

    @RequestMapping(value = "/experience-invest", method = RequestMethod.POST)
    public BaseResponseDto experienceInvest(@RequestBody InvestRequestDto investRequestDto) {
        investRequestDto.getBaseParam().setUserId(getLoginName());
        logger.info(MessageFormat.format("[Experience Invest] the investRequestDto baseParam:{0} , loginName:{1} , investMoney:{2} , loanId:{3} , userCouponIds:{4}",
                investRequestDto.getBaseParam().toString(),
                investRequestDto.getUserId(),
                investRequestDto.getInvestMoney(),
                investRequestDto.getLoanId(),
                investRequestDto.getUserCouponIds()));
        return mobileAppExperienceInvestService.experienceInvest(investRequestDto);
    }

}
