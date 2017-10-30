package com.tuotiansudai.console.service;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.console.dto.PayrollDataDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

@Service
public class ConsolePayrollService {

    static Logger logger = Logger.getLogger(ConsoleLoanService.class);
    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PayrollMapper payrollMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private PayrollDetailMapper payrollDetailMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    private static final String redisKey = "payroll:data:{0}";

    private static final int LEFT_SECONDS = 60 * 60 * 24;

    @Transactional
    public BaseDto<BaseDataDto> primaryAudit(long payRollId, String loginName) {
        PayrollModel payrollModel = payrollMapper.findById(payRollId);
        if (payrollModel == null
                || !Sets.newHashSet(PayrollStatusType.PENDING, PayrollStatusType.REJECTED).contains(payrollModel.getStatus())) {
            logger.debug("payRollId not exist or status no pending rejected ");
            return new BaseDto<>(new BaseDataDto(false, "状态不正确!"));
        }
        payrollModel.setUpdatedBy(loginName);
        payrollModel.setUpdatedTime(new Date());
        payrollModel.setStatus(PayrollStatusType.AUDITED);
        payrollMapper.update(payrollModel);
        return new BaseDto<>(new BaseDataDto(true));
    }

    @Transactional
    public BaseDto<BaseDataDto> advancedAudit(long payRollId, String loginName) {
        if (!isSufficientBalance(payRollId)) {
            logger.info("system balance is not sufficient");
            return new BaseDto<>(new BaseDataDto(false, "系统账户余额不足!"));
        }
        PayrollModel payrollModel = payrollMapper.findById(payRollId);
        payrollModel.setUpdatedBy(loginName);
        payrollModel.setUpdatedTime(new Date());
        payrollModel.setStatus(PayrollStatusType.AUDITED);
        payrollMapper.update(payrollModel);
        logger.info(String.format("%s send payroll message begin ...", String.valueOf(payRollId)));
        beginPayroll(payRollId);
        logger.info(String.format("%s send payroll message end ...", String.valueOf(payRollId)));

        return new BaseDto<>(new BaseDataDto(true));
    }

    @Transactional
    public void reject(long payRollId, String loginName) {
        PayrollModel payrollModel = payrollMapper.findById(payRollId);
        if (payrollModel == null
                || !Sets.newHashSet(PayrollStatusType.PENDING, PayrollStatusType.AUDITED).contains(payrollModel.getStatus())) {
            logger.debug("payRollId not exist or status no pending audited ");
            return;
        }
        payrollModel.setUpdatedBy(loginName);
        payrollModel.setUpdatedTime(new Date());
        payrollModel.setStatus(PayrollStatusType.REJECTED);
        payrollMapper.update(payrollModel);

    }

    private boolean isSufficientBalance(long payRollId) {
        long systemBalance = obtainSystemBalanceFromUmp();

        return systemBalance > 0 && systemBalance >= sumPayingAmount(payRollId);
    }

    private long obtainSystemBalanceFromUmp() {
        Map<String, String> systemInfo = payWrapperClient.getPlatformStatus();

        return systemInfo != null && systemInfo.containsKey("账户余额")
                ? AmountConverter.convertStringToCent(systemInfo.get("账户余额")) : 0l;

    }

    private long sumPayingAmount(long payRollId) {
        List<PayrollDetailModel> details = payrollDetailMapper.findByPayrollId(payRollId);

        return details.stream().map(detail -> detail.getAmount()).reduce(0l, Long::sum).longValue();
    }

    @Transactional
    public void createPayroll(String loginName, PayrollDataDto payrollDataDto) {
        PayrollModel payrollModel = new PayrollModel(payrollDataDto.getTitle(), payrollDataDto.getTotalAmount(), payrollDataDto.getHeadCount());
        payrollModel.setCreatedBy(loginName);
        payrollMapper.create(payrollModel);
        payrollDataDto.setId(payrollModel.getId());
        this.insertPayrollDetail(payrollDataDto, payrollModel);
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

        if (!Strings.isNullOrEmpty(payrollDataDto.getUuid())) {
            this.insertPayrollDetail(payrollDataDto, payrollModel);
        }
    }

    public PayrollModel findById(long id) {
        return payrollMapper.findById(id);
    }

    public List<PayrollDetailModel> findByPayrollId(long payrollId) {
        return payrollDetailMapper.findByPayrollId(payrollId);
    }

