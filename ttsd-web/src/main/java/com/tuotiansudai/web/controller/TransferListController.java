package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.service.TransferService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/transfer-list")
public class TransferListController {

    static Logger logger = Logger.getLogger(TransferListController.class);

    @Autowired
    private TransferService transferService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView webTransferList(@RequestParam(value = "transferStatus", required = false) TransferStatus transferStatus,
                                        @RequestParam(value = "rateStart", defaultValue = "0", required = false) double rateStart,
                                        @RequestParam(value = "rateEnd", defaultValue = "0", required = false) double rateEnd,
                                        @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        int count = transferService.findCountAllTransferApplicationPaginationList(transferStatus, rateStart, rateEnd);

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> transferApplicationItemList = transferService.findAllTransferApplicationPaginationList(transferStatus, rateStart, rateEnd, index, pageSize);

        ModelAndView modelAndView = new ModelAndView("/transfer-list");
        modelAndView.addObject("count", count);
        modelAndView.addObject("transferApplicationItemList", transferApplicationItemList.getRecords());
        modelAndView.addObject("index", index);
        modelAndView.addObject("rateStart", rateStart);
        modelAndView.addObject("rateEnd", rateEnd);
        modelAndView.addObject("transferStatus", transferStatus);
        int maxIndex = count / pageSize + (count % pageSize > 0 ? 1 : 0);
        modelAndView.addObject("hasPreviousPage", index > 1 && index <= maxIndex);
        modelAndView.addObject("hasNextPage", index < maxIndex);
        return modelAndView;
    }

}
