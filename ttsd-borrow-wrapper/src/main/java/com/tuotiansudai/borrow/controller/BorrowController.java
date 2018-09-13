package com.tuotiansudai.borrow.controller;

import com.tuotiansudai.borrow.dto.request.BaseRequestDto;
import com.tuotiansudai.borrow.dto.response.AuthenticationResponseDto;
import com.tuotiansudai.borrow.dto.response.BaseResponseDto;
import com.tuotiansudai.borrow.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService){
        this.borrowService = borrowService;
    }

    @RequestMapping(value = "/is-authentication", method = RequestMethod.POST)
    @ResponseBody
    public AuthenticationResponseDto isAuthentication(@RequestBody BaseRequestDto dto){
        return borrowService.isAuthentication(dto.getMobile());
    }
}
