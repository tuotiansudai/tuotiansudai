package com.tuotiansudai.web.ask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/sitemap.txt")
public class SitemapController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView sitemap() {
        return new ModelAndView("/sitemap");
    }

}
