package com.tuotiansudai.console.service;

import com.google.common.base.Strings;
import com.tuotiansudai.console.dto.PayrollDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.PayrollDetailMapper;
import com.tuotiansudai.repository.mapper.PayrollMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import com.tuotiansudai.util.UUIDGenerator;
import net.sf.json.JSONArray;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ConsolePayrollService {

    static Logger logger = Logger.getLogger(ConsolePayrollService.class);

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PayrollMapper payrollMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PayrollDetailMapper payrollDetailMapper;

    private static final String redisKey = "payroll:data:{0}";

    @Transactional
    public void createPayroll(String loginName, PayrollDataDto payrollDataDto) {
        PayrollModel payrollModel = new PayrollModel(payrollDataDto.getTitle(),payrollDataDto.getTotalAmount(),payrollDataDto.getHeadCount());
        payrollModel.setCreatedBy(loginName);
        payrollMapper.create(payrollModel);
        String existsRedisKey = MessageFormat.format(redisKey,payrollDataDto.getUuid());
        String payrollDetail = redisWrapperClient.hget(existsRedisKey,"payrolldetail");
        List<PayrollDetailModel> payrollDetailModelList=(List<PayrollDetailModel>)JSONArray.toList(JSONArray.fromObject(payrollDetail), PayrollDetailModel.class);

        for (PayrollDetailModel payrollDetailModel :payrollDetailModelList) {
            payrollDetailModel.setPayrollId(payrollModel.getId());
            payrollDetailModel.setStatus(PayrollStatusType.PENDING);
            payrollDetailMapper.create(payrollDetailModel);
        }
        redisWrapperClient.del(existsRedisKey);
    }

    @Transactional
    public void updatePayroll(String loginName, PayrollDataDto payrollDataDto) {
        PayrollModel payrollModel = payrollMapper.findById(payrollDataDto.getId());
        payrollModel.setTitle(payrollDataDto.getTitle());
        payrollModel.setTotalAmount(payrollDataDto.getTotalAmount());
        payrollModel.setHeadCount(payrollDataDto.getHeadCount());
        payrollModel.setUpdatedBy(loginName);
        payrollModel.setUpdatedTime(new Date());
        payrollMapper.update(payrollModel);

        if(!Strings.isNullOrEmpty(payrollDataDto.getUuid())){
            String existsRedisKey = MessageFormat.format(redisKey,payrollDataDto.getUuid());
            String payrollDetail = redisWrapperClient.hget(existsRedisKey,"payrolldetail");
            List<PayrollDetailModel> payrollDetailModelList=(List<PayrollDetailModel>)JSONArray.toList(JSONArray.fromObject(payrollDetail), PayrollDetailModel.class);

            payrollDetailMapper.deleteByPayrollId(payrollModel.getId());
            for (PayrollDetailModel payrollDetailModel :payrollDetailModelList) {
                payrollDetailModel.setPayrollId(payrollModel.getId());
                payrollDetailModel.setStatus(PayrollStatusType.PENDING);
                payrollDetailMapper.create(payrollDetailModel);
            }
            redisWrapperClient.del(existsRedisKey);
        }
    }

    public PayrollModel findById(long id){
        return payrollMapper.findById(id);
    }

    public List<PayrollDetailModel> findByPayrollId(long payrollId){
        return payrollDetailMapper.findByPayrollId(payrollId);
    }

    public PayrollDataDto importPayrollUserList(InputStream inputStream) throws Exception {
        PayrollDataDto payrollDataDto = new PayrollDataDto();
        List<String> listUserNotExists = new ArrayList<>();
        List<String> listUserAndUserNameNotMatch = new ArrayList<>();
        List<String> listUserNotAccount = new ArrayList<>();
        List<String> listUserAmountError = new ArrayList<>();
        List<PayrollDetailModel> payrollDetailModelList = new ArrayList<>();
        List<String> loginNameList = new ArrayList<>();
        long totalAmount = 0;
        long headCount = 0;
        try {
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
                if (!isAmount(arrayData[2])) {
                    listUserAmountError.add(arrayData[1]);
                    continue;
                }
                totalAmount += AmountConverter.convertStringToCent(arrayData[2]);
                headCount++;
                loginNameList.add(userModel.getLoginName());
                payrollDetailModelList.add(new PayrollDetailModel(userModel.getLoginName(), arrayData[0], arrayData[1], AmountConverter.convertStringToCent(arrayData[2])));
            }
        } catch (IOException e) {
            return new PayrollDataDto(false,"上传失败!文件内容错误");
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }

        if (CollectionUtils.isNotEmpty(listUserNotExists) || CollectionUtils.isNotEmpty(listUserAndUserNameNotMatch)
                || CollectionUtils.isNotEmpty(listUserNotAccount) || CollectionUtils.isNotEmpty(listUserAmountError)) {
            payrollDataDto.setStatus(false);
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
            payrollDataDto.setMessage(msg);
        } else {
            payrollDataDto.setStatus(true);
            payrollDataDto.setTotalAmount(totalAmount);
            payrollDataDto.setHeadCount(headCount);
            payrollDataDto.setPayrollDetailModelList(payrollDetailModelList);
            String uuid = UUIDGenerator.generate();
            payrollDataDto.setUuid(uuid);
            redisWrapperClient.hset(MessageFormat.format(redisKey, uuid),"payrolldetail",convertJavaListToString(payrollDetailModelList));
            payrollDataDto.setMessage("导入发放名单成功!");
        }
        return payrollDataDto;

    }

    private boolean isAmount(String str) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    private String convertJavaListToString(List<PayrollDetailModel> payrollDetailModelList){
        JSONArray jsArr = JSONArray.fromObject(payrollDetailModelList);
        return jsArr.toString();
    }

}
