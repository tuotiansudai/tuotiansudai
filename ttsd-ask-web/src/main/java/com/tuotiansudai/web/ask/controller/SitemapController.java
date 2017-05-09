package com.tuotiansudai.web.ask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(path = "/sitemap.txt")
public class SitemapController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView sitemap(HttpServletResponse response) {
        response.setHeader("Content-Type", "text/plain");
        return new ModelAndView("/sitemap");
    }

}
