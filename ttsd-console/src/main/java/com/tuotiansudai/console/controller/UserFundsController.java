package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.repository.model.UserBillPaginationView;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/finance-manage")
public class UserFundsController {

    @Autowired
    private UserBillService userBillService;

    @RequestMapping(value = "/user-funds", method = RequestMethod.GET)
    public ModelAndView userFunds(@RequestParam(value = "userBillBusinessType", required = false) UserBillBusinessType userBillBusinessType,
                                  @RequestParam(value = "userBillOperationType", required = false) UserBillOperationType userBillOperationType,
                                  @RequestParam(value = "loginName", required = false) String loginName,
                                  @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                  @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                  @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                  @RequestParam(value = "export", required = false) String export,
                                  HttpServletResponse response) throws IOException {
        if (export != null && !export.equals("")) {
            response.setCharacterEncoding("UTF-8");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("用户资金查询.csv", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/csv");
            int userFundsCount = userBillService.findUserFundsCount(userBillBusinessType, userBillOperationType, loginName, startTime, endTime);
            List<UserBillPaginationView> userBillModels = userBillService.findUserFunds(userBillBusinessType, userBillOperationType, loginName, startTime, endTime, 1, userFundsCount);
            List<List<String>> data = Lists.newArrayList();
            for (UserBillPaginationView userBillView : userBillModels) {
                List<String> dataModel = Lists.newArrayList();
                DateTime dateTime = new DateTime(userBillView.getCreatedTime());
                dataModel.add(dateTime != null ? dateTime.toString("yyyy-MM-dd HH:mm:ss") : "");
                dataModel.add(String.valueOf(userBillView.getId()));
                dataModel.add(userBillView.getLoginName());
                dataModel.add(userBillView.isStaff() ? "是" : "否");
                dataModel.add(userBillView.getUserName());
                dataModel.add(userBillView.getMobile());
                dataModel.add(userBillView.getOperationType().getDescription());
                dataModel.add(userBillView.getBusinessType().getDescription());
                dataModel.add(String.valueOf(new BigDecimal(userBillView.getAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue()));
                dataModel.add(String.valueOf(new BigDecimal(userBillView.getBalance()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue()));
                dataModel.add(String.valueOf(new BigDecimal(userBillView.getFreeze()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue()));
                data.add(dataModel);
            }
            ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleUserFundsCsvHeader, data, response.getOutputStream());
            return null;
        } else {
            ModelAndView modelAndView = new ModelAndView("/user-funds");
            List<UserBillPaginationView> userBillModels = userBillService.findUserFunds(userBillBusinessType, userBillOperationType, loginName, startTime, endTime, index, pageSize);
            int userFundsCount = userBillService.findUserFundsCount(userBillBusinessType, userBillOperationType, loginName, startTime, endTime);
            modelAndView.addObject("loginName", loginName);
            modelAndView.addObject("startTime", startTime);
            modelAndView.addObject("endTime", endTime);
            modelAndView.addObject("userBillBusinessType", userBillBusinessType);
            modelAndView.addObject("userBillOperationType", userBillOperationType);
            modelAndView.addObject("index", index);
            modelAndView.addObject("pageSize", pageSize);
            modelAndView.addObject("userBillModels", userBillModels);
            modelAndView.addObject("userFundsCount", userFundsCount);
            modelAndView.addObject("businessTypeList", UserBillBusinessType.values());
            modelAndView.addObject("operationTypeList", UserBillOperationType.values());
            long totalPages = userFundsCount / pageSize + (userFundsCount % pageSize > 0 ? 1 : 0);
            boolean hasPreviousPage = index > 1 && index <= totalPages;
            boolean hasNextPage = index < totalPages;
            modelAndView.addObject("hasPreviousPage", hasPreviousPage);
            modelAndView.addObject("hasNextPage", hasNextPage);
            return modelAndView;
        }
    }

}
