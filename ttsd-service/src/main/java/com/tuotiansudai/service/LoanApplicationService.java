package com.tuotiansudai.service;

import cn.jpush.api.utils.StringUtils;
import com.tuotiansudai.client.OssWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanApplicationDto;
import com.tuotiansudai.dto.LoanConsumeApplicationDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanApplicationMapper;
import com.tuotiansudai.repository.model.LoanApplicationMaterialsModel;
import com.tuotiansudai.repository.model.LoanApplicationModel;
import com.tuotiansudai.repository.model.PledgeType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.IdentityNumberValidator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

@Service
public class LoanApplicationService {

    private static Logger logger = Logger.getLogger(LoanApplicationService.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OssWrapperClient ossWrapperClient;

    public BaseDto<BaseDataDto> create(LoanApplicationDto loanApplicationDto) {
        BaseDto<BaseDataDto> baseDataDtoBaseDto = checkLoanApplication(loanApplicationDto);
        if (baseDataDtoBaseDto.isSuccess()){
            this.createLoanApplication(loanApplicationDto);
        }
        return baseDataDtoBaseDto;
    }

    private BaseDto<BaseDataDto> checkLoanApplication(LoanApplicationDto loanApplicationDto){
        if (null == accountMapper.findByLoginName(loanApplicationDto.getLoginName())) {
            return new BaseDto<>(new BaseDataDto(false, "账户没有实名认证"));
        }
        if (loanApplicationDto.getAmount() <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "借款金额必须是大于等于1的整数"));
        }
        if (loanApplicationDto.getPeriod() <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "借款期限必须是大于等于1的整数"));
        }
        if(StringUtils.isEmpty(loanApplicationDto.getLoanUsage())){
            return new BaseDto<>(new BaseDataDto(false, "借款用途不能为空"));
        }
        if(loanApplicationDto.getPledgeType() != PledgeType.NONE && StringUtils.isEmpty(loanApplicationDto.getPledgeInfo())){
            return new BaseDto<>(new BaseDataDto(false, "抵押物信息不能为空"));
        }
        return new BaseDto<>(new BaseDataDto(true));
    }

    private LoanApplicationModel createLoanApplication(LoanApplicationDto loanApplicationDto){
        LoanApplicationModel loanApplicationModel = new LoanApplicationModel(loanApplicationDto);
        UserModel userModel = userMapper.findByLoginName(loanApplicationDto.getLoginName());
        loanApplicationModel.setMobile(userModel.getMobile());
        loanApplicationModel.setUserName(userModel.getUserName());
        loanApplicationModel.setIdentityNumber(userModel.getIdentityNumber());
        loanApplicationModel.setAge((short)IdentityNumberValidator.getAgeByIdentityCard(userModel.getIdentityNumber(),18));
        loanApplicationModel.setAddress(IdentityNumberValidator.getCityByIdentityCard(userModel.getIdentityNumber()));
        loanApplicationModel.setSex("MALE".equalsIgnoreCase(IdentityNumberValidator.getSexByIdentityCard(userModel.getIdentityNumber(),"MALE"))?"男":"女");
        loanApplicationMapper.create(loanApplicationModel);
        return loanApplicationModel;
    }

    public BaseDto<BaseDataDto> createConsume(LoanConsumeApplicationDto loanConsumeApplicationDto){
        BaseDto<BaseDataDto> baseDataDtoBaseDto = this.checkLoanApplication(loanConsumeApplicationDto);
        if (!baseDataDtoBaseDto.isSuccess()){
            return baseDataDtoBaseDto;
        }
        if (StringUtils.isEmpty(loanConsumeApplicationDto.getIdentityProveUrls())) {
            return new BaseDto<>(new BaseDataDto(false, "身份证明材料不能为空"));
        }
        if (StringUtils.isEmpty(loanConsumeApplicationDto.getIncomeProveUrls())) {
            return new BaseDto<>(new BaseDataDto(false, "收入证明材料不能为空"));
        }
        if (StringUtils.isEmpty(loanConsumeApplicationDto.getCreditProveUrls())) {
            return new BaseDto<>(new BaseDataDto(false, "信用报告材料不能为空"));
        }
        if (loanConsumeApplicationDto.getMarriageState() == 1 && StringUtils.isEmpty(loanConsumeApplicationDto.getMarriageProveUrls())) {
            return new BaseDto<>(new BaseDataDto(false, "婚姻状况材料不能为空"));
        }
        if (StringUtils.isEmpty(loanConsumeApplicationDto.getPropertyProveUrls())) {
            return new BaseDto<>(new BaseDataDto(false, "资产证明材料不能为空"));
        }
        if (!StringUtils.isEmpty(loanConsumeApplicationDto.getTogetherLoaner()) && StringUtils.isEmpty(loanConsumeApplicationDto.getTogetherProveUrls())) {
            return new BaseDto<>(new BaseDataDto(false, "共同借款人材料不能为空"));
        }
        LoanApplicationModel loanApplicationModel = this.createLoanApplication(loanConsumeApplicationDto);
        LoanApplicationMaterialsModel loanApplicationMaterialsModel = new LoanApplicationMaterialsModel(loanApplicationModel.getId(), loanConsumeApplicationDto);
        loanApplicationMapper.createMaterials(loanApplicationMaterialsModel);
        return new BaseDto<>(new BaseDataDto(true));
    }

//    private String uploadFile(HttpServletRequest request) {
//        if (!ServletFileUpload.isMultipartContent(request)) {
//            return buildUploadFileResult("未包含文件上传域", "", "", "");
//        }
//        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//        MultipartFile dfi = multiRequest.getFile("upfile");
//        String originalName = dfi.getOriginalFilename().substring(dfi.getOriginalFilename().lastIndexOf(System.getProperty("file.separator")) + 1);
//        String fileExtName = FilenameUtils.getExtension(originalName);
//        String rootPath = request.getSession().getServletContext().getRealPath("/");
//        try {
//            String url = request.getRequestURL().toString();
//            String absoluteUrl = ossWrapperClient.upload(fileExtName, dfi.getInputStream(), rootPath, url.substring(0,url.lastIndexOf("/")+1), false);
//            if (absoluteUrl.indexOf(":") > 0 ) {
//                absoluteUrl = absoluteUrl.substring(absoluteUrl.indexOf("/upload"), absoluteUrl.length());
//            }
//            String relativeUrl = absoluteUrl.substring(absoluteUrl.indexOf("/"), absoluteUrl.length());
//            return buildUploadFileResult("SUCCESS", originalName, relativeUrl, absoluteUrl);
//        } catch (Exception e) {
//            logger.error(MessageFormat.format("{0}|{1}", "[OSS UPLOAD]", e.getLocalizedMessage()), e);
//            return buildUploadFileResult(e.getMessage(), "", "", "");
//        }
//    }
}
