package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.FinanceReportDto;
import com.tuotiansudai.service.FinanceReportService;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.log4j.Logger;
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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/finance-manage")
public class FinanceReportController {

    static Logger logger = Logger.getLogger(FinanceReportController.class);

    @Autowired
    FinanceReportService financeReportService;

    @RequestMapping(value = "/financeReport", method = RequestMethod.GET)
    public ModelAndView getFinanceReport(@RequestParam(value = "loanId", defaultValue = "", required = false) Long loanId,
                                         @RequestParam(value = "period", defaultValue = "", required = false) Integer period,
                                         @RequestParam(value = "investLoginName", defaultValue = "", required = false) String investLoginName,
                                         @RequestParam(value = "investStartTime", defaultValue = "", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date investStartTime,
                                         @RequestParam(value = "investEndTime", defaultValue = "", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date investEndTime,
                                         @RequestParam(value = "index", defaultValue = "1", required = true) int index,
                                         @RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/finance-report");
        BasePaginationDataDto<FinanceReportDto> basePaginationDataDto = financeReportService.getFinanceReportDtos(loanId, period, investLoginName, investStartTime, investEndTime, index, pageSize);
        modelAndView.addObject("data", basePaginationDataDto);
        modelAndView.addObject("loanId", loanId);
        modelAndView.addObject("selectedPeriod", period);
        modelAndView.addObject("investLoginName", investLoginName);
        modelAndView.addObject("investStartTime", investStartTime);
        modelAndView.addObject("investEndTime", investEndTime);

        return modelAndView;
    }

    @RequestMapping(value = "/financeReport/export", method = RequestMethod.GET)
    public ModelAndView exportFinanceReport(@RequestParam(value = "loanId", defaultValue = "", required = false) Long loanId,
                                            @RequestParam(value = "period", defaultValue = "", required = false) Integer period,
                                            @RequestParam(value = "investLoginName", defaultValue = "", required = false) String investLoginName,
                                            @RequestParam(value = "investStartTime", defaultValue = "", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date investStartTime,
                                            @RequestParam(value = "investEndTime", defaultValue = "", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date investEndTime,
                                            @RequestParam(value = "index", defaultValue = "1", required = true) int index,
                                            @RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize,
                                            HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.FinanceReportHeader.getDescription() + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        httpServletResponse.setContentType("application/csv");

        List<List<String>> csvData = financeReportService.getFinanceReportCsvData(loanId, period, investLoginName, investStartTime, investEndTime);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.FinanceReportHeader, csvData, httpServletResponse.getOutputStream());

        ModelAndView modelAndView = new ModelAndView("/finance-report");
        BasePaginationDataDto<FinanceReportDto> basePaginationDataDto = financeReportService.getFinanceReportDtos(loanId, period, investLoginName, investStartTime, investEndTime, index, pageSize);
        modelAndView.addObject("data", basePaginationDataDto);
        modelAndView.addObject("loanId", loanId);
        modelAndView.addObject("selectedPeriod", period);
        modelAndView.addObject("investLoginName", investLoginName);
        modelAndView.addObject("investStartTime", investStartTime);
        modelAndView.addObject("investEndTime", investEndTime);

        return modelAndView;
    }
}
