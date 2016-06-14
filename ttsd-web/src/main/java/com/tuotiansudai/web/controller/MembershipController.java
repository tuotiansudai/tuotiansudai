package com.tuotiansudai.web.controller;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillDto;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.MembershipExperienceBillService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.transfer.repository.model.TransferInvestDetailDto;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/membership")
public class MembershipController {

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MembershipExperienceBillService membershipExperienceBillService;

    @Autowired
    private UserMembershipService userMembershipService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/membership-index");

        String loginName = LoginUserInfo.getLoginName();
        if(loginName != null){
            MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
            MembershipModel nextLevelMembershipModel = membershipModel.getLevel() == 5?membershipModel:userMembershipService.getMembershipByLevel(membershipModel.getLevel() + 1);
            AccountModel accountModel = accountService.findByLoginName(loginName);

            modelAndView.addObject("membershipLevel", membershipModel.getLevel());
            modelAndView.addObject("membershipNextLevel", nextLevelMembershipModel.getLevel());
            modelAndView.addObject("membershipNextLevelValue", (nextLevelMembershipModel.getExperience() - accountModel.getMembershipPoint()));
            modelAndView.addObject("membershipPoint", accountModel != null?accountModel.getMembershipPoint():"");
            modelAndView.addObject("progressBarPercent", userMembershipService.getProgressBarPercent(loginName));
            modelAndView.addObject("leftDays", userMembershipService.getExpireDayByLoginName(loginName));
        }
        modelAndView.addObject("loginName", loginName);

        return modelAndView;

    }

    @RequestMapping(path = "/structure", method = RequestMethod.GET)
    public ModelAndView structure() {
        ModelAndView modelAndView = new ModelAndView("/membership-structure");

        String loginName = LoginUserInfo.getLoginName();
        if(loginName != null){
            AccountModel accountModel = accountService.findByLoginName(loginName);
            MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);

            modelAndView.addObject("membershipLevel", membershipModel != null?membershipModel.getLevel():"");
            modelAndView.addObject("membershipPoint", accountModel != null?accountModel.getMembershipPoint():"");
        }
        modelAndView.addObject("loginName", loginName);

        return modelAndView;
    }

    @RequestMapping(path = "/privilege", method = RequestMethod.GET)
    public ModelAndView privilege() {
        ModelAndView modelAndView = new ModelAndView("/membership-privilege");

        return modelAndView;
    }


    @ResponseBody
    @RequestMapping(value = "/structure-list-data", method = RequestMethod.GET)
    public List<MembershipExperienceBillDto> structureListData(@RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                          @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {

        String loginName = LoginUserInfo.getLoginName();
        List<MembershipExperienceBillModel> items = membershipExperienceBillService.findMembershipExperienceBillList(loginName, startTime, endTime, null, null);
        List<MembershipExperienceBillDto> records = Lists.transform(items, new Function<MembershipExperienceBillModel, MembershipExperienceBillDto>() {
            @Override
            public MembershipExperienceBillDto apply(MembershipExperienceBillModel input) {
                return new MembershipExperienceBillDto(input);
            }
        });

        return records;
    }
}
