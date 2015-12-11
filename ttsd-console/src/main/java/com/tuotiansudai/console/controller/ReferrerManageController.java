package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.ReferrerRewardStatus;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ReferrerManageService;
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
@RequestMapping(value = "/user-manage")
public class ReferrerManageController {

    @Autowired
    private ReferrerManageService referrerManageService;

    @RequestMapping(value = "/referrer", method = RequestMethod.GET)
    public ModelAndView referrerManage(@RequestParam(value = "referrerLoginName",required = false) String referrerLoginName,
                                        @RequestParam(value = "investLoginName",required = false) String investLoginName,
                                        @RequestParam(value = "investStartTime",required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date investStartTime,
                                        @RequestParam(value = "investEndTime",required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date investEndTime,
                                        @RequestParam(value = "level",required = false) Integer level,
                                        @RequestParam(value = "rewardStartTime",required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date rewardStartTime,
                                        @RequestParam(value = "rewardEndTime",required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date rewardEndTime,
                                        @RequestParam(value = "role",required = false) Role role,
                                        @RequestParam(value = "source",required = false) Source source,
                                        @RequestParam(value = "currentPageNo",defaultValue = "1",required = false) int currentPageNo,
                                        @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                        @RequestParam(value = "export", required = false) String export,
                                        HttpServletResponse response) throws IOException{
        DateTime investDateTime = new DateTime(investEndTime);
        DateTime rewardDateTime = new DateTime(rewardEndTime);
        if (export != null && !export.equals("")) {
            response.setCharacterEncoding("UTF-8");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("推荐人管理.csv", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/csv");
            int referrerManageCount = referrerManageService.findReferrerManageCount(referrerLoginName, investLoginName, investStartTime, investEndTime!=null?investDateTime.plusDays(1).toDate():investEndTime, level, rewardStartTime, rewardEndTime!=null?rewardDateTime.plusDays(1).toDate():rewardEndTime, role, source);
            List<ReferrerManageView> referrerManageViews = referrerManageService.findReferrerManage(referrerLoginName,investLoginName,investStartTime,investEndTime!=null?investDateTime.plusDays(1).toDate():investEndTime,level,rewardStartTime,rewardEndTime!=null?rewardDateTime.plusDays(1).toDate():rewardEndTime,role,source,1,referrerManageCount);
            List<List<String>> data = Lists.newArrayList();
            for (ReferrerManageView referrerManageView : referrerManageViews) {
                List<String> dataModel = Lists.newArrayList();
                dataModel.add(referrerManageView.getLoanName());
                dataModel.add(String.valueOf(referrerManageView.getPeriods()));
                dataModel.add(referrerManageView.getInvestLoginName());
                dataModel.add(referrerManageView.getInvestName());
                dataModel.add(String.valueOf(new BigDecimal(referrerManageView.getInvestAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue()));
                dataModel.add(new DateTime(referrerManageView.getInvestTime()).toString("yyyy-MM-dd HH:mm:ss"));
                dataModel.add(referrerManageView.getSource().name());
                dataModel.add(referrerManageView.getReferrerLoginName());
                dataModel.add(referrerManageView.getReferrerName());
                dataModel.add(referrerManageView.getRole() == Role.STAFF ? "是" : "否");
                dataModel.add(String.valueOf(referrerManageView.getLevel()));
                dataModel.add(String.valueOf(new BigDecimal(referrerManageView.getRewardAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue()));
                dataModel.add(referrerManageView.getStatus() == ReferrerRewardStatus.SUCCESS ? "已入账" : "入账失败");
                dataModel.add(new DateTime(referrerManageView.getRewardTime()).toString("yyyy-MM-dd HH:mm:ss"));
                data.add(dataModel);
            }
            ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleReferrerManageCsvHeader, data, response.getOutputStream());
            return null;
        } else {
            ModelAndView modelAndView = new ModelAndView("/referrer-manage");
            List<ReferrerManageView> referrerManageViews = referrerManageService.findReferrerManage(referrerLoginName, investLoginName, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source, currentPageNo, pageSize);
            int referrerManageCount = referrerManageService.findReferrerManageCount(referrerLoginName, investLoginName, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source);
            long investAmountSum = referrerManageService.findReferrerManageInvestAmountSum(referrerLoginName, investLoginName, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source);
            long rewardAmountSum = referrerManageService.findReferrerManageRewardAmountSum(referrerLoginName, investLoginName, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source);
            modelAndView.addObject("referrerLoginName", referrerLoginName);
            modelAndView.addObject("investLoginName", investLoginName);
            modelAndView.addObject("investStartTime", investStartTime);
            modelAndView.addObject("investEndTime", investEndTime);
            modelAndView.addObject("level", level);
            modelAndView.addObject("rewardStartTime", rewardStartTime);
            modelAndView.addObject("rewardEndTime", rewardEndTime);
            modelAndView.addObject("role", role);
            modelAndView.addObject("source", source);
            modelAndView.addObject("currentPageNo", currentPageNo);
            modelAndView.addObject("pageSize", pageSize);
            modelAndView.addObject("referrerManageViews", referrerManageViews);
            modelAndView.addObject("referrerManageCount", referrerManageCount);
            long totalPages = referrerManageCount / pageSize + (referrerManageCount % pageSize > 0 ? 1 : 0);
            boolean hasPreviousPage = currentPageNo > 1 && currentPageNo <= totalPages;
            boolean hasNextPage = currentPageNo < totalPages;
            modelAndView.addObject("hasPreviousPage", hasPreviousPage);
            modelAndView.addObject("hasNextPage", hasNextPage);
            Source[] sources = Source.values();
            modelAndView.addObject("sources",sources);
            modelAndView.addObject("investAmountSum",investAmountSum);
            modelAndView.addObject("rewardAmountSum",rewardAmountSum);
            return modelAndView;
        }
    }

}
