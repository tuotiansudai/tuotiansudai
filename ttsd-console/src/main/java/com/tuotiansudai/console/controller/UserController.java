package com.tuotiansudai.console.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.dto.RemainUserDto;
import com.tuotiansudai.console.dto.UserItemDataDto;
import com.tuotiansudai.console.repository.model.UserMicroModelView;
import com.tuotiansudai.console.repository.model.UserOperation;
import com.tuotiansudai.console.service.ConsoleRiskEstimateService;
import com.tuotiansudai.console.service.ConsoleUserService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.riskestimation.*;
import com.tuotiansudai.exception.BaseException;
import com.tuotiansudai.log.service.AuditLogService;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.service.ImpersonateService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.spring.security.SignInClient;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/user-manage")
public class UserController {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private UserService userService;

    @Autowired
    private ConsoleUserService consoleUserService;

    @Autowired
    private ImpersonateService impersonateService;

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private SignInClient signInClient;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserMembershipService userMembershipService;

    @Autowired
    private ConsoleRiskEstimateService consoleRiskEstimateService;

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
        List<Role> roles = Lists.newArrayList(Role.values())
                .stream()
                .filter(role -> !Lists.newArrayList(Role.NOT_STAFF_RECOMMEND, Role.SD_STAFF_RECOMMEND, Role.ZC_STAFF_RECOMMEND, Role.AGENT).contains(role))
                .collect(Collectors.toList());

        if (!redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {
            if (!model.containsAttribute("user")) {
                EditUserDto editUserDto = consoleUserService.getEditUser(loginName);
                modelAndView.addObject("user", editUserDto);
                modelAndView.addObject("roles", roles);
                modelAndView.addObject("showCommit", true);
            }
            return modelAndView;
        } else {
            OperationTask<EditUserDto> task = (OperationTask<EditUserDto>) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
            String description = task.getDescription();
            String afterUpdate = description.split(" =></br> ")[1];
            ObjectMapper objectMapper = new ObjectMapper();
            EditUserDto editUserDto = objectMapper.readValue(afterUpdate, EditUserDto.class);
            UserModel userModel = consoleUserService.findByLoginName(loginName);
            BankCardModel bankCard = bindBankCardService.getPassedBankCard(loginName);
            if (bankCard != null) {
                editUserDto.setBankCardNumber(bankCard.getCardNumber());
            }

            AutoInvestPlanModel autoInvestPlan = investService.findAutoInvestPlan(loginName);
            editUserDto.setAutoInvestStatus(autoInvestPlan != null && autoInvestPlan.isEnabled() ? "1" : "0");
            editUserDto.setIdentityNumber(userModel == null || Strings.isNullOrEmpty(userModel.getUserName()) ? "" : userModel.getIdentityNumber());
            editUserDto.setUserName(userModel == null || Strings.isNullOrEmpty(userModel.getUserName()) ? "" : userModel.getUserName());
            modelAndView.addObject("user", editUserDto);
            modelAndView.addObject("roles", roles);
            modelAndView.addObject("taskId", taskId);
            modelAndView.addObject("sender", task.getSender());
            modelAndView.addObject("showCommit", LoginUserInfo.getLoginName().equals(task.getSender()));
            return modelAndView;
        }
    }

