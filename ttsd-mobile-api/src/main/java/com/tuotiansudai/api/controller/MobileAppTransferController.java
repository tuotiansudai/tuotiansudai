package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.TransferTransfereeRequestDto;
import com.tuotiansudai.api.service.MobileAppTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MobileAppTransferController extends MobileAppBaseController {

    @Autowired
    private MobileAppTransferService mobileAppTransferService;

    @RequestMapping(value = "/get/transferee", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto getTransferee(@RequestBody TransferTransfereeRequestDto transferTransfereeRequestDto) {
        return mobileAppTransferService.getTransferee(transferTransfereeRequestDto);
    }

}
