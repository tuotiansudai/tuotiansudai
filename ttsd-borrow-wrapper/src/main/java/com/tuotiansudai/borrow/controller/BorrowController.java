package com.tuotiansudai.borrow.controller;

import com.tuotiansudai.borrow.dto.request.BaseRequestDto;
import com.tuotiansudai.borrow.dto.response.AuthenticationResponseDto;
import com.tuotiansudai.borrow.dto.response.BaseResponseDto;
import com.tuotiansudai.borrow.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public BaseResponseDto<AuthenticationResponseDto> isAuthentication(@RequestBody BaseRequestDto dto){
        if (!dto.isValid()){
            return new BaseResponseDto<AuthenticationResponseDto>("请求参数错误");
        }
        return borrowService.isAuthentication(dto.getMobile());
    }


    @RequestMapping(value = "/open-auto-repay", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto openAutoRepay(@RequestBody BaseRequestDto dto){
        if (!dto.isValid()){
            return new BaseResponseDto("请求参数错误");
        }
        return borrowService.openAutoRepay(dto.getMobile());
    }
}
