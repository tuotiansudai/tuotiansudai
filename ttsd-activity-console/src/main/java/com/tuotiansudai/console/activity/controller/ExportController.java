package com.tuotiansudai.console.activity.controller;


import com.tuotiansudai.activity.dto.ActivityCategory;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.console.activity.dto.AutumnExportDto;
import com.tuotiansudai.console.activity.service.ExportService;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.log4j.Logger;
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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/activity-console/activity-manage")
public class ExportController {

    static Logger logger = Logger.getLogger(ExportController.class);

    @Autowired
    private ExportService exportService;

    @RequestMapping(value = "/autumn-list", method = RequestMethod.GET)
    public ModelAndView autumnList() {
        ModelAndView modelAndView = new ModelAndView("/autumn-list");
        return modelAndView;
    }

    @RequestMapping(value = "/export-autumn-list", method = RequestMethod.GET)
    public void autumnActivityListExport(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.AutumnActivityList.getDescription() + new DateTime().toString("yyyyMMddHHmmSS") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //logger.error(e.getLocalizedMessage(), e);
        }
        response.setContentType("application/csv");

        List<AutumnExportDto> autumnExportDtos = exportService.getAutumnExport();

        List<List<String>> csvData = exportService.buildAutumnList(autumnExportDtos);

        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.AutumnActivityList, csvData, response.getOutputStream());
    }

    @RequestMapping(value = "/export-iphone7-lottery-stat", method = RequestMethod.GET)
    public void iphone7LotteryStatExport(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.Iphone7LotteryStatHeader.getDescription() + new DateTime().toString("yyyyMMddHHmmSS") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //logger.error(e.getLocalizedMessage(), e);
        }
        response.setContentType("application/csv");

        List<List<String>> csvData = exportService.iphone7LotteryStat();

        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.Iphone7LotteryStatHeader, csvData, response.getOutputStream());
    }

    @RequestMapping(value = "/export-prize-record", method = RequestMethod.GET)
    public void prizeRecordExport(@RequestParam(name = "mobile", required = false) String mobile,
                                  @RequestParam(name = "selectPrize", required = false) LotteryPrize lotteryPrize,
                                  @RequestParam(name = "prizeType", required = false ,defaultValue = "AUTUMN_PRIZE") ActivityCategory activityCategory,
                                  @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                  @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                  HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.LotteryPrizeHeader.getDescription() + new DateTime().toString("yyyyMMddHHmmSS") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //logger.error(e.getLocalizedMessage(), e);
        }
        response.setContentType("application/csv");

        List<List<String>> csvData = exportService.buildPrizeList(mobile, lotteryPrize, activityCategory, startTime, endTime);

        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.LotteryPrizeHeader, csvData, response.getOutputStream());
    }
}
