package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.WithdrawPaginationItemDataDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawStatus;
import com.tuotiansudai.service.WithdrawService;
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
@RequestMapping(value = "/finance-manage", method = RequestMethod.GET)
public class WithdrawController {

    @Autowired
    WithdrawService withdrawService;

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public ModelAndView getWithdrawList(@RequestParam(value = "withdrawId", required = false) String withdrawId,
                                        @RequestParam(value = "loginName", required = false) String loginName,
                                        @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime,
                                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                        @RequestParam(value = "status", required = false) WithdrawStatus status,
                                        @RequestParam(value = "source", required = false) Source source,
                                        @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                        @RequestParam(value = "export", required = false) String export,
                                        HttpServletResponse response) throws IOException {

        if (export != null && !export.equals("")) {
            response.setCharacterEncoding("UTF-8");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("提现记录.csv", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/csv");
            int count = withdrawService.findWithdrawCount(withdrawId, loginName, status, source, startTime, endTime);
            BaseDto<BasePaginationDataDto> baseDto = withdrawService.findWithdrawPagination(withdrawId, loginName, status, source, 1, count, startTime, endTime);
            List<List<String>> data = Lists.newArrayList();
            List<WithdrawPaginationItemDataDto> withdrawPaginationItemDataDtos = baseDto.getData().getRecords();
            for (int i = 0; i < withdrawPaginationItemDataDtos.size(); i++) {
                List<String> dataModel = Lists.newArrayList();
                dataModel.add(new BigDecimal(withdrawPaginationItemDataDtos.get(i).getWithdrawId()).toString());
                dataModel.add(new DateTime(withdrawPaginationItemDataDtos.get(i).getCreatedTime()).toString("yyyy-MM-dd HH:mm"));
                dataModel.add(new DateTime(withdrawPaginationItemDataDtos.get(i).getApplyNotifyTime()).toString("yyyy-MM-dd HH:mm"));
                dataModel.add(new DateTime(withdrawPaginationItemDataDtos.get(i).getNotifyTime()).toString("yyyy-MM-dd HH:mm"));
                dataModel.add(withdrawPaginationItemDataDtos.get(i).getLoginName());
                dataModel.add(withdrawPaginationItemDataDtos.get(i).isStaff() ? "是" : "否");
                dataModel.add(withdrawPaginationItemDataDtos.get(i).getUserName());
                dataModel.add(withdrawPaginationItemDataDtos.get(i).getMobile());
                dataModel.add(withdrawPaginationItemDataDtos.get(i).getAmount());
                dataModel.add(withdrawPaginationItemDataDtos.get(i).getFee());
                dataModel.add(withdrawPaginationItemDataDtos.get(i).getBankCard());
                dataModel.add(withdrawPaginationItemDataDtos.get(i).getStatus());
                dataModel.add(withdrawPaginationItemDataDtos.get(i).getSource().name());
                data.add(dataModel);
            }
            ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleWithdraw, data, response.getOutputStream());
            return null;
        } else {
            ModelAndView modelAndView = new ModelAndView("/withdraw");
            BaseDto<BasePaginationDataDto> baseDto = withdrawService.findWithdrawPagination(withdrawId, loginName, status, source, index, pageSize, startTime, endTime);

            long sumAmount = withdrawService.findSumWithdrawAmount(withdrawId, loginName, status, source, startTime, endTime);

            long sumFee = withdrawService.findSumWithdrawFee(withdrawId, loginName, status, source, startTime, endTime);

            modelAndView.addObject("baseDto", baseDto);
            modelAndView.addObject("sumAmount", sumAmount);
            modelAndView.addObject("sumFee", sumFee);
            modelAndView.addObject("withdrawStatusList", Lists.newArrayList(WithdrawStatus.values()));
            modelAndView.addObject("index", index);
            modelAndView.addObject("pageSize", pageSize);
            modelAndView.addObject("withdrawId", withdrawId);
            modelAndView.addObject("loginName", loginName);
            modelAndView.addObject("startTime", startTime);
            modelAndView.addObject("endTime", endTime);
            modelAndView.addObject("status", status);
            modelAndView.addObject("source", source);
            modelAndView.addObject("withdrawSourceList", Source.values());
            if (status != null) {
                modelAndView.addObject("withdrawStatus", status);
            }
            return modelAndView;
        }
    }
}
