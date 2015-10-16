package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.utils.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/{loginName}/edit", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable String loginName) {
        ModelAndView mv = new ModelAndView("/edit-user");
        EditUserDto editUserDto = userService.getEditUser(loginName);
        mv.addObject(editUserDto);
        return mv;
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> editUser(@ModelAttribute EditUserDto editUserDto,HttpServletRequest request) {
        String ip = RequestIPParser.getRequestIp(request);
        return userService.editUser(editUserDto, ip);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findAllUser(String loginName, String email, String mobile,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date beginTime,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                    Role role, String referrer, @RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                    @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {

        BaseDto<BasePaginationDataDto> baseDto = userService.findAllUser(loginName, email, mobile, beginTime, endTime, role, referrer, index, pageSize);
        ModelAndView mv = new ModelAndView("/user-list");
        mv.addObject("baseDto",baseDto);
        mv.addObject("loginName",loginName);
        mv.addObject("email",email);
        mv.addObject("mobile",mobile);
        mv.addObject("beginTime",beginTime);
        mv.addObject("endTime",endTime);
        mv.addObject("role",role);
        mv.addObject("referrer",referrer);
        mv.addObject("pageIndex",index);
        mv.addObject("pageSize",pageSize);
        List<Role> roleList = Lists.newArrayList(Role.values());
        mv.addObject("roleList",roleList);
        return mv;

    }

    @RequestMapping(value = "/user/name-like-query/{loginName}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findLoginNames(@PathVariable String loginName) {
        return userService.findLoginNameLike(loginName);
    }


}
