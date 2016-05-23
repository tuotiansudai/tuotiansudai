package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.AccountItemDataDto;
import com.tuotiansudai.point.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/point-manage")
public class PointController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PointBillService pointBillService;

    @RequestMapping(value = "/user-point-list")
    public ModelAndView usersAccountPointList(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                              @RequestParam(value = "loginName", required = false) String loginName,
                                              @RequestParam(value = "userName", required = false) String userName,
                                              @RequestParam(value = "mobile", required = false) String mobile) {

        ModelAndView modelAndView = new ModelAndView("/user-point-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        List<AccountItemDataDto> accountItemDataDtoList = accountService.findUsersAccountPoint(loginName, userName, mobile, index, pageSize);
        modelAndView.addObject("userPointList", accountItemDataDtoList);
        int count = accountService.findUsersAccountPointCount(loginName, userName, mobile);
        long totalPages = count / pageSize + (count % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("count", count);
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("userName", userName);
        modelAndView.addObject("mobile", mobile);
        return modelAndView;
    }

    @RequestMapping(value = "/user-point-detail-list")
    public ModelAndView usersAccountPointDetailList(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                    @RequestParam(value = "loginName", required = false) String loginName,
                                                    @RequestParam(value = "point", required = false) String point,
                                                    @RequestParam(value = "totalPoint", required = false) String totalPoint) {

        ModelAndView modelAndView = new ModelAndView("/user-point-detail-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("point", point);
        modelAndView.addObject("totalPoint", totalPoint);

        List<PointBillPaginationItemDataDto> pointBillPaginationItemDataDtoList = pointBillService.getPointBillByLoginName(loginName, index, pageSize);

        modelAndView.addObject("userPointDetailList", pointBillPaginationItemDataDtoList);
        long count = pointBillService.getPointBillCountByLoginName(loginName);
        long totalPages = count / pageSize + (count % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("count", count);
        modelAndView.addObject("loginName", loginName);
        return modelAndView;
    }
}