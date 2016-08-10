package com.tuotiansudai.console.bi.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.console.bi.dto.Granularity;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.bi.dto.UserStage;
import com.tuotiansudai.console.bi.repository.mapper.BusinessIntelligenceMapper;
import com.tuotiansudai.console.bi.repository.model.InvestViscosityDetailTableView;
import com.tuotiansudai.console.bi.repository.model.InvestViscosityDetailView;
import com.tuotiansudai.console.bi.repository.model.KeyValueModel;
import com.tuotiansudai.console.bi.service.BusinessIntelligenceService;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.SerializeUtil;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
public class BusinessIntelligenceServiceImpl implements BusinessIntelligenceService {

    public static final String PLATFORM_REPAY_KEY = "console:platformRepay:list";

    static Logger logger = Logger.getLogger(BusinessIntelligenceServiceImpl.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private BusinessIntelligenceMapper businessIntelligenceMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private InvestService investService;

    private int lifeSecond = 2592000;

    @Override
    public List<String> getChannels() {
        List<String> userChannel = userService.findAllUserChannels();
        List<String> investChannel = investService.findAllInvestChannels();
        userChannel.removeAll(investChannel);
        userChannel.addAll(investChannel);
        return userChannel;
    }

    @Override
    public List<KeyValueModel> queryUserRegisterTrend(Granularity granularity, Date startTime, Date endTime, String province, UserStage userStage, RoleStage roleStage, String channel) {
        if (granularity == Granularity.Hourly) {
            endTime = startTime;
        }
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        List<KeyValueModel> keyValueModels = businessIntelligenceMapper.queryUserRegisterTrend(queryStartTime, queryEndTime, granularity, province, userStage, roleStage, channel);
        if (granularity == Granularity.Hourly) {
            return getHourKeyValueModels(keyValueModels);
        }
        return keyValueModels;
    }

    private List<KeyValueModel> getHourKeyValueModels(List<KeyValueModel> keyValueModelList) {
        Map<Integer, KeyValueModel> modelMap = new HashMap<>();
        for (int i = 0; i<24; i++) {
            KeyValueModel keyValueModel = new KeyValueModel();
            keyValueModel.setName(String.valueOf(i));
            keyValueModel.setGroup("");
            keyValueModel.setValue("");
            modelMap.put(i, keyValueModel);
        }
        for (KeyValueModel keyValueModel : keyValueModelList) {
            modelMap.put(Integer.parseInt(keyValueModel.getName()), keyValueModel);
        }
        List<KeyValueModel> keyValueModels = Lists.newArrayList(modelMap.values());
        return Lists.transform(keyValueModels, new Function<KeyValueModel, KeyValueModel>() {
            @Override
            public KeyValueModel apply(KeyValueModel input) {
                input.setName(String.format("%02d", Integer.parseInt(input.getName())));
                return input;
            }
        });
    }

    @Override
    public List<KeyValueModel> queryUserRechargeTrend(Granularity granularity, Date startTime, Date endTime, String province) {
        if (granularity == Granularity.Hourly) {
            endTime = startTime;
        }
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        List<KeyValueModel> keyValueModels = businessIntelligenceMapper.queryUserRechargeTrend(queryStartTime, queryEndTime, granularity, province);
        if (granularity == Granularity.Hourly) {
            return getHourKeyValueModels(keyValueModels);
        }
        return keyValueModels;
    }

    @Override
    public List<KeyValueModel> queryUserWithdrawTrend(Granularity granularity, Date startTime, Date endTime, String province) {
        if (granularity == Granularity.Hourly) {
            endTime = startTime;
        }
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        List<KeyValueModel> keyValueModels = businessIntelligenceMapper.queryUserWithdrawTrend(queryStartTime, queryEndTime, granularity, province);
        if (granularity == Granularity.Hourly) {
            return getHourKeyValueModels(keyValueModels);
        }
        return keyValueModels;
    }

    @Override
    public List<KeyValueModel> queryInvestViscosity(Date startTime, Date endTime, final String province) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        List<KeyValueModel> keyValueModelList = businessIntelligenceMapper.queryInvestViscosity(queryStartTime, queryEndTime, province);
        final KeyValueModel keyValueModel = new KeyValueModel();
        List<KeyValueModel> top4List = ListUtils.select(keyValueModelList, new Predicate<KeyValueModel>() {
            @Override
            public boolean evaluate(KeyValueModel object) {
                int loan_count = Integer.valueOf(object.getName());
                if (loan_count <= 4) {
                    return true;
                } else {
                    keyValueModel.setGroup(object.getGroup());
                    if (keyValueModel.getValue() == null) {
                        keyValueModel.setValue(object.getValue());
                    } else {
                        int otherCount = Integer.valueOf(keyValueModel.getValue()) + Integer.valueOf(object.getValue());
                        keyValueModel.setValue(String.valueOf(otherCount));
                    }
                    return false;
                }
            }
        });
        if (keyValueModel.getGroup() != null) {
            keyValueModel.setName("5+");
            top4List.add(keyValueModel);
        }
        return top4List;
    }

