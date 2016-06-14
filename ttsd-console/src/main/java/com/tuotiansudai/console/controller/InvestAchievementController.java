package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.InvestAchievementService;
import com.tuotiansudai.repository.model.LoanAchievementView;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping(value = "/activity-manage")
public class InvestAchievementController {

    static Logger logger = Logger.getLogger(InvestAchievementController.class);

    @Autowired
    private InvestAchievementService investAchievementService;

    @RequestMapping(value = "/invest-achievement", method = RequestMethod.GET)
    public ModelAndView investAchievement(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                          @RequestParam(value = "loginName", required = false) String loginName,
                                          @RequestParam(value = "export", required = false) String export,
                                          HttpServletResponse response) throws IOException {
        if (export != null && !export.equals("")) {
            response.setCharacterEncoding("UTF-8");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("投资称号管理.csv", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            response.setContentType("application/csv");
            List<List<String>> data = Lists.newArrayList();
            long investAchievementCount = investAchievementService.findInvestAchievementCount(loginName);
            List<LoanAchievementView> loanAchievementViews = investAchievementService.findInvestAchievement(1, new Long(investAchievementCount).intValue(), loginName);
            for (LoanAchievementView loanAchievementView : loanAchievementViews) {
                List<String> dataModel = Lists.newArrayList();
                dataModel.add(loanAchievementView.getName());
                dataModel.add(loanAchievementView.getLoanStatus().getDescription());
                dataModel.add(String.valueOf(loanAchievementView.getPeriods()));
                dataModel.add(AmountConverter.convertCentToString(loanAchievementView.getLoanAmount()));
                if (loanAchievementView.getMaxAmountLoginName() != null) {
                    dataModel.add(loanAchievementView.getMaxAmountLoginName() + "/" + AmountConverter.convertCentToString(loanAchievementView.getMaxAmount()));
                } else {
                    dataModel.add("/");
                }
                if (loanAchievementView.getFirstInvestLoginName() != null) {
                    dataModel.add(loanAchievementView.getFirstInvestLoginName() + "/" + AmountConverter.convertCentToString(loanAchievementView.getFirstInvestAmount()));
                } else {
                    dataModel.add("/");
                }
                if (loanAchievementView.getLastInvestLoginName() != null) {
                    dataModel.add(loanAchievementView.getLastInvestLoginName() + "/" + AmountConverter.convertCentToString(loanAchievementView.getLastInvestAmount()));
                } else {
                    dataModel.add("/");
                }
                dataModel.add(loanAchievementView.getRaisingCompleteTime() != null ? new DateTime(loanAchievementView.getRaisingCompleteTime()).toString("yyyy-MM-dd HH:mm:ss") : "");
                dataModel.add(loanAchievementView.getFirstInvestDuration());
                dataModel.add(loanAchievementView.getCompleteInvestDuration());
                data.add(dataModel);
            }
            ExportCsvUtil.createCsvOutputStream(CsvHeaderType.InvestAchievementHeader, data, response.getOutputStream());
            return null;
        } else {
            List<LoanAchievementView> loanAchievementViews = investAchievementService.findInvestAchievement(index, pageSize, loginName);
            ModelAndView modelAndView = new ModelAndView("/invest-achievement");
            modelAndView.addObject("loanAchievementViews", loanAchievementViews);
            modelAndView.addObject("index", index);
            modelAndView.addObject("pageSize", pageSize);
            modelAndView.addObject("loginName", loginName);
            long investAchievementCount = investAchievementService.findInvestAchievementCount(loginName);
            long totalPages = investAchievementCount / pageSize + (investAchievementCount % pageSize > 0 ? 1 : 0);
            boolean hasPreviousPage = index > 1 && index <= totalPages;
            boolean hasNextPage = index < totalPages;
            modelAndView.addObject("hasPreviousPage", hasPreviousPage);
            modelAndView.addObject("hasNextPage", hasNextPage);
            modelAndView.addObject("investAchievementCount", investAchievementCount);
            return modelAndView;
        }
    }

}