    public PayrollDataDto importPayrollUserList(InputStream inputStream) throws Exception {
        PayrollDataDto payrollDataDto = new PayrollDataDto();
        List<String> listUserNotExists = new ArrayList<>();
        List<String> listUserAndUserNameNotMatch = new ArrayList<>();
        List<String> listUserNotAccount = new ArrayList<>();
        List<String> listUserAmountError = new ArrayList<>();
        List<PayrollDetailModel> payrollDetailModelList = new ArrayList<>();
        long totalAmount = 0;
        long headCount = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String strVal;

            while (null != (strVal = bufferedReader.readLine())) {
                String arrayData[] = strVal.split(",");
                UserModel userModel = userMapper.findByMobile(arrayData[1].trim());
                if (userModel == null) {
                    listUserNotExists.add(arrayData[1].trim());
                    continue;
                }
                if (!userModel.getUserName().equals(arrayData[0].trim())) {
                    listUserAndUserNameNotMatch.add(arrayData[1].trim());
                    continue;
                }
                AccountModel accountModel = accountMapper.findByMobile(arrayData[1].trim());
                if (accountModel == null) {
                    listUserNotAccount.add(arrayData[1].trim());
                    continue;
                }
                if (!isAmount(arrayData[2].trim())) {
                    listUserAmountError.add(arrayData[1].trim());
                    continue;
                }
                totalAmount += AmountConverter.convertStringToCent(arrayData[2].trim());
                headCount++;
                payrollDetailModelList.add(new PayrollDetailModel(userModel.getLoginName(), arrayData[0].trim(), arrayData[1].trim(), AmountConverter.convertStringToCent(arrayData[2].trim())));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return new PayrollDataDto(false, "上传失败!请检查文件的列数");
        } catch (IOException e) {
            return new PayrollDataDto(false, "上传失败!文件内容读取错误");
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
            redisWrapperClient.setex(MessageFormat.format(redisKey, uuid), LEFT_SECONDS, convertJavaListToString(payrollDetailModelList));
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

    private void insertPayrollDetail(PayrollDataDto payrollDataDto, PayrollModel payrollModel) {
        String existsRedisKey = MessageFormat.format(redisKey, payrollDataDto.getUuid());
        String payrollDetail = redisWrapperClient.get(existsRedisKey);
        List<PayrollDetailModel> payrollDetailModelList = (List<PayrollDetailModel>) JSONArray.toList(JSONArray.fromObject(payrollDetail), PayrollDetailModel.class);
        payrollDetailModelList.stream().forEach(n -> {
            n.setPayrollId(payrollModel.getId());
            n.setStatus(PayrollPayStatus.WAITING);
        });
        payrollDetailMapper.deleteByPayrollId(payrollModel.getId());
        payrollDetailMapper.create(payrollDetailModelList);
        redisWrapperClient.del(existsRedisKey);
    }

    private String convertJavaListToString(List<PayrollDetailModel> payrollDetailModelList) {
        JSONArray jsArr = JSONArray.fromObject(payrollDetailModelList);
        return jsArr.toString();
    }

    private void beginPayroll(long payrollId) {
        mqWrapperClient.sendMessage(MessageQueue.Payroll, String.valueOf(payrollId));
    }

    public BasePaginationDataDto<PayrollModel> list(Date createStartTime, Date createEndTime,
                                                    Date sendStartTime, Date sendEndTime,
                                                    String amountMin, String amountMax,
                                                    PayrollStatusType payrollStatusType, String title,
                                                    int index, int pageSize) {
        List<PayrollModel> payrollModels = payrollMapper.findPayroll(createStartTime, createEndTime, sendStartTime, sendEndTime,
                Integer.parseInt(amountMin) * 100, Integer.parseInt(amountMax) * 100, payrollStatusType, title);
        int count = payrollModels.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, payrollModels.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }

    public void updateRemark(long id, String remark, String loginName) {
        payrollMapper.updateRemark(id, remark, loginName, new Date());
    }

    public BasePaginationDataDto<PayrollDetailModel> detail(long payrollId, int index, int pageSize) {
        List<PayrollDetailModel> payrollDetailModels = payrollDetailMapper.findByPayrollId(payrollId);
        int count = payrollDetailModels.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, payrollDetailModels.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }
}
