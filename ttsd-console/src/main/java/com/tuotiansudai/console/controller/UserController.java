package com.tuotiansudai.console.controller;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.dto.UserItemDataDto;
import com.tuotiansudai.exception.BaseException;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/user-manage")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;


    @RequestMapping(value = "/user/{loginName}", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable String loginName, Model model) {
        ModelAndView modelAndView = new ModelAndView("/user-edit");
        if (!model.containsAttribute("user")) {
            EditUserDto editUserDto = userService.getEditUser(loginName);
            modelAndView.addObject("user", editUserDto);
            modelAndView.addObject("roles", Role.values());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/account/{loginName}/search", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findLoginNames(@PathVariable String loginName) {
        return userService.findLoginNameFromAccountLike(loginName);
    }


    @RequestMapping(value = "/user/{loginName}/search", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchLoginName(@PathVariable String loginName) {
        return userService.findLoginNameLike(loginName);
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView editUser(@ModelAttribute EditUserDto editUserDto, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String ip = RequestIPParser.parse(request);
        ModelAndView modelAndView = new ModelAndView();
        try {
            userService.editUser(LoginUserInfo.getLoginName(), editUserDto, ip);
            modelAndView.setViewName("redirect:/user-manage/users");
            return modelAndView;
        } catch (BaseException e) {
            modelAndView.setViewName(MessageFormat.format("redirect:/user-manage/user/{0}", editUserDto.getLoginName()));
            redirectAttributes.addFlashAttribute("user", editUserDto);
            redirectAttributes.addFlashAttribute("roles", Role.values());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/users-search", method = RequestMethod.GET)
    public ModelAndView searchAllUsers(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                        String loginName,
                                        String referrer,
                                        String mobile,
                                        String identityNumber) {
        ModelAndView mv = new ModelAndView("/user-search");
        mv.addObject("index",index);
        mv.addObject("pageSize",pageSize);
        mv.addObject("loginName",loginName);
        mv.addObject("referrer",referrer);
        mv.addObject("mobile",mobile);
        mv.addObject("identityNumber",identityNumber);
        mv.addObject("userList", userService.searchAllUsers(loginName, referrer, mobile, identityNumber, index, pageSize));
        int usersCount = userService.searchAllUsersCount(loginName, referrer, mobile, identityNumber);
        mv.addObject("userCount", usersCount);
        long totalPages = usersCount / pageSize + (usersCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        mv.addObject("hasPreviousPage", hasPreviousPage);
        mv.addObject("hasNextPage", hasNextPage);
        return mv;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findAllUser(String loginName, String email, String mobile,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date beginTime,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                    Role role, String referrer, String channel, @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                    @RequestParam(value = "source", required = false) Source source,
                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                    @RequestParam(value = "export", required = false) String export,
                                    HttpServletResponse response) throws IOException{
        if (export != null && !export.equals("")) {
            response.setCharacterEncoding("UTF-8");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("用户管理.csv", "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/csv");
            int count = userMapper.findAllUserCount(loginName, email, mobile, beginTime, endTime, source, role, referrer, channel);
            BaseDto<BasePaginationDataDto> baseDto = userService.findAllUser(loginName, email, mobile, beginTime, endTime, source, role, referrer, channel, 1, count);
            List<List<String>> data = Lists.newArrayList();
            List<UserItemDataDto> userItemDataDtos = baseDto.getData().getRecords();
            for (int i = 0 ;i < userItemDataDtos.size(); i++) {
                List<String> dataModel = Lists.newArrayList();
                dataModel.add(userItemDataDtos.get(i).getLoginName());
                dataModel.add(userItemDataDtos.get(i).isBankCard() ? "是" : "否");
                dataModel.add(userItemDataDtos.get(i).getUserName());
                dataModel.add(userItemDataDtos.get(i).getMobile());
                dataModel.add(userItemDataDtos.get(i).getEmail());
                dataModel.add(userItemDataDtos.get(i).getReferrer());
                dataModel.add(userItemDataDtos.get(i).isStaff() ? "是" : "否");
                dataModel.add(userItemDataDtos.get(i).getSource().name());
                dataModel.add(userItemDataDtos.get(i).getChannel());
                dataModel.add(new DateTime(userItemDataDtos.get(i).getRegisterTime()).toString("yyyy-MM-dd HH:mm"));

                List<UserRoleModel> userRoleModels = userItemDataDtos.get(i).getUserRoles();
                List<String> userRole = Lists.transform(userRoleModels, new Function<UserRoleModel, String>() {
                    @Override
                    public String apply(UserRoleModel input) {
                        return input.getRole().getDescription();
                    }
                });

                dataModel.add(StringUtils.join(userRole,";"));
                dataModel.add(userItemDataDtos.get(i).getStatus() == UserStatus.ACTIVE ? "正常" : "禁用");
                data.add(dataModel);
            }
            ExportCsvUtil.createCsvOutputStream(CsvHeaderType.ConsoleUsers, data, response.getOutputStream());
            return null;
        } else {
            BaseDto<BasePaginationDataDto> baseDto = userService.findAllUser(loginName, email, mobile, beginTime, endTime, source, role, referrer, channel, index, pageSize);
            ModelAndView mv = new ModelAndView("/user-list");
            mv.addObject("baseDto", baseDto);
            mv.addObject("loginName", loginName);
            mv.addObject("email", email);
            mv.addObject("mobile", mobile);
            mv.addObject("beginTime", beginTime);
            mv.addObject("endTime", endTime);
            mv.addObject("role", role);
            mv.addObject("referrer", referrer);
            mv.addObject("channel", channel);
            mv.addObject("source", source);
            mv.addObject("pageIndex", index);
            mv.addObject("pageSize", pageSize);
            List<Role> roleList = Lists.newArrayList(Role.values());
            List<String> channelList = userService.findAllChannels();
            mv.addObject("roleList", roleList);
            mv.addObject("channelList", channelList);
            mv.addObject("sourceList", Source.values());
            return mv;
        }
    }

    @RequestMapping(value = "/user/{loginName}/disable", method = RequestMethod.POST)
    @ResponseBody
    public String disableUser(@PathVariable String loginName, HttpServletRequest request) {
        if (loginName.equals(LoginUserInfo.getLoginName())) {
            return "不能禁用当前登录用户";
        }
        String ip = RequestIPParser.parse(request);
        userService.updateUserStatus(loginName, UserStatus.INACTIVE, ip, LoginUserInfo.getLoginName());
        return "OK";
    }

    @RequestMapping(value = "/user/{loginName}/enable", method = RequestMethod.POST)
    @ResponseBody
    public String enableUser(@PathVariable String loginName, HttpServletRequest request) {
        String ip = RequestIPParser.parse(request);
        userService.updateUserStatus(loginName, UserStatus.ACTIVE, ip, LoginUserInfo.getLoginName());
        return "OK";
    }
}
