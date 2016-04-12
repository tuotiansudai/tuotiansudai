package com.tuotiansudai.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/app/download")
public class AppDownloadController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView appDownload() {
        return new ModelAndView("/app-download");
    }
}
