package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseWrapperDataDto;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.RedisWrapperClient;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping(path = "/help")
public class HelpCenterController {
    private static final String HELP_CENTER_VOTE_KEY = "helpvote";
    @Value("${pay.withdraw.fee}")
    private long withdrawFee;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();
    @Autowired
    private OperationDataService operationDataService;

    @RequestMapping(path = "/{item:^help-center|account|user|money|product|help-content|other$}", method = RequestMethod.GET)
    public ModelAndView about(@PathVariable String item, @RequestParam(value = "contentId", required = false) String contentId) {
        ModelAndView modelAndView = new ModelAndView("/helpCenter/" + item);
        modelAndView.addObject("responsive", true);
        if ("help-content".equals(item)) {
            modelAndView.addObject("voteNumber", contentId == null ? 0 : redisWrapperClient.hlen(HELP_CENTER_VOTE_KEY + contentId) + contentId.hashCode() & 127);
        }
        return modelAndView;
    }

    @RequestMapping(path = "/operation-data/chart", method = RequestMethod.GET)
    @ResponseBody
    public OperationDataDto infoPublishChart() {
        return operationDataService.getOperationDataFromRedis(new Date());
    }


    @RequestMapping(path = "/help-content/vote")
    @ResponseBody
    public BaseWrapperDataDto voteHelpCenter(HttpServletRequest request) {
        BaseWrapperDataDto wrapperDataDto = new BaseWrapperDataDto();
        wrapperDataDto.setStatus(true);
        wrapperDataDto.setData(setHelperVoteNumber(request));
        return wrapperDataDto;
    }

    private int setHelperVoteNumber(HttpServletRequest request) {
        String contentId = request.getParameter("contentId");
        if (contentId == null) {
            return 0;
        }
        String ip = RequestIPParser.parse(request);
        String browserInfo = RequestIPParser.getOsAndBrowserInfo(request);
        redisWrapperClient.hset(HELP_CENTER_VOTE_KEY + contentId, ip + browserInfo, "1");
        return redisWrapperClient.hlen(HELP_CENTER_VOTE_KEY + contentId) + contentId.hashCode() & 127;
    }
}
