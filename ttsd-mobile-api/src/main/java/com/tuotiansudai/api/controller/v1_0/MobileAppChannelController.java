package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.DomobNotifyResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppChannelServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController()
public class MobileAppChannelController {
    static Logger log = Logger.getLogger(MobileAppChannelController.class);

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @RequestMapping(value = "/ad/domob", method = RequestMethod.GET)
    public Object clickNotifyDomob(HttpServletRequest request) {
        if (log.isInfoEnabled()) {
            log.info("receive domob notify request:" + request.getRequestURI() + " " + request.getQueryString());
        }
        boolean recordSuccess;
        String message;
        if (MobileAppChannelServiceImpl.MOBILE_APP_ID
                .equalsIgnoreCase(request.getParameter("appkey"))) {
            recordSuccess = recordChannelDomob(request.getParameter("mac"),
                    request.getParameter("macmd5"),
                    request.getParameter("ifa"),
                    request.getParameter("ifamd5"),
                    request.getParameter("source"));
            message = recordSuccess ? "ok" : "exists";
        } else {
            recordSuccess = false;
            message = "invalid appkey";
        }
        if (log.isInfoEnabled()) {
            log.info("send response to domob :" + message);
        }

        return new DomobNotifyResponseDto(recordSuccess, message);
    }

    @RequestMapping(value = "/v1.0/install-notify", method = RequestMethod.POST)
    public BaseResponseDto installNotify(@RequestBody BaseParamDto paramDto) {
        mobileAppChannelService.sendInstallNotify(paramDto.getBaseParam());
        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }

    public boolean recordChannelDomob(String mac, String macmd5, String ifa, String ifamd5, String subChannel) {
        String channel = "domob";
        boolean r1 = mobileAppChannelService.recordDeviceId("mac", mac, channel, subChannel);
        boolean r2 = mobileAppChannelService.recordDeviceId("macmd5", macmd5, channel, subChannel);
        boolean r3 = mobileAppChannelService.recordDeviceId("ifa", ifa, channel, subChannel);
        boolean r4 = mobileAppChannelService.recordDeviceId("ifamd5", ifamd5, channel, subChannel);
        return (r1 || r2 || r3 || r4);
    }
}
