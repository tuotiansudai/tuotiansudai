package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.UserItemDataDto;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserRoleService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping(value = "/finance-manage")
public class AccountBalanceController {

    static Logger logger = Logger.getLogger(AccountBalanceController.class);

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/account-balance")
    public ModelAndView accountBalance(@RequestParam(value = "currentPageNo", defaultValue = "1", required = false) int currentPageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                        @RequestParam(value = "loginName",required = false) String loginName,
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
            List<UserItemDataDto> userItemDataDtoList = userService.findUsersAccountBalance(loginName, 1, Integer.MAX_VALUE);
            List<List<String>> data = Lists.newArrayList();
            for (UserItemDataDto itemDataDto : userItemDataDtoList) {
                List<String> dataModel = Lists.newArrayList();
                dataModel.add(itemDataDto.getLoginName());
                dataModel.add(itemDataDto.isStaff() ? "是" : "否");
                dataModel.add(itemDataDto.getUserName());
                dataModel.add(itemDataDto.getMobile());
                dataModel.add(itemDataDto.getBirthday() != null ? itemDataDto.getBirthday() : "");
                dataModel.add(itemDataDto.getProvince() != null ? itemDataDto.getProvince() : "");
                dataModel.add(itemDataDto.getCity() != null ? itemDataDto.getCity() : "");
                dataModel.add(String.valueOf(new BigDecimal(itemDataDto.getBalance()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue()));
                data.add(dataModel);
            }
            ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleBalance, data, response.getOutputStream());
            return null;
        } else {
            ModelAndView modelAndView = new ModelAndView("/account-balance");
            modelAndView.addObject("currentPageNo", currentPageNo);
            modelAndView.addObject("pageSize", pageSize);
            List<UserItemDataDto> userItemDataDtoList = userService.findUsersAccountBalance(loginName, currentPageNo, pageSize);
            modelAndView.addObject("userAccountList", userItemDataDtoList);
            int count = userService.findUsersAccountBalanceCount(loginName);
            modelAndView.addObject("sumBalance", userService.findUsersAccountBalanceSum(loginName));
            long totalPages = count / pageSize + (count % pageSize > 0 ? 1 : 0);
            boolean hasPreviousPage = currentPageNo > 1 && currentPageNo <= totalPages;
            boolean hasNextPage = currentPageNo < totalPages;
            modelAndView.addObject("hasPreviousPage", hasPreviousPage);
            modelAndView.addObject("hasNextPage", hasNextPage);
            modelAndView.addObject("count", count);
            modelAndView.addObject("loginName", loginName);
            return modelAndView;
        }
    }

}
