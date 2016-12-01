package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppTransferApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(description = "债权转让")
public class MobileAppTransferApplicationController extends MobileAppBaseController {

    @Autowired
    private MobileAppTransferApplicationService mobileAppTransferApplicationService;

    @RequestMapping(value = "/get/transferrer-transfer-application-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("我的债权转让记录")
    public BaseResponseDto<TransferApplicationResponseDataDto> generateTransferApplication(@RequestBody TransferApplicationRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplicationService.generateTransferApplication(requestDto);
    }

    @RequestMapping(value = "/get/transferee-transfer-application-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("我的承接记录")
    public BaseResponseDto<TransferApplicationResponseDataDto> generateTransfereeApplication(@RequestBody PaginationRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplicationService.generateTransfereeApplication(requestDto);
    }

        @RequestMapping(value = "/get/transfer-apply", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("申请转让查询")
    public BaseResponseDto<TransferApplyQueryResponseDataDto> transferApplyQuery(@Valid @RequestBody TransferApplyQueryRequestDto requestDto,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            requestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppTransferApplicationService.transferApplyQuery(requestDto);
        }
    }

    @RequestMapping(value = "/transfer-apply", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("申请转让")
    public BaseResponseDto generateAgreementRequest(@Valid @RequestBody TransferApplyRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            requestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppTransferApplicationService.transferApply(requestDto);
        }
    }

    @RequestMapping(value = "/get/transfer-cancel", method = RequestMethod.POST)
    @ApiOperation("取消债权申请")
    public BaseResponseDto transferApplicationCancel(@RequestBody TransferCancelRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplicationService.transferApplicationCancel(requestDto);
    }

    @RequestMapping(value = "/get/transfer-purchase", method = RequestMethod.POST)
    @ApiOperation("债权购买利息查询")
    public BaseResponseDto<TransferPurchaseResponseDataDto> transferPurchase(@RequestBody TransferPurchaseRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplicationService.transferPurchase(requestDto);
    }

    @RequestMapping(value = "/get/transfer-application-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("债权转让列表")
    public BaseResponseDto<TransferApplicationResponseDataDto> transferApplicationList(@RequestBody TransferApplicationListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplicationService.transferApplicationList(requestDto);
    }

    @RequestMapping(value = "/get/transfer-application", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("债权转让详情")
    public BaseResponseDto<TransferApplicationDetailResponseDataDto> transferApplicationDetail(@RequestBody TransferApplicationDetailRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplicationService.transferApplicationById(requestDto);
    }

    @RequestMapping(value = "/get/user-transfee-repay", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("承接人回款计划")
    public BaseResponseDto<UserInvestRepayResponseDataDto> getTransferLoanRepay(@RequestBody UserInvestRepayRequestDto userInvestRepayRequestDto) {
        return mobileAppTransferApplicationService.userInvestRepay(userInvestRepayRequestDto);
    }
}
