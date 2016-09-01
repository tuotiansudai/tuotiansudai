package com.tuotiansudai.console.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.repository.mapper.UserMapperConsole;
import com.tuotiansudai.console.service.UserServiceConsole;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.dto.UserItemDataDto;
import com.tuotiansudai.exception.BaseException;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.*;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.util.RequestIPParser;
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
@RequestMapping(value = "/user-manage")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceConsole userServiceConsole;

    @Autowired
    private UserMapperConsole userMapperConsole;

    @Autowired
    private ImpersonateService impersonateService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private InvestService investService;

    @Value("${web.server}")
    private String webServer;

    @RequestMapping(value = "/user/{loginName}", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable String loginName, Model model) throws Exception {
        String taskId = OperationType.USER + "-" + loginName;
        ModelAndView modelAndView = new ModelAndView("/user-edit");
        if (!redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {
            if (!model.containsAttribute("user")) {
                EditUserDto editUserDto = userService.getEditUser(loginName);
                modelAndView.addObject("user", editUserDto);
                modelAndView.addObject("roles", Role.values());
                modelAndView.addObject("showCommit", true);
            }
            return modelAndView;
        } else {
            OperationTask<EditUserDto> task = (OperationTask<EditUserDto>) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
            String description = task.getDescription();
            String afterUpdate = description.split(" =></br> ")[1];
            ObjectMapper objectMapper = new ObjectMapper();
            EditUserDto editUserDto = objectMapper.readValue(afterUpdate, EditUserDto.class);
            AccountModel accountModel = accountService.findByLoginName(loginName);
            BankCardModel bankCard = bindBankCardService.getPassedBankCard(loginName);
            if (bankCard != null) {
                editUserDto.setBankCardNumber(bankCard.getCardNumber());
            }

            AutoInvestPlanModel autoInvestPlan = investService.findAutoInvestPlan(loginName);
            editUserDto.setAutoInvestStatus(autoInvestPlan != null && autoInvestPlan.isEnabled() ? "1" : "0");
            editUserDto.setIdentityNumber(accountModel == null ? "" : accountModel.getIdentityNumber());
            editUserDto.setUserName(accountModel == null ? "" : accountModel.getUserName());
            modelAndView.addObject("user", editUserDto);
            modelAndView.addObject("roles", Role.values());
            modelAndView.addObject("taskId", taskId);
            modelAndView.addObject("sender", task.getSender());
            modelAndView.addObject("showCommit", LoginUserInfo.getLoginName().equals(task.getSender()));
            return modelAndView;
        }
    }

    @RequestMapping(value = "/account/{loginName}/search", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findLoginNames(@PathVariable String loginName) {
        return userServiceConsole.findAllLoanerLikeLoginName(loginName);
    }

    @RequestMapping(value = "/account/{loginName}/query", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findAllLoanerLikeLoginName(@PathVariable String loginName) {
        return userServiceConsole.findAccountLikeLoginName(loginName);
    }

    @RequestMapping(value = "/user/{loginName}/search", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchLoginName(@PathVariable String loginName) {
        return userServiceConsole.findLoginNameLike(loginName);
    }

    @RequestMapping(value = "/staff/{loginName}/search", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchStaffName(@PathVariable String loginName) {
        return userServiceConsole.findStaffNameFromUserLike(loginName);
    }

    @RequestMapping(value = "/mobile/{mobile}/search", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchMobile(@PathVariable String mobile) {
        return userServiceConsole.findMobileLike(mobile);
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
    public ModelAndView searchAllUsers(String loginName,
                                       String referrerMobile,
                                       String mobile,
                                       String identityNumber, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/user-search");
        if (request.getParameterMap().size() != 0) {
            mv.addObject("loginName", loginName);
            mv.addObject("referrerMobile", referrerMobile);
            mv.addObject("mobile", mobile);
            mv.addObject("identityNumber", identityNumber);
            mv.addObject("userList", userServiceConsole.searchAllUsers(loginName, referrerMobile, mobile, identityNumber));
        }
        return mv;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findAllUser(String loginName, String email, String mobile,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date beginTime,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                    RoleStage roleStage, String referrerMobile, String channel, @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                    @RequestParam(value = "source", required = false) Source source,
                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        BaseDto<BasePaginationDataDto<UserItemDataDto>> baseDto = userServiceConsole.findAllUser(loginName, email, mobile, beginTime, endTime, source, roleStage, referrerMobile, channel, index, pageSize);
        ModelAndView mv = new ModelAndView("/user-list");
        mv.addObject("baseDto", baseDto);
        mv.addObject("loginName", loginName);
        mv.addObject("email", email);
        mv.addObject("mobile", mobile);
        mv.addObject("beginTime", beginTime);
        mv.addObject("endTime", endTime);
        mv.addObject("roleStage", roleStage);
        mv.addObject("referrerMobile", referrerMobile);
        mv.addObject("channel", channel);
        mv.addObject("source", source);
        mv.addObject("pageIndex", index);
        mv.addObject("pageSize", pageSize);
        List<RoleStage> roleStageList = Lists.newArrayList(RoleStage.values());
        List<String> channelList = userService.findAllUserChannels();
        mv.addObject("roleStageList", roleStageList);
        mv.addObject("channelList", channelList);
        mv.addObject("sourceList", Source.values());
        return mv;
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

    @RequestMapping(value = "/user/{loginName}/impersonate", method = RequestMethod.GET)
    public ModelAndView impersonate(@PathVariable String loginName) {
        String securityCode = impersonateService.plantSecurityCode(LoginUserInfo.getLoginName(), loginName);
        return new ModelAndView("redirect:" + webServer + "/impersonate?securityCode=" + securityCode);
    }

    @RequestMapping(value = "/user/agents", method = RequestMethod.GET)
    @ResponseBody
    public List<String> queryAllAgent() {
        return userRoleService.queryAllAgent();
    }

    @RequestMapping(value = "/user/channels", method = RequestMethod.GET)
    @ResponseBody
    public List<String> queryAllChannel() {
        return userService.findAllUserChannels();
    }

    @RequestMapping(value = "/user/{channel}/channel", method = RequestMethod.GET)
    @ResponseBody
    public long queryUserByChannel(@PathVariable String channel) {
        return userServiceConsole.findUsersCountByChannel(channel);
    }

}
