package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.FeedbackModel;
import com.tuotiansudai.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/announce-manage", method = RequestMethod.GET)
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/feedback", method = RequestMethod.GET)
    public ModelAndView announceManage(@RequestParam(value = "loginName", required = false) String loginName,
                                       @RequestParam(value = "currentPageNo", defaultValue = "1", required = false) int currentPageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        BasePaginationDataDto<FeedbackModel> feedbackModelPaginationData = feedbackService.getFeedbackPagination(loginName, currentPageNo, pageSize);
        ModelAndView mv = new ModelAndView("feedback-list");
        mv.addObject("loginName", loginName);
        mv.addObject("feedbackCount", feedbackModelPaginationData.getCount());
        mv.addObject("feedbackList", feedbackModelPaginationData.getRecords());
        mv.addObject("currentPageNo", currentPageNo);
        mv.addObject("pageSize", pageSize);
        mv.addObject("hasNextPage", feedbackModelPaginationData.isHasNextPage());
        mv.addObject("hasPreviousPage", feedbackModelPaginationData.isHasPreviousPage());
        return mv;
    }
}
