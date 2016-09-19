package com.tuotiansudai.console.controller;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.membership.dto.UserMembershipItemDto;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.MembershipUserGroup;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.membership.service.MembershipExperienceBillService;
import com.tuotiansudai.membership.service.MembershipGiveService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
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

    @Autowired
    private UserMembershipService userMembershipService;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private MembershipGiveService membershipGiveService;

    @RequestMapping(value = "/membership-list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView membershipList(@RequestParam(value = "index", required = true, defaultValue = "1") int index,
                                       @RequestParam(value = "pageSize", required = true, defaultValue = "10") int pageSize,
                                       @RequestParam(value = "loginName", required = false, defaultValue = "") String loginName,
                                       @RequestParam(value = "startTime", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date registerStartTime,
                                       @RequestParam(value = "endTime", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date registerEndTime,
                                       @RequestParam(value = "mobile", required = false, defaultValue = "") String mobile,
                                       @RequestParam(value = "type", required = false, defaultValue = "") UserMembershipType userMembershipType,
                                       @RequestParam(value = "levels", required = false, defaultValue = "") List<Integer> selectedLevels) {

        int count = userMembershipMapper.findCountUserMembershipItemViews(loginName,
                mobile, registerStartTime, registerEndTime, userMembershipType, selectedLevels, (index - 1) * 10, pageSize);

        List<UserMembershipItemDto> userMembershipItemDtos = userMembershipService.getUserMembershipItems(loginName,
                mobile, registerStartTime, registerEndTime, userMembershipType, selectedLevels, (index - 1) * 10, pageSize);

        BasePaginationDataDto<UserMembershipItemDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, userMembershipItemDtos);

        ModelAndView modelAndView = new ModelAndView("/membership-list");
        modelAndView.addObject("data", basePaginationDataDto);

        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("userMembershipTypeList", UserMembershipType.values());
        modelAndView.addObject("levels", userMembershipService.getAllLevels());
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("startTime", registerStartTime);
        modelAndView.addObject("endTime", registerEndTime);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("selectedType", userMembershipType);
        if (CollectionUtils.isEmpty(selectedLevels)) {
            modelAndView.addObject("selectedLevels", "");
        } else {
            modelAndView.addObject("selectedLevels", Joiner.on(',').join(selectedLevels));
        }

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
        modelAndView.addObject("V0Experience", userMembershipService.getMembershipByLevel(0).getExperience());
        modelAndView.addObject("V1Experience", userMembershipService.getMembershipByLevel(1).getExperience());
        modelAndView.addObject("V2Experience", userMembershipService.getMembershipByLevel(2).getExperience());
        modelAndView.addObject("V3Experience", userMembershipService.getMembershipByLevel(3).getExperience());
        modelAndView.addObject("V4Experience", userMembershipService.getMembershipByLevel(4).getExperience());
        modelAndView.addObject("V5Experience", userMembershipService.getMembershipByLevel(5).getExperience());

        modelAndView.addObject("membershipLevel", membershipModel == null ? "0" : membershipModel.getLevel());
        modelAndView.addObject("membershipPoint", accountModel == null ? 0 : accountModel.getMembershipPoint());
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

    @RequestMapping(value = "/give/edit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView membershipDetail() {
        ModelAndView modelAndView = new ModelAndView("/membership-give-edit");
        modelAndView.addObject("userGroups", MembershipUserGroup.values());
        modelAndView.addObject("membershipLevels", Lists.newArrayList(0, 1, 2, 3, 4, 5));

        return modelAndView;
    }
}
