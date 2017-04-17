package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.service.HTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/htracking")
public class HTrackingController {

    @Autowired
    private HTrackingService hTrackingService;

    @ResponseBody
    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public BaseDto callback(@RequestParam String uid, @RequestParam String idfa) {
        return hTrackingService.save(uid, idfa);
    }

}