    @Override
    public InvestViscosityDetailTableView queryInvestViscosityDetail(Date startTime, Date endTime, final String province, int loanCount, int pageNo, int pageSize) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();

        long sumAmount = businessIntelligenceMapper.queryInvestViscositySumAmount(startTime, endTime, province, loanCount);
        List<InvestViscosityDetailView> items = businessIntelligenceMapper.queryInvestViscosityDetail(startTime, endTime, province, loanCount, (pageNo - 1) * pageSize, pageSize);
        int totalCount = businessIntelligenceMapper.queryInvestViscosityDetailCount(startTime, endTime, province, loanCount);

        InvestViscosityDetailTableView tableView = new InvestViscosityDetailTableView(sumAmount, totalCount, items);
        return tableView;
    }

    @Override
    public List<KeyValueModel> queryUserInvestCountTrend(Date startTime, Date endTime, String province) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserInvestCountTrend(queryStartTime, queryEndTime, province);
    }

    @Override
    public List<KeyValueModel> queryUserInvestAmountTrend(Granularity granularity, Date startTime, Date endTime, String province, RoleStage roleStage, String channel) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserInvestAmountTrend(queryStartTime, queryEndTime, granularity, province, roleStage, channel);
    }

    @Override
    public List<KeyValueModel> queryUserAgeTrend(Date startTime, Date endTime, String province, String isInvestor) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserAgeTrend(queryStartTime, queryEndTime, province, isInvestor);
    }

    @Override
    public List<KeyValueModel> queryLoanAmountDistribution(Date startTime, Date endTime) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryLoanAmountDistribution(queryStartTime, queryEndTime);
    }

    public List<KeyValueModel> queryLoanRaisingTimeCostingTrend(Date startTime, Date endTime) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        List<KeyValueModel> loanRaisingTimeCostList = businessIntelligenceMapper.queryLoanRaisingTimeCostingTrend(queryStartTime, queryEndTime);

        for (KeyValueModel kvm : loanRaisingTimeCostList) {
            String[] timeCostList = kvm.getValue().split(",");
            int median;
            int average = Integer.parseInt(kvm.getGroup());
            int len = timeCostList.length;
            if (len % 2 == 0) {
                median = Math.round((Float.parseFloat(timeCostList[len / 2]) + Float.parseFloat(timeCostList[len / 2 - 1])) / 2);
            } else {
                median = Integer.parseInt(timeCostList[len / 2]);
            }

            StringBuffer newValue = new StringBuffer();
            // low,median,average,high
            newValue.append(timeCostList[0]).append(",").append(median).append(",").append(average).append(",").append(timeCostList[len - 1]);
            kvm.setValue(newValue.toString());
            kvm.setGroup(null);
        }
        return loanRaisingTimeCostList;
    }

    @Override
    public List<KeyValueModel> queryWithdrawUserCountTrend(Date startTime, Date endTime, Granularity granularity) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryWithdrawUserCountTrend(queryStartTime, queryEndTime, granularity);
    }

    @Override
    public List<KeyValueModel> queryPlatformSumRepay(Date startTime, Date endTime, Granularity granularity) {
        Date queryStartTime = new DateTime(startTime).minusDays(1).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        List<KeyValueModel> keyValueModelLists = Lists.newArrayList();
        if (redisWrapperClient.hgetValuesSeri(PLATFORM_REPAY_KEY).size() == 0) {
            while (queryStartTime.before(queryEndTime)) {
                KeyValueModel keyValueModel = businessIntelligenceMapper.queryRepayByRecheckTimeAndActualRepayDate(DateUtils.addDays(queryStartTime, 1),queryStartTime);
                logger.info(MessageFormat.format("Platform Repay date:{0},value:{1}",keyValueModel.getName(),keyValueModel.getValue()));
                keyValueModelLists.add(keyValueModel);
                queryStartTime = DateUtils.addDays(queryStartTime, 1);
            }
            redisWrapperClient.hsetSeri(PLATFORM_REPAY_KEY,Granularity.Daily.name(), keyValueModelLists,lifeSecond);
        }else{
            List<byte[]> notifies = redisWrapperClient.hgetValuesSeri(PLATFORM_REPAY_KEY);
            for (byte[] bs : notifies) {
                keyValueModelLists = (List<KeyValueModel>) SerializeUtil.deserialize(bs);
            }

            KeyValueModel keyValueModel = keyValueModelLists.get(keyValueModelLists.size() - 1);
            logger.info(MessageFormat.format("Platform Repay date:{0},value:{1}",keyValueModel.getName(),keyValueModel.getValue()));
            Date redisLastDate = DateTime.parse(keyValueModel.getName()).toDate();
            Date nowDate = DateTime.now().withTimeAtStartOfDay().toDate();
            redisLastDate = DateUtils.addDays(redisLastDate, 1);
            while (redisLastDate.before(nowDate)) {
                keyValueModel = businessIntelligenceMapper.queryRepayByRecheckTimeAndActualRepayDate(DateUtils.addDays(redisLastDate, 1),redisLastDate);
                logger.info(MessageFormat.format("Platform Repay date:{0},value:{1}",keyValueModel.getName(),keyValueModel.getValue()));
                keyValueModelLists.add(keyValueModel);
                redisLastDate = DateUtils.addDays(redisLastDate, 1);
            }
            redisWrapperClient.hsetSeri(PLATFORM_REPAY_KEY,Granularity.Daily.name(), keyValueModelLists,lifeSecond);
        }
        return getMonthKeyValue(keyValueModelLists,granularity,startTime,endTime);
    }

    private List<KeyValueModel> getMonthKeyValue(List<KeyValueModel> keyValueModels, Granularity granularity,Date queryStartTime,Date queryEndTime){
        List<KeyValueModel> list = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        for(KeyValueModel keyValueModel : keyValueModels){
            Date redisDate = DateTime.parse(keyValueModel.getName()).toDate();
            if(redisDate.after(queryEndTime) || redisDate.before(queryStartTime)){
                continue;
            }
            calendar.setTime(redisDate);
            if(granularity.equals(Granularity.Monthly) && isLastDayOfMonth(calendar)){
                list.add(keyValueModel);
            }else if(granularity.equals(Granularity.Weekly) && isWeekend(calendar)){
                list.add(keyValueModel);
            }else if(granularity.equals(Granularity.Daily)){
                list.add(keyValueModel);
            }
        }
        return list;
    }

    private boolean isWeekend(Calendar cal){
        int week= cal.get(Calendar.DAY_OF_WEEK)-1;
        return week == 0;
    }

    private boolean isLastDayOfMonth(Calendar calendar) {
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }

}
