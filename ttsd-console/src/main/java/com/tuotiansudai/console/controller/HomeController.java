package com.tuotiansudai.console.controller;

import com.tuotiansudai.client.AbstractRedisWrapperClient;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.ConsoleHomeService;
import com.tuotiansudai.service.UserRoleService;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskType;
import com.tuotiansudai.task.aspect.ApplicationAspect;
import com.tuotiansudai.util.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    ConsoleHomeService consoleHomeService;

    @Autowired
    AbstractRedisWrapperClient redisWrapperClient;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {

        ModelAndView mav = new ModelAndView("/index");

        mav.addObject("taskList", getTaskList(LoginUserInfo.getLoginName()));

        mav.addObject("userToday", consoleHomeService.getRegisterUserToday());
        mav.addObject("user7Days", consoleHomeService.getRegisterUser7Days());
        mav.addObject("user30Days", consoleHomeService.getRegisterUser30Days());

        mav.addObject("rechargeToday", consoleHomeService.getSumRechargeAmountToday());
        mav.addObject("recharge7Days", consoleHomeService.getSumRechargeAmount7Days());
        mav.addObject("recharge30Days", consoleHomeService.getSumRechargeAmount30Days());

        mav.addObject("withdrawToday", consoleHomeService.getSumWithdrawAmountToday());
        mav.addObject("withdraw7Days", consoleHomeService.getSumWithdrawAmount7Days());
        mav.addObject("withdraw30Days", consoleHomeService.getSumWithdrawAmount30Days());

        mav.addObject("investToday", consoleHomeService.getSumInvestAmountToday());
        mav.addObject("invest7Days", consoleHomeService.getSumInvestAmount7Days());
        mav.addObject("invest30Days", consoleHomeService.getSumInvestAmount30Days());

        mav.addObject("totalInvest", consoleHomeService.getSumInvestAmount());

        return mav;
    }

    private List<OperationTask> getTaskList(String loginName) {
        List<Role> roles = userRoleService.findRoleNameByLoginName(loginName);
        List<OperationTask> taskList = new ArrayList<>();
        for (Role role : roles) {
            List<byte[]> tasks = redisWrapperClient.hgetValuesSeri(ApplicationAspect.TASK_KEY + role);

            for (byte[] bs : tasks) {
                taskList.add((OperationTask) SerializeUtil.deserialize(bs));
            }
        }

        List<byte[]> notifies = redisWrapperClient.hgetValuesSeri(ApplicationAspect.NOTIFY_KEY + loginName);
        for (byte[] bs : notifies) {
            taskList.add((OperationTask) SerializeUtil.deserialize(bs));
        }

        Collections.sort(taskList);
        return taskList;
    }


    @ResponseBody
    @RequestMapping(value = "/refuse", method = RequestMethod.GET, params = "taskId")
    public BaseDto<BaseDataDto> refuseApply(String taskId) {

        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);

        if (redisWrapperClient.hexists(ApplicationAspect.TASK_KEY + Role.OPERATOR_ADMIN, taskId)) {

            OperationTask task = (OperationTask) redisWrapperClient.hgetSeri(ApplicationAspect.TASK_KEY + Role.OPERATOR_ADMIN, taskId);

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

            AccountModel sender = accountService.findByLoginName(senderLoginName);
            String senderRealName = sender != null ? sender.getUserName() : senderLoginName;

            notify.setDescription(senderRealName + "拒绝了您" + operationType.getDescription() + "'" + task.getObjName() + "'的申请。");

            redisWrapperClient.hdelSeri(ApplicationAspect.TASK_KEY + Role.OPERATOR_ADMIN, taskId);
            redisWrapperClient.hsetSeri(ApplicationAspect.NOTIFY_KEY + task.getSender(), notifyId, notify);

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
        redisWrapperClient.hdelSeri(ApplicationAspect.NOTIFY_KEY + LoginUserInfo.getLoginName(), taskId);

        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setSuccess(true);
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDataDto.setStatus(true);
        baseDto.setData(baseDataDto);
        return baseDto;
    }

}
