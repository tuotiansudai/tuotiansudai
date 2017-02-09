package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ConsoleBookingLoanService;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/booking-loan-manage")
public class BookingLoanController {

    static Logger logger = Logger.getLogger(BookingLoanController.class);

    @Autowired
    private ConsoleBookingLoanService consoleBookingLoanService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView bookingLoanList(@RequestParam(value = "productType", required = false) ProductType productType,
                                        @RequestParam(value = "bookingTimeStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingTimeStartTime,
                                        @RequestParam(value = "bookingTimeEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingTimeEndTime,
                                        @RequestParam(value = "mobile", required = false) String mobile,
                                        @RequestParam(value = "noticeTimeStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date noticeTimeStartTime,
                                        @RequestParam(value = "noticeTimeEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date noticeTimeEndTime,
                                        @RequestParam(value = "source", required = false) Source source,
                                        @RequestParam(value = "status", required = false) Boolean status,
                                        @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        int pageSize = 10;
        ModelAndView mv = new ModelAndView("/booking-loan-list");
        mv.addObject("bookingLoan", consoleBookingLoanService.bookingLoanList(productType, bookingTimeStartTime, bookingTimeEndTime,
                mobile, noticeTimeStartTime, noticeTimeEndTime, source, status, index, pageSize));
        mv.addObject("productType", productType);
        mv.addObject("bookingLoanSumList", consoleBookingLoanService.findBookingLoanSumAmountByProductType(productType,
                bookingTimeStartTime, bookingTimeEndTime, mobile, noticeTimeStartTime, noticeTimeEndTime, source, status));
        mv.addObject("productTypeList", Lists.newArrayList(ProductType._180, ProductType._90, ProductType._360));
        mv.addObject("bookingTimeStartTime", bookingTimeStartTime);
        mv.addObject("bookingTimeEndTime", bookingTimeEndTime);
        mv.addObject("mobile", mobile);
        mv.addObject("noticeTimeStartTime", noticeTimeStartTime);
        mv.addObject("noticeTimeEndTime", noticeTimeEndTime);
        mv.addObject("source", source);
        mv.addObject("sourceList", Lists.newArrayList(Source.values()));
        mv.addObject("status", status);
        mv.addObject("index", index);
        mv.addObject("pageSize", pageSize);
        return mv;
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public ModelAndView exportBookingLoanList(@RequestParam(value = "productType", required = false) ProductType productType,
                                              @RequestParam(value = "bookingTimeStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingTimeStartTime,
                                              @RequestParam(value = "bookingTimeEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingTimeEndTime,
                                              @RequestParam(value = "mobile", required = false) String mobile,
                                              @RequestParam(value = "noticeTimeStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date noticeTimeStartTime,
                                              @RequestParam(value = "noticeTimeEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date noticeTimeEndTime,
                                              @RequestParam(value = "source", required = false) Source source,
                                              @RequestParam(value = "status", required = false) Boolean status,
                                              @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                              HttpServletResponse httpServletResponse) throws IOException {
        int pageSize = 10;
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.BookingLoanHeader.getDescription() + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        httpServletResponse.setContentType("application/csv");
        List<List<String>> csvData = consoleBookingLoanService.getBookingLoanReportCsvData(productType, bookingTimeStartTime, bookingTimeEndTime, mobile, noticeTimeStartTime, noticeTimeEndTime, source, status);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.BookingLoanHeader, csvData, httpServletResponse.getOutputStream());
        ModelAndView mv = new ModelAndView("/booking-loan-list");
        mv.addObject("bookingLoan", consoleBookingLoanService.bookingLoanList(productType, bookingTimeStartTime, bookingTimeEndTime, mobile, noticeTimeStartTime, noticeTimeEndTime, source, status, index, pageSize));
        mv.addObject("productType", productType);
        mv.addObject("productTypeList", Lists.newArrayList(ProductType._180, ProductType._90, ProductType._360));
        mv.addObject("bookingTimeStartTime", bookingTimeStartTime);
        mv.addObject("bookingTimeEndTime", bookingTimeEndTime);
        mv.addObject("mobile", mobile);
        mv.addObject("noticeTimeStartTime", noticeTimeStartTime);
        mv.addObject("noticeTimeEndTime", noticeTimeEndTime);
        mv.addObject("source", source);
        mv.addObject("sourceList", Lists.newArrayList(Source.values()));
        mv.addObject("status", status);
        mv.addObject("index", index);
        mv.addObject("pageSize", pageSize);
        return mv;
    }

    @RequestMapping(value = "/{bookingLoanId}/notice", method = RequestMethod.GET)
    public String noticeBookingLoan(@PathVariable long bookingLoanId) {
        consoleBookingLoanService.noticeBookingLoan(bookingLoanId);
        return "redirect:/booking-loan-manage/list";
    }
}