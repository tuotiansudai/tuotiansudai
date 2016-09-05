package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.FeedbackModel;
import com.tuotiansudai.repository.model.FeedbackType;
import com.tuotiansudai.repository.model.ProcessStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.FeedbackService;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
    public ModelAndView announceManage(@RequestParam(value = "mobile", required = false) String mobile,
                                       @RequestParam(value = "source", required = false) Source source,
                                       @RequestParam(value = "type", required = false) FeedbackType type,
                                       @RequestParam(value = "status", required = false) ProcessStatus status,
                                       @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                       @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                       @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        BasePaginationDataDto<FeedbackModel> feedbackModelPaginationData = feedbackService.getFeedbackPagination(mobile, source, type, status, startTime, endTime, index, pageSize);
        ModelAndView mv = new ModelAndView("feedback-list");
        mv.addObject("mobile", mobile);
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

    @ResponseBody
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    public String updateStatus(long feedbackId, boolean status) {
        feedbackService.updateStatus(feedbackId, status ? ProcessStatus.DONE : ProcessStatus.NOT_DONE);
        return "true";
    }

    @ResponseBody
    @RequestMapping(value = "/updateRemark", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> updateRemark(long feedbackId, String remark) {
        FeedbackModel feedbackModel = feedbackService.findById(feedbackId);
        BaseDataDto dataDto = new BaseDataDto();
        if (feedbackModel != null) {
            feedbackModel.setRemark(StringUtils.isEmpty(feedbackModel.getRemark()) ? remark : feedbackModel.getRemark() + "|" + remark);
            feedbackService.updateRemark(feedbackModel);
        }
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);
        return baseDto;
    }
}
