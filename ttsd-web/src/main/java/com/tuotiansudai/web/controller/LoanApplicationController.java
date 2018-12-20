package com.tuotiansudai.web.controller;

import com.tuotiansudai.client.OssWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanApplicationDto;
import com.tuotiansudai.dto.LoanConsumeApplicationDto;
import com.tuotiansudai.repository.model.LoanApplicationRegion;
import com.tuotiansudai.repository.model.PledgeType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.LoanApplicationService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.IdentityNumberValidator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/loan-application")
public class LoanApplicationController {

    private static Logger logger = Logger.getLogger(LoanApplicationController.class);

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Autowired
    private UserService userService;

    @Autowired
    private OssWrapperClient ossWrapperClient;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("loan-application", "responsive", true);

        String mobile = LoginUserInfo.getMobile();
        if (!StringUtils.isEmpty(mobile)) {
            modelAndView.addObject("mobile", mobile);
            UserModel userModel = userService.findByMobile(mobile);
            modelAndView.addObject("userName", userModel.getUserName());
        }
        modelAndView.addObject("isAnxinProp", !StringUtils.isEmpty(mobile) && loanApplicationService.isAnxinProp(LoginUserInfo.getLoginName()));
        modelAndView.addObject("regions", LoanApplicationRegion.values());
        return modelAndView;
    }

    @RequestMapping(value = "/borrow-apply", method = RequestMethod.GET)
    public ModelAndView loanApplication(@RequestParam("type")PledgeType pledgeType) {
        ModelAndView modelAndView = new ModelAndView(pledgeType == PledgeType.NONE ? "loan-consume-apply" : "loan-borrow-apply", "responsive", true);
        UserModel userModel = userService.findByMobile(LoginUserInfo.getMobile());
        modelAndView.addObject("identityNumber", userModel.getIdentityNumber());
        modelAndView.addObject("age", IdentityNumberValidator.getAgeByIdentityCard(userModel.getIdentityNumber(),18));
        modelAndView.addObject("sex", "MALE".equalsIgnoreCase(IdentityNumberValidator.getSexByIdentityCard(userModel.getIdentityNumber(),"MALE"))?"男":"女");
        modelAndView.addObject("mobile", userModel.getMobile());
        modelAndView.addObject("address", IdentityNumberValidator.getCityByIdentityCard(userModel.getIdentityNumber()));
        modelAndView.addObject("userName", userModel.getUserName());
        modelAndView.addObject("pledgeType", pledgeType);
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> create(@RequestBody LoanApplicationDto loanApplicationDto) {
        String loginName = LoginUserInfo.getLoginName();
        loanApplicationDto.setLoginName(null == LoginUserInfo.getLoginName() ? "" : loginName);
        return loanApplicationService.create(loanApplicationDto);
    }

    @RequestMapping(value = "/create-consume", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> createConsume(@RequestBody LoanConsumeApplicationDto loanConsumeApplicationDto) {
        loanConsumeApplicationDto.setLoginName(LoginUserInfo.getLoginName());
        return loanApplicationService.createConsume(loanConsumeApplicationDto);
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView success() {
        return new ModelAndView("loan-application-success");
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> uploadFile(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("status", "SUCCESS");
        if (!ServletFileUpload.isMultipartContent(request)) {
            map.put("status", "FAIL");
            return map;
        }
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile dfi = multiRequest.getFile("upfile");
        String originalName = dfi.getOriginalFilename().substring(dfi.getOriginalFilename().lastIndexOf(System.getProperty("file.separator")) + 1);
        String fileExtName = FilenameUtils.getExtension(originalName);
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        try {
            String url = request.getRequestURL().toString();
            String absoluteUrl = ossWrapperClient.upload(fileExtName, dfi.getInputStream(), rootPath, url.substring(0,url.lastIndexOf("/")+1), false);
            if (absoluteUrl.indexOf(":") > 0 ) {
                absoluteUrl = absoluteUrl.substring(absoluteUrl.indexOf("/upload"), absoluteUrl.length());
            }
            map.put("ImgUrl", absoluteUrl);
        } catch (Exception e) {
            logger.error(MessageFormat.format("{0}|{1}", "[LOAN APPLICATION OSS UPLOAD]", e.getLocalizedMessage()), e);
            map.put("status", "FAIL");
        }
        return map;
    }
}
