package com.tuotiansudai.api.controller;


import com.tuotiansudai.service.HTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/htracking")
public class MobleAppHTrackingController {

    @Autowired
    private HTrackingService hTrackingService;

    @ResponseBody
    @RequestMapping(value = "/pre-reg", method = RequestMethod.GET)
    public void reg(@RequestParam String uid, @RequestParam String idfa) {
        hTrackingService.save(uid, idfa);
    }

}
