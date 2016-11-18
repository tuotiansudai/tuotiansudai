package com.tuotiansudai.console.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.repository.model.UserOperation;
import com.tuotiansudai.console.service.UserServiceConsole;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.dto.UserItemDataDto;
import com.tuotiansudai.exception.BaseException;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.*;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.spring.security.SignInClient;
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
    private ImpersonateService impersonateService;

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private InvestService investService;

    @Autowired
    private SignInClient signInClient;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserMembershipService userMembershipService;

    private final static long MEMBERSHIP_V0 = 0;
    private final static long MEMBERSHIP_V1 = 1;
    private final static long MEMBERSHIP_V2 = 2;
    private final static long MEMBERSHIP_V3 = 3;
    private final static long MEMBERSHIP_V4 = 4;
    private final static long MEMBERSHIP_V5 = 5;

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
            UserModel userModel = userService.findByLoginName(loginName);
            BankCardModel bankCard = bindBankCardService.getPassedBankCard(loginName);
            if (bankCard != null) {
                editUserDto.setBankCardNumber(bankCard.getCardNumber());
            }

            AutoInvestPlanModel autoInvestPlan = investService.findAutoInvestPlan(loginName);
            editUserDto.setAutoInvestStatus(autoInvestPlan != null && autoInvestPlan.isEnabled() ? "1" : "0");
            editUserDto.setIdentityNumber(userModel == null || Strings.isNullOrEmpty(userModel.getUserName()) ? "" : userModel.getIdentityNumber());
            editUserDto.setUserName(userModel == null || Strings.isNullOrEmpty(userModel.getUserName()) ? "" : userModel.getUserName());
            modelAndView.addObject("user", editUserDto);
            modelAndView.addObject("roles", Role.values());
            modelAndView.addObject("taskId", taskId);
            modelAndView.addObject("sender", task.getSender());
            modelAndView.addObject("showCommit", LoginUserInfo.getLoginName().equals(task.getSender()));
            return modelAndView;
        }
    }

    @RequestMapping(value = "/account/{loginName}/search/{loanerType}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findLoanerLoginNames(@PathVariable String loginName, @PathVariable String loanerType) {
        if ("ENTERPRISE_DIRECT".equals(loanerType)) {
            return userServiceConsole.findAllEnterpriseLoanerLikeLoginName(loginName);
        } else {
            return userServiceConsole.findAllLoanerLikeLoginName(loginName);
        }
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
    public ModelAndView findAllUser(@RequestParam(value = "loginName", required = false) String loginName,
                                    @RequestParam(value = "email", required = false) String email,
                                    @RequestParam(value = "mobile", required = false) String mobile,
                                    @RequestParam(value = "beginTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date beginTime,
                                    @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                    @RequestParam(value = "roleStage", required = false) RoleStage roleStage,
                                    @RequestParam(value = "referrerMobile", required = false) String referrerMobile,
                                    @RequestParam(value = "channel", required = false) String channel,
                                    @RequestParam(value = "source", required = false) Source source,
                                    @RequestParam(value = "userOperation", required = false) UserOperation userOperation,
                                    @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        BaseDto<BasePaginationDataDto<UserItemDataDto>> baseDto = userServiceConsole.findAllUser(loginName, email, mobile,
                beginTime, endTime, source, roleStage, referrerMobile, channel, userOperation, index, pageSize);
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
        mv.addObject("selectedUserOperation", userOperation);
        mv.addObject("pageIndex", index);
        mv.addObject("pageSize", pageSize);
        List<RoleStage> roleStageList = Lists.newArrayList(RoleStage.values());
        List<String> channelList = userService.findAllUserChannels();
        mv.addObject("roleStageList", roleStageList);
        mv.addObject("channelList", channelList);
        mv.addObject("sourceList", Source.values());
        mv.addObject("userOperations", UserOperation.values());
        return mv;
    }

    @RequestMapping(value = "/user/{loginName}/enable", method = RequestMethod.POST)
    @ResponseBody
    public String enableUser(@PathVariable String loginName, HttpServletRequest request) {
        String ip = RequestIPParser.parse(request);
        String mobile = userService.getMobile(loginName);
        signInClient.unlockUser(loginName, mobile);
        auditLogService.createUserActiveLog(loginName, LoginUserInfo.getLoginName(), UserStatus.ACTIVE, ip);
        return "OK";
    }

    @RequestMapping(value = "/user/{loginName}/impersonate", method = RequestMethod.GET)
    @ResponseBody
    public String impersonate(@PathVariable String loginName) {
        return impersonateService.plantSecurityCode(LoginUserInfo.getLoginName(), loginName);
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

    @RequestMapping(value = "/userMembership/count", method = RequestMethod.GET)
    @ResponseBody
    public long queryUserMembershipByLevelCount(@RequestParam(value = "userGroup") UserGroup userGroup) {
        long level = MEMBERSHIP_V0;
        if (userGroup.equals(UserGroup.MEMBERSHIP_V1)) {
            level = MEMBERSHIP_V1;
        } else if (userGroup.equals(UserGroup.MEMBERSHIP_V2)) {
            level = MEMBERSHIP_V2;
        } else if (userGroup.equals(UserGroup.MEMBERSHIP_V3)) {
            level = MEMBERSHIP_V3;
        } else if (userGroup.equals(UserGroup.MEMBERSHIP_V4)) {
            level = MEMBERSHIP_V4;
        } else if (userGroup.equals(UserGroup.MEMBERSHIP_V5)) {
            level = MEMBERSHIP_V5;
        }
        return userMembershipService.findCountMembershipByLevel(level);
    }

}
