package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.Granularity;
import com.tuotiansudai.dto.RoleStage;
import com.tuotiansudai.dto.UserStage;
import com.tuotiansudai.repository.mapper.BusinessIntelligenceMapper;
import com.tuotiansudai.repository.model.InvestViscosityDetailTableView;
import com.tuotiansudai.repository.model.InvestViscosityDetailView;
import com.tuotiansudai.repository.model.KeyValueModel;
import com.tuotiansudai.service.BusinessIntelligenceService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.UserService;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Predicate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BusinessIntelligenceServiceImpl implements BusinessIntelligenceService {

    @Autowired
    private BusinessIntelligenceMapper businessIntelligenceMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private InvestService investService;

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
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserRegisterTrend(queryStartTime, queryEndTime, granularity, province, userStage, roleStage, channel);
    }

    @Override
    public List<KeyValueModel> queryUserRechargeTrend(Granularity granularity, Date startTime, Date endTime, String province) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserRechargeTrend(queryStartTime, queryEndTime, granularity, province);
    }

    @Override
    public List<KeyValueModel> queryUserWithdrawTrend(Granularity granularity, Date startTime, Date endTime, String province) {
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryUserWithdrawTrend(queryStartTime, queryEndTime, granularity, province);
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
    public List<KeyValueModel> queryWithdrawUserCountTrend(Date startTime, Date endTime,Granularity granularity){
        Date queryStartTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        Date queryEndTime = new DateTime(endTime).plusDays(1).withTimeAtStartOfDay().toDate();
        return businessIntelligenceMapper.queryWithdrawUserCountTrend(queryStartTime, queryEndTime,granularity);
    }
}
