package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.UserLotteryService;
import com.tuotiansudai.repository.model.UserLotteryTimeModel;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.log4j.Logger;
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
@RequestMapping("/activity-manage")
public class LotteryController {

    static Logger logger = Logger.getLogger(LotteryController.class);

    @Autowired
    private UserLotteryService userLotteryService;

    @RequestMapping(value = "/user-lottery-list", method = RequestMethod.GET)
    public ModelAndView userLotteryList(@RequestParam(name = "mobile",required = false) String mobile,
                                        @RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                        @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                        @RequestParam(value = "export", required = false) String export,
                                        HttpServletResponse response) throws IOException {
        if (export != null && !export.equals("")) {
            response.setCharacterEncoding("UTF-8");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("用户余额查询.csv", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            response.setContentType("application/csv");
            List<UserLotteryTimeModel> userLotteryTimeModels = userLotteryService.findUserLotteryTimeModels(mobile, 0, Integer.MAX_VALUE);
            List<List<String>> data = Lists.newArrayList();
            for(UserLotteryTimeModel userLotteryTimeModel : userLotteryTimeModels){
                List<String> dataModel = Lists.newArrayList();
                dataModel.add(userLotteryTimeModel.getMobile());
                dataModel.add(userLotteryTimeModel.getLoginName());
                dataModel.add(String.valueOf(userLotteryTimeModel.getUseCount()));
                dataModel.add(String.valueOf(userLotteryTimeModel.getUnUseCount()));
                data.add(dataModel);
            }
            
            ExportCsvUtil.createCsvOutputStream(CsvHeaderType.UserLotteryList, data, response.getOutputStream());
            return null;
        }else{
            ModelAndView modelAndView = new ModelAndView("activity-lottery-list");
            int lotteryCount = userLotteryService.findUserLotteryTimeCountModels(mobile);
            modelAndView.addObject("lotteryCount", lotteryCount);
            modelAndView.addObject("lotteryList",userLotteryService.findUserLotteryTimeModels(mobile,(index - 1) * pageSize,pageSize));
            modelAndView.addObject("index",index);
            modelAndView.addObject("pageSize",pageSize);
            long totalPages = lotteryCount / pageSize + (lotteryCount % pageSize > 0 || lotteryCount == 0 ? 1 : 0);
            boolean hasPreviousPage = index > 1 && index <= totalPages;
            boolean hasNextPage = index < totalPages;
            modelAndView.addObject("hasPreviousPage", hasPreviousPage);
            modelAndView.addObject("hasNextPage", hasNextPage);
            modelAndView.addObject("mobile",mobile);
            return modelAndView;
        }
    }
}
