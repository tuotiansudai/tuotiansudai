package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppPointShopController extends MobileAppBaseController{

    @Autowired
    private MobileAppPointShopService mobileAppPointShopService;
}
