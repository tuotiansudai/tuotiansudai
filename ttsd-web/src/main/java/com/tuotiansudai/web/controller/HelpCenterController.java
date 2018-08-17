package com.tuotiansudai.web.controller;

import com.mysql.jdbc.StringUtils;
import com.tuotiansudai.dto.BaseWrapperDataDto;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.RedisWrapperClient;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

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
    public ModelAndView about(@PathVariable String item) {
        ModelAndView modelAndView = new ModelAndView("/helpCenter/" + item);
        modelAndView.addObject("responsive", true);
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
        String contentId = request.getParameter("contentId");
        String isSolution = request.getParameter("isSolution");
        String ip = RequestIPParser.parse(request);
        String browserInfo = RequestIPParser.getOsAndBrowserInfo(request);
        String redisKey = HELP_CENTER_VOTE_KEY + contentId;

        HashMap<String, Integer> dataMap = new HashMap();
        BaseWrapperDataDto wrapperDataDto = new BaseWrapperDataDto();
        wrapperDataDto.setStatus(true);

        if (contentId == null) {
            dataMap.put("voteNumber", 0);
            dataMap.put("isSolution", -1);
            wrapperDataDto.setData(dataMap);
            return wrapperDataDto;
        }
        String redisisSolution = redisWrapperClient.hget(redisKey, ip + browserInfo);
        if (StringUtils.isNullOrEmpty(isSolution)) {
            dataMap.put("voteNumber", redisWrapperClient.hlen(redisKey) + contentId.hashCode() & 127);
            dataMap.put("isSolution", redisisSolution == null ? -1 : Integer.valueOf(redisisSolution));
            wrapperDataDto.setData(dataMap);
            return wrapperDataDto;
        }
        if (!("1".equals(isSolution) || "0".equals(isSolution))) {
            wrapperDataDto.setMessage("参数错误");
            wrapperDataDto.setStatus(false);
            return wrapperDataDto;
        }
        if (!StringUtils.isEmptyOrWhitespaceOnly(redisisSolution)) {
            wrapperDataDto.setMessage("已经投票");
            wrapperDataDto.setStatus(false);
            return wrapperDataDto;
        }
        redisWrapperClient.hset(HELP_CENTER_VOTE_KEY + contentId, ip + browserInfo, isSolution);
        dataMap.put("voteNumber", redisWrapperClient.hlen(redisKey) + contentId.hashCode() & 127);
        dataMap.put("isSolution", isSolution == null ? -1 : Integer.valueOf(isSolution));
        wrapperDataDto.setData(dataMap);
        return wrapperDataDto;
    }
}
