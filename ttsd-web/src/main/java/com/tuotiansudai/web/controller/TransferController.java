package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@Controller
@RequestMapping(value = "/transferrer")
public class TransferController {

    static Logger logger = Logger.getLogger(TransferController.class);

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private InvestService investService;

    @RequestMapping(value = "/transfer-application-list/{transferStatus}",method = RequestMethod.GET)
    public ModelAndView getTransferrerTransferApplicationList(@PathVariable String transferStatus){
        ModelAndView modelAndView = new ModelAndView();
        if(TransferStatus.TRANSFERRING == TransferStatus.valueOf(transferStatus.toUpperCase())){
            modelAndView.setViewName("/create-transfer-transferring");
        }else if(TransferStatus.SUCCESS == TransferStatus.valueOf(transferStatus.toUpperCase())){
            modelAndView.setViewName("/create-transfer-record");
        }else{
            modelAndView.setViewName("/create-transfer-transferable");
        }
        return modelAndView;
    }


    @RequestMapping(value = "/transfer-application-list-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> transferrerListData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                         @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                         @RequestParam(name = "status", required = false) List<TransferStatus> statusList) {
        String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto dataDto;
        if(CollectionUtils.isNotEmpty(statusList) && statusList.contains(TransferStatus.TRANSFERABLE)){
            dataDto = investService.getInvestPagination(loginName, index, pageSize, null, null, LoanStatus.REPAYING);
        }else{
            dataDto = investTransferService.findWebTransferApplicationPaginationList(loginName,statusList,index,pageSize);
        }

        dataDto.setStatus(true);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }

}
