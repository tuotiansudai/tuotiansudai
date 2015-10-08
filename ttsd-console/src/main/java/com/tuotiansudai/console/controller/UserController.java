package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/edit-user/{loginName}", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable String loginName) {
        ModelAndView mv = new ModelAndView("/edit-user");
        EditUserDto editUserDto = userService.getUser(loginName);
        mv.addObject(editUserDto);
        return mv;
    }

    @RequestMapping(value = "/edit-user", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<PayDataDto> editUser(@RequestBody EditUserDto editUserDto, HttpServletRequest request) {
        try {
            return userService.editUser(editUserDto, request);
        } catch (Exception e) {
            BaseDto<PayDataDto> baseDto = new BaseDto<>();
            PayDataDto payDataDto = new PayDataDto();
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getMessage());
            baseDto.setSuccess(true);
            return baseDto;
        }

    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findAllUser(String loginName, String email, String mobile,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date beginTime,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                    Role role, String referrer, Integer index, Integer pageSize) {
        if(index == null || index <= 0){
            index = 1;
        }
        if (pageSize == null || pageSize <= 0){
            pageSize = 10;
        }
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

}
