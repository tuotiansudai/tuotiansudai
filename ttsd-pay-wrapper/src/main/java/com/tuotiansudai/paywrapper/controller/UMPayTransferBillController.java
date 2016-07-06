package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.paywrapper.service.UMPayTransferBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/transfer-bill")
public class UMPayTransferBillController {

    @Autowired
    private UMPayTransferBillService umPayTransferBillService;

    @RequestMapping(path = "/user/{loginName}/start-date/{startDate}/end-date/{endDate}", method = RequestMethod.GET)
    @ResponseBody
    public List<List<String>> getTransferBill(@PathVariable String loginName,
                                                    @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,
                                                    @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date endDate) {
        return umPayTransferBillService.getTransferBill(loginName, startDate, endDate);
    }
}
