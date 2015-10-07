package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/edit-user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{loginName}", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable String loginName) {
        ModelAndView mv = new ModelAndView("/edit-user");
        EditUserDto editUserDto = userService.getUser(loginName);
        mv.addObject(editUserDto);
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<PayDataDto> editUser(@RequestBody EditUserDto editUserDto,HttpServletRequest request) {
        try {
            return userService.editUser(editUserDto,request);
        }catch (Exception e){
            BaseDto<PayDataDto> baseDto = new BaseDto<>();
            PayDataDto payDataDto = new PayDataDto();
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getMessage());
            baseDto.setSuccess(true);
            return baseDto;
        }

    }





}
