package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.RechargePaginationItemDataDto;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.service.RechargeService;
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
public class RechargeController {

    @Autowired
    RechargeService rechargeService;

    @Autowired
    private RechargeMapper rechargeMapper;

    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public ModelAndView getRechargeList(@RequestParam(value = "rechargeId", required = false) String rechargeId,
                                        @RequestParam(value = "loginName", required = false) String loginName,
                                        @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime,
                                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                        @RequestParam(value = "status", required = false) RechargeStatus status,
                                        @RequestParam(value = "source", required = false) RechargeSource source,
                                        @RequestParam(value = "channel", required = false) String channel,
                                        @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                        @RequestParam(value = "export", required = false) String export,
                                        HttpServletResponse response) throws IOException{
        if (export != null && !export.equals("")) {
            response.setCharacterEncoding("UTF-8");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("充值记录.csv", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/csv");
            int count = rechargeMapper.findRechargeCount(rechargeId, loginName, source, status, channel, startTime, endTime);
            BaseDto<BasePaginationDataDto> baseDto = rechargeService.findRechargePagination(rechargeId, loginName, source, status, channel, 1, count, startTime, endTime);
            List<List<String>> data = Lists.newArrayList();
            List<RechargePaginationItemDataDto> rechargePaginationItemDataDtos = baseDto.getData().getRecords();
            for (int i = 0 ;i < rechargePaginationItemDataDtos.size(); i++) {
                List<String> dataModel = Lists.newArrayList();
                dataModel.add(new BigDecimal(rechargePaginationItemDataDtos.get(i).getRechargeId()).toString());
                dataModel.add(new DateTime(rechargePaginationItemDataDtos.get(i).getCreatedTime()).toString("yyyy-MM-dd HH:mm"));
                dataModel.add(rechargePaginationItemDataDtos.get(i).getLoginName());
                dataModel.add(rechargePaginationItemDataDtos.get(i).isStaff()?"是":"否");
                dataModel.add(rechargePaginationItemDataDtos.get(i).getUserName());
                dataModel.add(rechargePaginationItemDataDtos.get(i).getMobile());
                dataModel.add(rechargePaginationItemDataDtos.get(i).getAmount());
                dataModel.add(rechargePaginationItemDataDtos.get(i).getFee());
                dataModel.add(rechargePaginationItemDataDtos.get(i).getBankCode());
                dataModel.add(rechargePaginationItemDataDtos.get(i).isFastPay()?"是":"否");
                dataModel.add(rechargePaginationItemDataDtos.get(i).getStatus());
                dataModel.add(rechargePaginationItemDataDtos.get(i).getSource().name());
                dataModel.add(rechargePaginationItemDataDtos.get(i).getChannel());
                data.add(dataModel);
            }
            ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleRecharge, data, response.getOutputStream());
            return null;
        } else {
            ModelAndView modelAndView = new ModelAndView("/recharge");
            BaseDto<BasePaginationDataDto> baseDto = rechargeService.findRechargePagination(rechargeId,
                    loginName,
                    source,
                    status,
                    channel,
                    index,
                    pageSize,
                    startTime,
                    endTime);
            List<String> channelList = rechargeService.findAllChannel();

            long sumAmount = rechargeService.findSumRechargeAmount(rechargeId, loginName, source, status, channel, startTime, endTime);
            modelAndView.addObject("baseDto", baseDto);
            modelAndView.addObject("sumAmount", sumAmount);
            modelAndView.addObject("rechargeStatusList", Lists.newArrayList(RechargeStatus.values()));
            modelAndView.addObject("rechargeSourceList", Lists.newArrayList(RechargeSource.values()));
            modelAndView.addObject("rechargeChannelList", channelList);
            modelAndView.addObject("index", index);
            modelAndView.addObject("pageSize", pageSize);
            modelAndView.addObject("rechargeId", rechargeId);
            modelAndView.addObject("loginName", loginName);
            modelAndView.addObject("startTime", startTime);
            modelAndView.addObject("endTime", endTime);
            modelAndView.addObject("source", source);
            modelAndView.addObject("status", status);
            modelAndView.addObject("channel", channel);
            if (status != null) {
                modelAndView.addObject("rechargeStatus", status);
            }
            return modelAndView;
        }
    }
}
