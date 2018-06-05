package com.tuotiansudai.api.controller.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "债权转让")
public class MobileAppTransferController extends MobileAppBaseController {

    @Autowired
    private MobileAppTransferService mobileAppTransferService;

    @RequestMapping(value = "/get/transferee", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("债权转让承接人列表")
    public BaseResponseDto<TransferTransfereeResponseDataDto> getTransferee(@RequestBody TransferTransfereeRequestDto transferTransfereeRequestDto) {
        return mobileAppTransferService.getTransferee(transferTransfereeRequestDto);
    }

    @RequestMapping(value = "/transfer-purchase", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("验密债权购买")
    public BaseResponseDto<InvestResponseDataDto> transferPurchase(@RequestBody TransferPurchaseRequestDto transferPurchaseRequestDto) {
        return mobileAppTransferService.transferPurchase(transferPurchaseRequestDto);
    }

}
