package com.tuotiansudai.console.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import com.tuotiansudai.client.PayWrapperClient;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/finance-manage")
public class TransferBillController {

    private final static Logger logger = Logger.getLogger(TransferBillController.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @RequestMapping(value = "/transfer-bill", method = RequestMethod.GET)
    public ModelAndView transferBill(@RequestParam(value = "loginName", required = false) String loginName,
                                  @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                  @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        ModelAndView modelAndView = new ModelAndView("/transfer-bill", "data", Lists.newArrayList());
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("endDate", endDate);


        if (!Strings.isNullOrEmpty(loginName)) {
            Ordering<List<String>> ordering = new Ordering<List<String>>() {
                @Override
                public int compare(List<String> left, List<String> right) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        return Longs.compare(simpleDateFormat.parse(left.get(1)).getTime(), simpleDateFormat.parse(right.get(1)).getTime());
                    } catch (ParseException e) {
                        logger.error(e.getLocalizedMessage(), e);
                    }
                    return 0;
                }
            };

            List<List<String>> data = Lists.newArrayList();
            DateTime queryStartDate = new DateTime(startDate).withTimeAtStartOfDay();
            while (queryStartDate.isBefore(new DateTime(endDate).withTimeAtStartOfDay()) || queryStartDate.isEqual(new DateTime(endDate).withTimeAtStartOfDay())) {
                DateTime queryEndDate = new DateTime(endDate).withTimeAtStartOfDay();
                if (Days.daysBetween(queryStartDate, new DateTime(endDate).withTimeAtStartOfDay()).getDays() >= 30) {
                    queryEndDate = queryStartDate.plusDays(29);
                }
                List<List<String>> transferBill = payWrapperClient.getTransferBill(loginName, queryStartDate.toDate(), queryEndDate.toDate());
                data.addAll(transferBill);
                queryStartDate = queryEndDate.plusDays(1);
            }
            modelAndView.addObject("data", ordering.sortedCopy(data));
        }

        return modelAndView;
    }

}
