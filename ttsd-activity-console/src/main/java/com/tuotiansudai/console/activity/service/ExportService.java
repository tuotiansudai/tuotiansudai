package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.IPhone7InvestLotteryMapper;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryStatView;
import com.tuotiansudai.activity.service.AutumnService;
import com.tuotiansudai.console.activity.dto.AutumnExportDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.DateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExportService {

    @Autowired
    private AutumnService autumnService;

    @Autowired
    private IPhone7InvestLotteryMapper iPhone7InvestLotteryMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    public List<AutumnExportDto> getAutumnExport() {
        List<AutumnExportDto> autumnExportDtoList = Lists.newArrayList();
        int activityDays = (int) DateUtil.differenceDay(activityAutumnStartTime, activityAutumnEndTime) + 1;
        for (int i = 0; i < activityDays; i++) {
            Date startTime = new DateTime(activityAutumnStartTime).plusDays(i).withTimeAtStartOfDay().toDate();
            Date endTime = new DateTime(activityAutumnStartTime).plusDays(i).withTime(23, 59, 59, 0).toDate();

            Map<String, List<String>> allFamilyAndNum = autumnService.getAllFamilyMap(activityAutumnStartTime, endTime);

            if (allFamilyAndNum.size() == 0) continue;
            for (Map.Entry<String, List<String>> entry1 : allFamilyAndNum.entrySet()) {
                long totalAmount = 0;
                List<InvestModel> currentHomeInvestModelList = Lists.newArrayList();
                for (String loginName : entry1.getValue()) {
                    totalAmount += investMapper.sumInvestAmount(null, loginName, null, null, null, startTime, endTime, InvestStatus.SUCCESS, null);
                    List<InvestModel> investModelList = investMapper.findSuccessInvestByInvestTime(loginName, startTime, endTime);
                    if (investModelList == null || investModelList.size() == 0) {
                        InvestModel investModel = new InvestModel();
                        investModel.setLoginName(loginName);
                        investModel.setAmount(0);
                        investModelList.add(investModel);
                    }
                    currentHomeInvestModelList.addAll(investModelList);
                }
                for (InvestModel investModel : currentHomeInvestModelList) {
                    AutumnExportDto autumnExportDto = new AutumnExportDto();
                    autumnExportDto.setName(entry1.getKey());
                    autumnExportDto.setTotalAmount(totalAmount);
                    autumnExportDto.setInvestTime(startTime);
                    if (totalAmount >= 5000000) {
                        autumnExportDto.setPrize("50元红包");
                    } else if (totalAmount >= 2000000 && totalAmount < 5000000) {
                        autumnExportDto.setPrize("15元红包");
                    } else if (totalAmount >= 1000000 && totalAmount < 2000000) {
                        autumnExportDto.setPrize("5元红包");
                    } else {
                        autumnExportDto.setPrize("");
                    }

                    UserModel userModel = userMapper.findByLoginName(investModel.getLoginName());
                    autumnExportDto.setLoginName(userModel == null ? "" : userModel.getMobile());
                    autumnExportDto.setInvestAmount(investModel.getAmount());
                    autumnExportDtoList.add(autumnExportDto);
                }
            }
        }
        return autumnExportDtoList;
    }

    public List<List<String>> buildAutumnList(List<AutumnExportDto> records) {

        Comparator<AutumnExportDto> comparator = new Comparator<AutumnExportDto>() {
            public int compare(AutumnExportDto autumnExportDto1, AutumnExportDto autumnExportDto2) {
                //按日期
                if (!autumnExportDto1.getName().equals(autumnExportDto2.getName())) {
                    //
                    return (int) (autumnExportDto1.getInvestTime().getTime() - autumnExportDto2.getInvestTime().getTime());
                }
                //日期相同按名称
                else if (autumnExportDto1.getName() != autumnExportDto2.getLoginName()) {
                    return autumnExportDto1.getName().compareTo(autumnExportDto2.getName());

                }
                return -1;
            }
        };

        Collections.sort(records, comparator);
        List<List<String>> rows = Lists.newArrayList();


        for (AutumnExportDto record : records) {
            List<String> row = Lists.newArrayList();
            row.add(record.getName());
            row.add(AmountConverter.convertCentToString(record.getTotalAmount()));
            row.add(new DateTime(record.getInvestTime()).toString("yyyy-MM-dd"));
            row.add(record.getPrize());
            row.add(record.getLoginName());
            row.add(AmountConverter.convertCentToString(record.getInvestAmount()));
            rows.add(row);
        }
        return rows;

    }

    public List<List<String>> iphone7LotteryStat() {
        List<IPhone7InvestLotteryStatView> list = iPhone7InvestLotteryMapper.allStatInvest();
        return list.stream().map(r -> {
            UserModel userModel = userMapper.findByLoginName(r.getLoginName());
            AccountModel accountModel = accountMapper.findByLoginName(r.getLoginName());
            return Arrays.asList(
                    userModel.getMobile(),
                    accountModel.getUserName(),
                    new DecimalFormat("0.00").format(((double) r.getInvestAmountTotal()) / 100),
                    String.valueOf(r.getInvestCount()));
        }).collect(Collectors.toList());
    }
}
