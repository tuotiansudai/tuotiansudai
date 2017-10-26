package com.tuotiansudai.console.service;

import com.google.common.base.Strings;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.PayrollDetailMapper;
import com.tuotiansudai.repository.mapper.PayrollMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.PayrollDetailModel;
import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ConsolePayrollService {

    static Logger logger = Logger.getLogger(ConsolePayrollService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PayrollMapper payrollMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PayrollDetailMapper payrollDetailMapper;

    @Transactional
    private void createPayroll(PayrollModel payrollModel, List<PayrollDetailModel> payrollDetailModelList) {
        payrollMapper.create(payrollModel);

        for (PayrollDetailModel payrollDetailModel :payrollDetailModelList) {
            payrollDetailModel.setPayrollId(payrollModel.getId());
            payrollDetailMapper.create(payrollDetailModel);
        }
    }

    public BaseDataDto importPayrollUserList(HttpServletRequest httpServletRequest) throws Exception {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        String title = httpServletRequest.getParameter("title");
        InputStream inputStream = null;
        BaseDataDto baseDataDto = new BaseDataDto();
        List<String> listUserNotExists = new ArrayList<>();
        List<String> listUserAndUserNameNotMatch = new ArrayList<>();
        List<String> listUserNotAccount = new ArrayList<>();
        List<String> listUserAmountError = new ArrayList<>();
        List<PayrollDetailModel> payrollDetailModelList = new ArrayList<>();
        long totalAmount = 0;
        long headCount = 0;
        if (Strings.isNullOrEmpty(title)) {
            return new BaseDataDto(false,"标题不能为空!");
        }
        if (!multipartFile.getOriginalFilename().endsWith(".csv")) {
            return new BaseDataDto(false,"上传失败!文件必须是csv格式");
        }
        try {
            inputStream = multipartFile.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String strVal;

            while (null != (strVal = bufferedReader.readLine())) {
                String arrayData[] = strVal.split(",");
                UserModel userModel = userMapper.findByMobile(arrayData[1]);
                if (userModel == null) {
                    listUserNotExists.add(arrayData[1]);
                    continue;
                }
                if (!userModel.getUserName().equals(arrayData[0])) {
                    listUserAndUserNameNotMatch.add(arrayData[1]);
                    continue;
                }
                AccountModel accountModel = accountMapper.findByMobile(arrayData[1]);
                if (accountModel == null) {
                    listUserNotAccount.add(arrayData[1]);
                    continue;
                }
                if (!isNumber(arrayData[2])) {
                    listUserAmountError.add(arrayData[1]);
                    continue;
                }
                totalAmount += AmountConverter.convertStringToCent(arrayData[2]);
                headCount++;
                payrollDetailModelList.add(new PayrollDetailModel(arrayData[0], arrayData[1], AmountConverter.convertStringToCent(arrayData[2])));
            }
        } catch (IOException e) {
            return new BaseDataDto(false,"上传失败!文件内容错误");
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }

        if (CollectionUtils.isNotEmpty(listUserNotExists) || CollectionUtils.isNotEmpty(listUserAndUserNameNotMatch)
                || CollectionUtils.isNotEmpty(listUserNotAccount) || CollectionUtils.isNotEmpty(listUserAmountError)) {
            baseDataDto.setStatus(false);
            String msg = "导入发放名单失败!<br/> ";
            if (CollectionUtils.isNotEmpty(listUserNotExists)) {
                msg += StringUtils.join(listUserNotExists, ",") + " 用户不存在<br/>";
            }
            if (CollectionUtils.isNotEmpty(listUserAndUserNameNotMatch)) {
                msg += StringUtils.join(listUserAndUserNameNotMatch, ",") + " 姓名与手机号不匹配<br/>";
            }
            if (CollectionUtils.isNotEmpty(listUserNotAccount)) {
                msg += StringUtils.join(listUserNotExists, ",") + " 未实名认证";
            }
            if (CollectionUtils.isNotEmpty(listUserAmountError)) {
                msg += StringUtils.join(listUserAmountError, ",") + " 金额不正确";
            }
            baseDataDto.setMessage(msg);
        } else {
            PayrollModel payrollModel = new PayrollModel(title,totalAmount,headCount);
            this.createPayroll(payrollModel,payrollDetailModelList);
            baseDataDto.setStatus(true);
            baseDataDto.setMessage("导入发放名单成功!");
        }
        return baseDataDto;

    }

    private boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }
}
