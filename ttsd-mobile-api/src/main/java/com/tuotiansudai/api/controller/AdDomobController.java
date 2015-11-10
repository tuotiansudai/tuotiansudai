package com.tuotiansudai.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/ad")
public class AdDomobController {

    @RequestMapping(value = "/domob", method = RequestMethod.GET)
    public String clickNotify(HttpServletRequest request, HttpServletResponse response) {
        return "{\"message\":\"\", \"success\":true}";
    }
}
