package com.tuotiansudai.console.controller;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.console.service.ConsoleHomeService;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.AuditLogService;

import com.tuotiansudai.service.UserRoleService;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.task.TaskType;
import com.tuotiansudai.util.RequestIPParser;
import com.tuotiansudai.util.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ConsoleHomeService consoleHomeService;

    @Autowired
    RedisWrapperClient redisWrapperClient;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    AccountService accountService;

    @Autowired
    private AuditLogService auditLogService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {

        ModelAndView mav = new ModelAndView("/index");

        mav.addObject("taskList", getTaskList(LoginUserInfo.getLoginName()));

        mav.addObject("userToday", consoleHomeService.userToday());
        mav.addObject("user7Days", consoleHomeService.user7Days());
        mav.addObject("user30Days", consoleHomeService.user30Days());

        mav.addObject("rechargeTodayLoaner", consoleHomeService.rechargeToday_Loaner());
        mav.addObject("recharge7DaysLoaner", consoleHomeService.recharge7Days_Loaner());
        mav.addObject("recharge30DaysLoaner", consoleHomeService.recharge30Days_Loaner());

        mav.addObject("rechargeTodayNotLoaner", consoleHomeService.rechargeToday_NotLoaner());
        mav.addObject("recharge7DaysNotLoaner", consoleHomeService.recharge7Days_NotLoaner());
        mav.addObject("recharge30DaysNotLoaner", consoleHomeService.recharge30Days_NotLoaner());

        mav.addObject("withdrawTodayLoaner", consoleHomeService.withdrawToday_Loaner());
        mav.addObject("withdraw7DaysLoaner", consoleHomeService.withdraw7Days_Loaner());
        mav.addObject("withdraw30DaysLoaner", consoleHomeService.withdraw30Days_Loaner());

        mav.addObject("withdrawTodayNotLoaner", consoleHomeService.withdrawToday_NotLoaner());
        mav.addObject("withdraw7DaysNotLoaner", consoleHomeService.withdraw7Days_NotLoaner());
        mav.addObject("withdraw30DaysNotLoaner", consoleHomeService.withdraw30Days_NotLoaner());

        mav.addObject("investToday", consoleHomeService.investToday());
        mav.addObject("invest7Days", consoleHomeService.invest7Days());
        mav.addObject("invest30Days", consoleHomeService.invest30Days());

        mav.addObject("totalInvest", consoleHomeService.getSumInvestAmount());

        return mav;
    }

    private List<OperationTask> getTaskList(String loginName) {
        List<Role> roles = userRoleService.findRoleNameByLoginName(loginName);
        List<OperationTask> taskList = new ArrayList<>();
        for (Role role : roles) {
            List<byte[]> tasks = redisWrapperClient.hgetValuesSeri(TaskConstant.TASK_KEY + role);

            for (byte[] bs : tasks) {
                taskList.add((OperationTask) SerializeUtil.deserialize(bs));
            }
        }

        List<byte[]> notifies = redisWrapperClient.hgetValuesSeri(TaskConstant.NOTIFY_KEY + loginName);
        for (byte[] bs : notifies) {
            taskList.add((OperationTask) SerializeUtil.deserialize(bs));
        }

        Collections.sort(taskList);
        return taskList;
    }


    @ResponseBody
    @RequestMapping(value = "/refuse", method = RequestMethod.GET, params = "taskId")
    public BaseDto<BaseDataDto> refuseApply(String taskId, HttpServletRequest request) {

        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);

        String ip = RequestIPParser.parse(request);

        if (redisWrapperClient.hexists(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

            OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

            OperationTask notify = new OperationTask();
            String notifyId = taskId;

            notify.setId(notifyId);
            notify.setTaskType(TaskType.NOTIFY);

            OperationType operationType = task.getOperationType();
            notify.setOperationType(operationType);

            String senderLoginName = LoginUserInfo.getLoginName();
            notify.setSender(senderLoginName);
            notify.setReceiver(task.getSender());
            notify.setCreatedTime(new Date());

            notify.setObjId(task.getObjId());

            String senderRealName = accountService.getRealName(senderLoginName);

            notify.setDescription(senderRealName + " 拒绝了您 " + operationType.getDescription() + "［" + task.getObjName() + "］的申请。");

            redisWrapperClient.hdelSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
            redisWrapperClient.hsetSeri(TaskConstant.NOTIFY_KEY + task.getSender(), notifyId, notify);

            String operator = task.getSender();
            String operatorRealName = accountService.getRealName(operator);
            String description = senderRealName + " 拒绝了 " + operatorRealName + " " + operationType.getDescription() + "［" + task.getObjName() + "］的申请。";
            auditLogService.createAuditLog(senderLoginName, operator, operationType, task.getObjId(), description, ip);

            baseDto.setSuccess(true);
            baseDataDto.setStatus(true);
        } else {
            baseDto.setSuccess(false);
            baseDataDto.setMessage("此审核任务不存在或已经被处理，请勿重复处理。");
            baseDataDto.setStatus(false);
        }

        return baseDto;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteNotify", method = RequestMethod.GET, params = {"taskId"})
    public BaseDto<BaseDataDto> deleteNotify(String taskId) {
        redisWrapperClient.hdelSeri(TaskConstant.NOTIFY_KEY + LoginUserInfo.getLoginName(), taskId);

        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setSuccess(true);
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDataDto.setStatus(true);
        baseDto.setData(baseDataDto);
        return baseDto;
    }

}
