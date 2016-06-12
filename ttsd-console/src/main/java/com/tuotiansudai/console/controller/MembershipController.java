package com.tuotiansudai.console.controller;

import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.MembershipExperienceBillService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
@RequestMapping(value = "/membership-manage")
public class MembershipController {

    @Autowired
    private MembershipExperienceBillService membershipExperienceBillService;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/membership-list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView membershipList(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize){
        ModelAndView modelAndView = new ModelAndView("/membership-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);

        return modelAndView;
    }

    @RequestMapping(value = "/membership-detail", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView membershipDetail(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                         @RequestParam(value = "loginName") String loginName) {
        ModelAndView modelAndView = new ModelAndView("/membership-detail");

        long membershipExperienceCount = membershipExperienceBillService.findMembershipExperienceBillCount(loginName, null, null);
        List<MembershipExperienceBillModel> membershipExperienceList = membershipExperienceBillService.findMembershipExperienceBillList(loginName, null, null, index, pageSize);
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        AccountModel accountModel = accountService.findByLoginName(loginName);
        modelAndView.addObject("membershipExperienceCount", membershipExperienceCount);
        modelAndView.addObject("membershipExperienceList", membershipExperienceList);
        modelAndView.addObject("membershipLevel", membershipModel == null?"V0":"V" + membershipModel.getLevel());
        modelAndView.addObject("membershipPoint", accountModel == null?0:accountModel.getMembershipPoint());
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = membershipExperienceCount / pageSize + (membershipExperienceCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("loginName", loginName);

        return modelAndView;
    }
}
