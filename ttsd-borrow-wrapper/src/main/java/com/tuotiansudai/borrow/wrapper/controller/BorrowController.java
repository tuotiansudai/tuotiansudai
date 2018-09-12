package com.tuotiansudai.borrow.wrapper.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/huizu")
public class BorrowController {

    @ResponseBody
    public boolean text(){
        return true;
    }
}
