package com.tuotiansudai.api.controller.v3_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v3_0.LoanListResponseDataDto;
import com.tuotiansudai.api.service.v3_0.MobileAppLoanListV3Service;
import com.tuotiansudai.spring.LoginUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
@Api(description = "V3.0首页")
public class MobileAppHomeV3Controller extends MobileAppBaseController {

    static Logger logger = Logger.getLogger(MobileAppHomeV3Controller.class);

    @Autowired
    private MobileAppLoanListV3Service mobileAppLoanListV3Service;

    @RequestMapping(value = "/get/index", method = RequestMethod.POST)
    @ApiOperation("首页")
    public BaseResponseDto<LoanListResponseDataDto> index() {
        logger.info(MessageFormat.format("generateIndexLoan3 loginName:{0}", getLoginName()));
        logger.info(MessageFormat.format("generateIndexLoan3 mobile:{0}", LoginUserInfo.getMobile()));
        logger.info(MessageFormat.format("generateIndexLoan3 token:{0}", LoginUserInfo.getToken()));
        return mobileAppLoanListV3Service.generateIndexLoan(getLoginName());
    }
}
