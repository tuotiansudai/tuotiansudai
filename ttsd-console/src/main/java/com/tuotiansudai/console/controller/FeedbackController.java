package com.tuotiansudai.console.controller;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.UserItemDataDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.FeedbackService;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/announce-manage", method = RequestMethod.GET)
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/feedback", method = RequestMethod.GET)
    public ModelAndView announceManage(@RequestParam(value = "loginName", required = false) String loginName,
                                       @RequestParam(value = "source", required = false) Source source,
                                       @RequestParam(value = "type", required = false) FeedbackType type,
                                       @RequestParam(value = "status", required = false) ProcessStatus status,
                                       @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                       @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                       @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                       @RequestParam(value = "export", required = false) String export,
                                       HttpServletResponse response) throws IOException {
        if (export != null && !export.equals("")) {
            response.setCharacterEncoding("UTF-8");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("意见反馈.csv", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/csv");
            long count = feedbackService.getFeedbackCount(loginName, source, type, status, startTime, endTime);
            BasePaginationDataDto<FeedbackModel> feedbackModel = feedbackService.getFeedbackPagination(loginName, source, type, status, startTime, endTime, index, pageSize);
            List<List<String>> data = Lists.newArrayList();
            List<FeedbackModel> feedbackModelList = feedbackModel.getRecords();
            for (int i = 0; i < feedbackModelList.size(); i++) {
                List<String> dataModel = Lists.newArrayList();
                dataModel.add(String.valueOf(feedbackModelList.get(i).getId()));
                dataModel.add(feedbackModelList.get(i).getLoginName());
                dataModel.add(feedbackModelList.get(i).getContact());
                dataModel.add(feedbackModelList.get(i).getSource().name());
                dataModel.add(feedbackModelList.get(i).getType().getDesc());
                dataModel.add(feedbackModelList.get(i).getContent());
                dataModel.add(new DateTime(feedbackModelList.get(i).getCreatedTime()).toString("yyyy-MM-dd HH:mm"));
                dataModel.add(feedbackModelList.get(i).getStatus().getDesc());
                data.add(dataModel);
            }
            ExportCsvUtil.createCsvOutputStream(CsvHeaderType.Feedback, data, response.getOutputStream());
            return null;
        } else {
            BasePaginationDataDto<FeedbackModel> feedbackModelPaginationData = feedbackService.getFeedbackPagination(loginName, source, type, status, startTime, endTime, index, pageSize);
            ModelAndView mv = new ModelAndView("feedback-list");
            mv.addObject("loginName", loginName);
            mv.addObject("source", source);
            mv.addObject("type", type);
            mv.addObject("status", status);
            mv.addObject("startTime", startTime);
            mv.addObject("endTime", endTime);

            mv.addObject("sourceList", Source.values());
            mv.addObject("typeList", FeedbackType.values());
            mv.addObject("statusList", ProcessStatus.values());

            mv.addObject("feedbackCount", feedbackModelPaginationData.getCount());
            mv.addObject("feedbackList", feedbackModelPaginationData.getRecords());
            mv.addObject("index", index);
            mv.addObject("pageSize", pageSize);
            mv.addObject("hasNextPage", feedbackModelPaginationData.isHasNextPage());
            mv.addObject("hasPreviousPage", feedbackModelPaginationData.isHasPreviousPage());
            return mv;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public String updateStatus(long feedbackId, boolean status) {
        feedbackService.updateStatus(feedbackId, status ? ProcessStatus.DONE : ProcessStatus.NOT_DONE);
        return "true";
    }
}
