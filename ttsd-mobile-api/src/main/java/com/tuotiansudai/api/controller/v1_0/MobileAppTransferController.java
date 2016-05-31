package com.tuotiansudai.api.controller.v1_0;


import com.tuotiansudai.api.dto.v1_0.TransferPurchaseRequestDto;
import com.tuotiansudai.api.dto.v1_0.TransferTransfereeRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppTransferService;
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

    @RequestMapping(value = "/transfer-purchase", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto transferPurchase(@RequestBody TransferPurchaseRequestDto transferPurchaseRequestDto) {
        return mobileAppTransferService.transferPurchase(transferPurchaseRequestDto);
    }

    @RequestMapping(value = "/transfer-no-password-purchase", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto transferNoPasswordPurchase(@RequestBody TransferPurchaseRequestDto transferPurchaseRequestDto) {
        return mobileAppTransferService.transferNoPasswordPurchase(transferPurchaseRequestDto);
    }

}
