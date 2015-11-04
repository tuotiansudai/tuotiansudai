package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.BaseException;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.utils.LoginUserInfo;
import com.tuotiansudai.utils.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value("${login.max.times}")
    private int times;

    @RequestMapping(value = "/user/{loginName}/edit", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable String loginName, Model model) {
        ModelAndView modelAndView = new ModelAndView("/edit-user");
        if (!model.containsAttribute("user")) {
            EditUserDto editUserDto = userService.getEditUser(loginName);
            modelAndView.addObject("user", editUserDto);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/user/{loginName}/search", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchLoginName(@PathVariable String loginName) {
        return userService.findLoginNameLike(loginName);
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView editUser(@ModelAttribute EditUserDto editUserDto, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String ip = RequestIPParser.getRequestIp(request);
        ModelAndView modelAndView = new ModelAndView();
        try {
            userService.editUser(editUserDto, ip);
            modelAndView.setViewName("redirect:/users");
            return modelAndView;
        } catch (BaseException e) {
            modelAndView.setViewName(MessageFormat.format("redirect:/user/{0}/edit", editUserDto.getLoginName()));
            redirectAttributes.addFlashAttribute("user", editUserDto);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findAllUser(String loginName, String email, String mobile,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date beginTime,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                    Role role, String referrer, @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        BaseDto<BasePaginationDataDto> baseDto = userService.findAllUser(loginName, email, mobile, beginTime, endTime, role, referrer, index, pageSize);
        ModelAndView mv = new ModelAndView("/user-list");
        mv.addObject("baseDto", baseDto);
        mv.addObject("loginName", loginName);
        mv.addObject("email", email);
        mv.addObject("mobile", mobile);
        mv.addObject("beginTime", beginTime);
        mv.addObject("endTime", endTime);
        mv.addObject("role", role);
        mv.addObject("referrer", referrer);
        mv.addObject("pageIndex", index);
        mv.addObject("pageSize", pageSize);
        List<Role> roleList = Lists.newArrayList(Role.values());
        mv.addObject("roleList", roleList);
        return mv;

    }

    @RequestMapping(value = "/user/{loginName}/disable", method = RequestMethod.POST)
    @ResponseBody
    public String disableUser(@PathVariable String loginName, HttpServletRequest request) {
        if (loginName.equals(LoginUserInfo.getLoginName())) {
            return "不能禁用当前登录用户";
        }
        String ip = RequestIPParser.getRequestIp(request);
        userService.updateUserStatus(loginName, UserStatus.INACTIVE, ip);
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", loginName);
        redisWrapperClient.set(redisKey,String.valueOf(times));
        return "OK";
    }

    @RequestMapping(value = "/user/{loginName}/enable", method = RequestMethod.POST)
    @ResponseBody
    public String enableUser(@PathVariable String loginName, HttpServletRequest request) {
        String ip = RequestIPParser.getRequestIp(request);
        userService.updateUserStatus(loginName, UserStatus.ACTIVE, ip);
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", loginName);
        redisWrapperClient.del(redisKey);
        return "OK";
    }
}
