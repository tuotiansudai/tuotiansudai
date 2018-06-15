package com.tuotiansudai.console.controller;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.membership.dto.MembershipGiveDto;
import com.tuotiansudai.membership.dto.MembershipGiveReceiveDto;
import com.tuotiansudai.membership.dto.UserMembershipItemDto;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.MembershipUserGroup;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.membership.service.*;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.service.BankAccountService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
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
    private BankAccountService bankAccountService;

    @Autowired
    private UserMembershipService userMembershipService;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private MembershipGiveService membershipGiveService;

    @Autowired
    private ImportService importService;

    @RequestMapping(value = "/membership-list", method = RequestMethod.GET)
    public ModelAndView membershipList(@RequestParam(value = "index", required = true, defaultValue = "1") int index,
                                       @RequestParam(value = "loginName", required = false, defaultValue = "") String loginName,
                                       @RequestParam(value = "startTime", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date registerStartTime,
                                       @RequestParam(value = "endTime", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date registerEndTime,
                                       @RequestParam(value = "mobile", required = false, defaultValue = "") String mobile,
                                       @RequestParam(value = "type", required = false, defaultValue = "") UserMembershipType userMembershipType,
                                       @RequestParam(value = "levels", required = false, defaultValue = "") List<Integer> selectedLevels) {
        int pageSize = 10;
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
    public ModelAndView membershipDetail(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                         @RequestParam(value = "loginName") String loginName) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/membership-detail");
        long membershipExperienceCount = membershipExperienceBillService.findMembershipExperienceBillCount(loginName, null, null);
        List<MembershipExperienceBillModel> membershipExperienceList = membershipExperienceBillService.findMembershipExperienceBillList(loginName, null, null, index, pageSize);
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        BankAccountModel bankAccountModel = bankAccountService.findBankAccount(loginName);
        modelAndView.addObject("membershipExperienceCount", membershipExperienceCount);
        modelAndView.addObject("membershipExperienceList", membershipExperienceList);
        modelAndView.addObject("V0Experience", userMembershipService.getMembershipByLevel(0).getExperience());
        modelAndView.addObject("V1Experience", userMembershipService.getMembershipByLevel(1).getExperience());
        modelAndView.addObject("V2Experience", userMembershipService.getMembershipByLevel(2).getExperience());
        modelAndView.addObject("V3Experience", userMembershipService.getMembershipByLevel(3).getExperience());
        modelAndView.addObject("V4Experience", userMembershipService.getMembershipByLevel(4).getExperience());
        modelAndView.addObject("V5Experience", userMembershipService.getMembershipByLevel(5).getExperience());

        modelAndView.addObject("membershipLevel", membershipModel == null ? "0" : membershipModel.getLevel());
        modelAndView.addObject("membershipPoint", bankAccountModel == null ? 0 : bankAccountModel.getMembershipPoint());
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = PaginationUtil.calculateMaxPage(membershipExperienceCount, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("loginName", loginName);

        return modelAndView;
    }

    @RequestMapping(value = "/give/edit-view", method = RequestMethod.GET)
    public ModelAndView membershipDetail() {
        ModelAndView modelAndView = new ModelAndView("/membership-give-edit");
        modelAndView.addObject("userGroups", MembershipUserGroup.values());
        modelAndView.addObject("membershipLevels", Lists.newArrayList(userMembershipService.getAllLevels()));

        return modelAndView;
    }

    @RequestMapping(value = "/give/edit-view/{membershipGiveId}", method = RequestMethod.GET)
    public ModelAndView membershipDetail(@PathVariable long membershipGiveId) {
        ModelAndView modelAndView = new ModelAndView("/membership-give-edit");

        MembershipGiveDto membershipGiveDto = membershipGiveService.getMembershipGiveDtoById(membershipGiveId);
        modelAndView.addObject("membershipGiveDto", membershipGiveDto);

        modelAndView.addObject("userGroups", MembershipUserGroup.values());
        modelAndView.addObject("membershipLevels", Lists.newArrayList(userMembershipService.getAllLevels()));

        return modelAndView;
    }

    @RequestMapping(value = "/give/{membershipGiveId}/details", method = RequestMethod.GET)
    public ModelAndView membershipGiveReceiveDetails(@PathVariable long membershipGiveId,
                                                     @RequestParam(value = "mobile", defaultValue = "") String mobile,
                                                     @RequestParam(value = "index", defaultValue = "1") int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/membership-give-receive-detail");
        BasePaginationDataDto<MembershipGiveReceiveDto> basePaginationDataDto = membershipGiveService.getMembershipGiveReceiveDtosByMobile(membershipGiveId, mobile, index, pageSize);

        modelAndView.addObject("dataDto", basePaginationDataDto);
        modelAndView.addObject("selectMobile", mobile);
        modelAndView.addObject("selectGiveId", membershipGiveId);

        return modelAndView;
    }

    @RequestMapping(value = "/give/edit", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> editMembershipGive(@RequestBody MembershipGiveDto membershipGiveDto,
                                                   @RequestParam(value = "importUsersId") long importUsersId) {
        membershipGiveDto.setCreatedBy(LoginUserInfo.getLoginName());
        membershipGiveService.createAndEditMembershipGive(membershipGiveDto, importUsersId);

        return new BaseDto<>(new BaseDataDto(true));
    }

    @RequestMapping(value = "/give/import-users/{importUsersId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> importUsers(@PathVariable long importUsersId,
                                            HttpServletRequest httpServletRequest) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        if (null == multipartFile) {
            return new BaseDto<>();
        }
        if (!multipartFile.getOriginalFilename().endsWith(".csv")) {
            return new BaseDto<>(new BaseDataDto(false, "上传失败!文件必须是csv格式"));
        }

        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            baseDto = membershipGiveService.importGiveUsers(importUsersId, inputStream);
        } catch (IOException e) {
            baseDto.setData(new BaseDataDto(false, "上传文件失败"));
        }
        return baseDto;
    }

    @RequestMapping(value = "/give/list", method = RequestMethod.GET)
    public ModelAndView getMembershipGives(@RequestParam(value = "index", defaultValue = "1") int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/membership-give-list");
        BasePaginationDataDto<MembershipGiveDto> basePaginationDataDto = membershipGiveService.getMembershipGiveDtos(index, pageSize);

        modelAndView.addObject("dataDto", basePaginationDataDto);

        return modelAndView;
    }

    @RequestMapping(value = "/give/approve/{membershipGiveId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> approveMembershipGive(@PathVariable long membershipGiveId) {
        return membershipGiveService.approveMembershipGive(membershipGiveId, LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/give/cancel/{membershipGiveId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> cancelMembershipGive(@PathVariable long membershipGiveId) {
        return membershipGiveService.cancelMembershipGive(membershipGiveId, LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/give/importUsersList/{importUsersId}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getImportUsersList(@PathVariable long importUsersId) {
        List<String> importUsers = importService.getImportStrings(ImportService.redisMembershipGiveReceivers, importUsersId);
        return importUsers;
    }
}
