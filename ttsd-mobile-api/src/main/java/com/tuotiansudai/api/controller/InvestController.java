package com.tuotiansudai.api.controller;

import com.tuotiansudai.service.InvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvestController {

    @Autowired
    private InvestService investService;

}