    @RequestMapping(value = "/account/{loginName}/search", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findLoginNames(@PathVariable String loginName) {
        return consoleUserService.findAllLoanerLikeLoginName(loginName);
    }

    @RequestMapping(value = "/account/{loginName}/query", method = RequestMethod.GET)
    @ResponseBody
    public List<String> findAllLoanerLikeLoginName(@PathVariable String loginName) {
        return consoleUserService.findAccountLikeLoginName(loginName);
    }

    @RequestMapping(value = "/user/{loginName}/search", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchLoginName(@PathVariable String loginName) {
        return consoleUserService.findLoginNameLike(loginName);
    }

    @RequestMapping(value = "/staff/{loginName}/search", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchStaffName(@PathVariable String loginName) {
        return consoleUserService.findStaffNameFromUserLike(loginName);
    }

    @RequestMapping(value = "/mobile/{mobile}/search", method = RequestMethod.GET)
    @ResponseBody
    public List<String> searchMobile(@PathVariable String mobile) {
        return consoleUserService.findMobileLike(mobile);
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView editUser(@ModelAttribute EditUserDto editUserDto, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String ip = RequestIPParser.parse(request);
        ModelAndView modelAndView = new ModelAndView();
        try {
            consoleUserService.editUser(LoginUserInfo.getLoginName(), editUserDto, ip);
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
            mv.addObject("userList", consoleUserService.searchAllUsers(loginName, referrerMobile, mobile, identityNumber));
        }
        return mv;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView findAllUser(@RequestParam(value = "loginName", required = false) String loginName,
                                    @RequestParam(value = "email", required = false) String email,
                                    @RequestParam(value = "mobile", required = false) String mobile,
                                    @RequestParam(value = "beginTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime,
                                    @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                    @RequestParam(value = "roleStage", required = false) RoleStage roleStage,
                                    @RequestParam(value = "referrerMobile", required = false) String referrerMobile,
                                    @RequestParam(value = "channel", required = false) String channel,
                                    @RequestParam(value = "source", required = false) Source source,
                                    @RequestParam(value = "userOperation", required = false) UserOperation userOperation,
                                    @RequestParam(value = "hasStaff", required = false) Boolean hasStaff,
                                    @RequestParam(value = "staffMobile", required = false) String staffMobile,
                                    @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        BaseDto<BasePaginationDataDto<UserItemDataDto>> baseDto = consoleUserService.findAllUser(loginName, email, mobile,
                beginTime, endTime, source, roleStage, referrerMobile, channel, userOperation, hasStaff, staffMobile, index, pageSize);
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
        List<String> channelList = consoleUserService.findAllUserChannels();
        mv.addObject("roleStageList", roleStageList);
        mv.addObject("channelList", channelList);
        mv.addObject("sourceList", Source.values());
        mv.addObject("userOperations", UserOperation.values());
        mv.addObject("hasStaff", hasStaff);
        mv.addObject("staffMobile", staffMobile);
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
        return consoleUserService.findAllAgents();
    }

    @RequestMapping(value = "/user/channels", method = RequestMethod.GET)
    @ResponseBody
    public List<String> queryAllChannel() {
        return consoleUserService.findAllUserChannels();
    }

    @RequestMapping(value = "/user/{channel}/channel", method = RequestMethod.GET)
    @ResponseBody
    public long queryUserByChannel(@PathVariable String channel) {
        return consoleUserService.findUsersCountByChannel(channel);
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

    @RequestMapping(value = "/user-micro-model", method = RequestMethod.GET)
    public ModelAndView userMicroModel(
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userRole", required = false) Role role,
            @RequestParam(value = "registerTimeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date registerTimeStart,
            @RequestParam(value = "registerTimeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date registerTimeEnd,
            @RequestParam(value = "hasCertify", required = false) String hasCertify,
            @RequestParam(value = "invested", required = false) String invested,
            @RequestParam(value = "totalInvestAmountStart", required = false) Long totalInvestAmountStart,
            @RequestParam(value = "totalInvestAmountEnd", required = false) Long totalInvestAmountEnd,
            @RequestParam(value = "totalWithdrawAmountStart", required = false) Long totalWithdrawAmountStart,
            @RequestParam(value = "totalWithdrawAmountEnd", required = false) Long totalWithdrawAmountEnd,
            @RequestParam(value = "userBalanceStart", required = false) Long userBalanceStart,
            @RequestParam(value = "userBalanceEnd", required = false) Long userBalanceEnd,
            @RequestParam(value = "investCountStart", required = false) Integer investCountStart,
            @RequestParam(value = "investCountEnd", required = false) Integer investCountEnd,
            @RequestParam(value = "loanCountStart", required = false) Integer loanCountStart,
            @RequestParam(value = "loanCountEnd", required = false) Integer loanCountEnd,
            @RequestParam(value = "transformPeriodStart", required = false) Integer transformPeriodStart,
            @RequestParam(value = "transformPeriodEnd", required = false) Integer transformPeriodEnd,
            @RequestParam(value = "invest1st2ndTimingStart", required = false) Integer invest1st2ndTimingStart,
            @RequestParam(value = "invest1st2ndTimingEnd", required = false) Integer invest1st2ndTimingEnd,
            @RequestParam(value = "invest1st3ndTimingStart", required = false) Integer invest1st3ndTimingStart,
            @RequestParam(value = "invest1st3ndTimingEnd", required = false) Integer invest1st3ndTimingEnd,
            @RequestParam(value = "lastInvestTimeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastInvestTimeStart,
            @RequestParam(value = "lastInvestTimeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastInvestTimeEnd,
            @RequestParam(value = "repayingAmountStart", required = false) Long repayingAmountStart,
            @RequestParam(value = "repayingAmountEnd", required = false) Long repayingAmountEnd,
            @RequestParam(value = "lastLoginTimeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastLoginTimeStart,
            @RequestParam(value = "lastLoginTimeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastLoginTimeEnd,
            @RequestParam(value = "lastLoginSource", required = false) Source lastLoginSource,
            @RequestParam(value = "lastRepayTimeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastRepayTimeStart,
            @RequestParam(value = "lastRepayTimeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastRepayTimeEnd,
            @RequestParam(value = "lastWithdrawTimeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastWithdrawTimeStart,
            @RequestParam(value = "lastWithdrawTimeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date lastWithdrawTimeEnd,
            @RequestParam(value = "index", defaultValue = "1", required = false) int index) {

        int pageSize = 10;
        BaseDto<BasePaginationDataDto<UserMicroModelView>> baseDto = consoleUserService.queryUserMicroView(mobile,
                role,
                registerTimeStart,
                registerTimeEnd,
                hasCertify,
                invested,
                totalInvestAmountStart == null ? null : totalInvestAmountStart * 100,
                totalInvestAmountEnd == null ? null : totalInvestAmountEnd * 100,
                totalWithdrawAmountStart == null ? null : totalWithdrawAmountStart * 100,
                totalWithdrawAmountEnd == null ? null : totalWithdrawAmountEnd * 100,
                userBalanceStart == null ? null : userBalanceStart * 100,
                userBalanceEnd == null ? null : userBalanceEnd * 100,
                investCountStart,
                investCountEnd,
                loanCountStart,
                loanCountEnd,
                transformPeriodStart,
                transformPeriodEnd,
                invest1st2ndTimingStart,
                invest1st2ndTimingEnd,
                invest1st3ndTimingStart,
                invest1st3ndTimingEnd,
                lastInvestTimeStart,
                lastInvestTimeEnd,
                repayingAmountStart == null ? null : repayingAmountStart * 100,
                repayingAmountEnd == null ? null : repayingAmountEnd * 100,
                lastLoginTimeStart,
                lastLoginTimeEnd,
                lastLoginSource,
                lastRepayTimeStart,
                lastRepayTimeEnd,
                lastWithdrawTimeStart,
                lastWithdrawTimeEnd,
                index,
                pageSize);

        ModelAndView mv = new ModelAndView("/user-micro-model");
        mv.addObject("baseDto", baseDto);
        mv.addObject("mobile", mobile);
        mv.addObject("roleSelected", role);
        mv.addObject("registerTimeStart", registerTimeStart);
        mv.addObject("registerTimeEnd", registerTimeEnd);
        mv.addObject("hasCertify", hasCertify);
        mv.addObject("invested", invested);
        mv.addObject("totalInvestAmountStart", totalInvestAmountStart);
        mv.addObject("totalInvestAmountEnd", totalInvestAmountEnd);
        mv.addObject("totalWithdrawAmountStart", totalWithdrawAmountStart);
        mv.addObject("totalWithdrawAmountEnd", totalWithdrawAmountEnd);
        mv.addObject("userBalanceStart", userBalanceStart);
        mv.addObject("userBalanceEnd", userBalanceEnd);
        mv.addObject("investCountStart", investCountStart);
        mv.addObject("investCountEnd", investCountEnd);
        mv.addObject("loanCountStart", loanCountStart);
        mv.addObject("loanCountEnd", loanCountEnd);
        mv.addObject("transformPeriodStart", transformPeriodStart);
        mv.addObject("transformPeriodEnd", transformPeriodEnd);
        mv.addObject("invest1st2ndTimingStart", invest1st2ndTimingStart);
        mv.addObject("invest1st2ndTimingEnd", invest1st2ndTimingEnd);
        mv.addObject("invest1st3ndTimingStart", invest1st3ndTimingStart);
        mv.addObject("invest1st3ndTimingEnd", invest1st3ndTimingEnd);
        mv.addObject("lastInvestTimeStart", lastInvestTimeStart);
        mv.addObject("lastInvestTimeEnd", lastInvestTimeEnd);
        mv.addObject("repayingAmountStart", repayingAmountStart);
        mv.addObject("repayingAmountEnd", repayingAmountEnd);
        mv.addObject("lastLoginTimeStart", lastLoginTimeStart);
        mv.addObject("lastLoginTimeEnd", lastLoginTimeEnd);
        mv.addObject("lastLoginSource", lastLoginSource);
        mv.addObject("lastRepayTimeStart", lastRepayTimeStart);
        mv.addObject("lastRepayTimeEnd", lastRepayTimeEnd);
        mv.addObject("lastWithdrawTimeStart", lastWithdrawTimeStart);
        mv.addObject("lastWithdrawTimeEnd", lastWithdrawTimeEnd);
        mv.addObject("index", index);
        mv.addObject("pageSize", pageSize);

        mv.addObject("sourceList", Source.values());
        mv.addObject("roleList", Role.values());
        return mv;
    }

    @RequestMapping(value = "/remain-users", method = RequestMethod.GET)
    public ModelAndView remainUser(@RequestParam(value = "loginName", required = false) String loginName,
                                   @RequestParam(value = "mobile", required = false) String mobile,
                                   @RequestParam(value = "registerStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date registerStartTime,
                                   @RequestParam(value = "registerEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date registerEndTime,
                                   @RequestParam(value = "useExperienceCoupon", required = false) Boolean useExperienceCoupon,
                                   @RequestParam(value = "experienceStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date experienceStartTime,
                                   @RequestParam(value = "experienceEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date experienceEndTime,
                                   @RequestParam(value = "investCountLowLimit", required = false) Integer investCountLowLimit,
                                   @RequestParam(value = "investCountHighLimit", required = false) Integer investCountHighLimit,
                                   @RequestParam(value = "investSumLowLimit", required = false) String investSumLowLimit,
                                   @RequestParam(value = "investSumHighLimit", required = false) String investSumHighLimit,
                                   @RequestParam(value = "firstInvestStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date firstInvestStartTime,
                                   @RequestParam(value = "firstInvestEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date firstInvestEndTime,
                                   @RequestParam(value = "secondInvestStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date secondInvestStartTime,
                                   @RequestParam(value = "secondInvestEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date secondInvestEndTime,
                                   @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        final int pageSize = 10;

        ModelAndView modelAndView = new ModelAndView("/user-remain-list");

        BasePaginationDataDto<RemainUserDto> data = consoleUserService.findRemainUsers(loginName, mobile, registerStartTime,
                registerEndTime, useExperienceCoupon, experienceStartTime, experienceEndTime, investCountLowLimit, investCountHighLimit,
                StringUtils.isEmpty(investSumLowLimit) ? null : AmountConverter.convertStringToCent(investSumLowLimit),
                StringUtils.isEmpty(investSumHighLimit) ? null : AmountConverter.convertStringToCent(investSumHighLimit),
                firstInvestStartTime, firstInvestEndTime, secondInvestStartTime, secondInvestEndTime, index, pageSize);

        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("registerStartTime", registerStartTime);
        modelAndView.addObject("registerEndTime", registerEndTime);
        modelAndView.addObject("useExperienceCoupon", useExperienceCoupon);
        modelAndView.addObject("experienceStartTime", experienceStartTime);
        modelAndView.addObject("experienceEndTime", experienceEndTime);
        modelAndView.addObject("investCountLowLimit", investCountLowLimit);
        modelAndView.addObject("investCountHighLimit", investCountHighLimit);
        modelAndView.addObject("investSumLowLimit", investSumLowLimit);
        modelAndView.addObject("investSumHighLimit", investSumHighLimit);
        modelAndView.addObject("firstInvestStartTime", firstInvestStartTime);
        modelAndView.addObject("firstInvestEndTime", firstInvestEndTime);
        modelAndView.addObject("secondInvestStartTime", secondInvestStartTime);
        modelAndView.addObject("secondInvestEndTime", secondInvestEndTime);
        modelAndView.addObject("data", data);

        return modelAndView;
    }


    @RequestMapping(value = "/risk-estimate", method = RequestMethod.GET)
    public ModelAndView riskEstimate(@RequestParam(value = "selectedEstimate", required = false) Estimate selectedEstimate,
                                     @RequestParam(value = "selectedIncome", required = false) Income selectedIncome,
                                     @RequestParam(value = "selectedRate", required = false) Rate selectedRate,
                                     @RequestParam(value = "selectedDuration", required = false) Duration selectedDuration,
                                     @RequestParam(value = "selectedAge", required = false) Age selectedAge,
                                     @RequestParam(value = "index", required = false, defaultValue = "1") int index) {

        ModelAndView modelAndView = new ModelAndView("/risk-estimate");
        modelAndView.addObject("estimateOptions", Estimate.values());
        modelAndView.addObject("incomeOptions", Income.values());
        modelAndView.addObject("rateOptions", Rate.values());
        modelAndView.addObject("durationOptions", Duration.values());
        modelAndView.addObject("ageOptions", Age.values());

        modelAndView.addObject("selectedEstimate", selectedEstimate);
        modelAndView.addObject("selectedIncome", selectedIncome);
        modelAndView.addObject("selectedRate", selectedRate);
        modelAndView.addObject("selectedDuration", selectedDuration);
        modelAndView.addObject("selectedAge", selectedAge);

        modelAndView.addObject("pagination", consoleRiskEstimateService.listRiskEstimate(selectedEstimate, selectedIncome, selectedRate, selectedDuration, selectedAge, index));

        return modelAndView;
    }


}
