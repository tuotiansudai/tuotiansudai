package com.esoft.jdp2p.invest.controller;

import com.esoft.archer.user.model.ReferrerRelation;
import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.model.InvestUserReferrer;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class RefereeInvestList implements java.io.Serializable {

    public static final String QUERY_LEVEL = "select relation from ReferrerRelation relation where relation.referrerId=''{0}'' and relation.userId=''{1}''";
    public static final String QUERY_REWARD = "select investUserReferrer from InvestUserReferrer investUserReferrer where investUserReferrer.invest=''{0}'' and investUserReferrer.referrer=''{1}''";
    public static final String QUERY_ROLE = "select role from Role role where role.id=''{0}''";

    @Resource
    HibernateTemplate ht;

    private List<InvestItem> lazyModel = Lists.newArrayList();

    private Date startTime;
    private Date endTime;

    private InvestItem condition = new InvestItem();


    public List<InvestItem> getLazyModel() {
        List<Invest> invests = ht.find(generateQueryHql());
        List<InvestItem> investItems = Lists.newArrayList();
        for (Invest invest : invests) {
            List<User> referrers = invest.getUser().getReferrers();
            if (CollectionUtils.isEmpty(referrers) && Strings.isNullOrEmpty(condition.getReferrerId())) {
                List<InvestItem> newInvestItems = createInvestItems(invest, null);
                investItems.addAll(newInvestItems);
            }
            for (User referrer : referrers) {
                if (referrer.getId().equals(condition.getReferrerId())
                        || Strings.isNullOrEmpty(condition.getReferrerId())) {
                    List<InvestItem> newInvestItems = createInvestItems(invest, referrer);
                    investItems.addAll(newInvestItems);
                }
            }
        }

        Ordering<InvestItem> ordering = new Ordering<InvestItem>() {
            @Override
            public int compare(InvestItem left, InvestItem right) {
                return left.getInvestTime().compareTo(right.getInvestTime());
            }
        };

        return ordering.reverse().sortedCopy(investItems);
    }

    private List<InvestItem> createInvestItems(Invest invest, User referrer) {
        List<InvestItem> items = Lists.newArrayList();
        String referrerId = referrer == null ? null : referrer.getId();
        String referrerName = referrer == null ? null : referrer.getRealname();
        List<InvestUserReferrer> rewards = ht.find(MessageFormat.format(QUERY_REWARD, invest.getId(), referrerId));
        if (referrerId == null || rewards.isEmpty()) {
            InvestItem investItem = createInvestItem(invest, referrerId, referrerName, null);
            items.add(investItem);
        } else
            for (InvestUserReferrer reward : rewards) {
                InvestItem investItem = createInvestItem(invest, referrerId, referrerName, reward);
                items.add(investItem);
            }

        return items;
    }

    private InvestItem createInvestItem(Invest invest, String referrerId, String referrerName, InvestUserReferrer reward) {
        InvestItem investItem = new InvestItem();
        investItem.setInvestorId(invest.getUser().getId());
        investItem.setInvestorName(invest.getUser().getRealname());
        investItem.setInvestTime(invest.getTime());
        investItem.setIsAutoInvest(invest.getIsAutoInvest());
        investItem.setLoanId(invest.getLoan().getId());
        investItem.setInvestStatus(invest.getStatus());
        investItem.setLoadType(invest.getLoan().getType().getName());
        investItem.setMoney(invest.getMoney());
        investItem.setLoanName(invest.getLoan().getName());
        investItem.setReferrerId(referrerId);
        investItem.setReferrerName(referrerName);
        investItem.setRefereeLevel(queryReferrerLevel(referrerId, invest.getUser().getId()));
        if (reward != null) {
            investItem.setReward(reward.getBonus());
            investItem.setRewardTime(reward.getTime());
            if (reward.getStatus().equals(InvestUserReferrer.SUCCESS)) {
                investItem.setRewardStatus("奖励已入账");
            }
            if (reward.getRoleName().equals("INVESTOR") && reward.getStatus().equals(InvestUserReferrer.FAIL)) {
                investItem.setRewardStatus("奖励入账失败");
            }
            if (reward.getRoleName().equals("ROLE_MERCHANDISER") && reward.getStatus().equals(InvestUserReferrer.FAIL)) {
                investItem.setRewardStatus("奖励已记录");
            }

            List<Role> roleList = ht.find(MessageFormat.format(QUERY_ROLE, reward.getRoleName()));
            if (CollectionUtils.isNotEmpty(roleList)) {
                investItem.setReferrerRole(roleList.get(0).getDescription());
            }
        }
        return investItem;
    }

    private Integer queryReferrerLevel(String referrerId, String investorId) {
        if (referrerId == null || investorId == null) {
            return null;
        }
        List<ReferrerRelation> relations = ht.find(MessageFormat.format(QUERY_LEVEL, referrerId, investorId));
        if (relations.isEmpty()) {
            return null;
        }
        return relations.get(0).getLevel();
    }

    private String generateQueryHql() {
        String hql = "select invest from Invest invest ";

        if (!Strings.isNullOrEmpty(condition.getReferrerId())) {
            String referrerConditionTemplate = "inner join invest.user investor inner join investor.referrers referrer where referrer.id=''{0}'' ";
            hql += MessageFormat.format(referrerConditionTemplate, condition.getReferrerId());
        } else {
            hql += "where 1=1 ";
        }

        if (!Strings.isNullOrEmpty(condition.getInvestStatus())) {
            String loanStatusConditionTemplate = "and invest.loan.status=''{0}'' ";
            hql += MessageFormat.format(loanStatusConditionTemplate, condition.getInvestStatus());
        } else {
            hql += "and invest.status in ('complete', 'bad_debt', 'overdue', 'bid_success', 'repaying') ";
        }

        if (!Strings.isNullOrEmpty(condition.getLoanId())) {
            String loanConditionTemplate = "and invest.loan=''{0}'' ";
            hql += MessageFormat.format(loanConditionTemplate, condition.getLoanId());
        }

        if (!Strings.isNullOrEmpty(condition.getInvestorId())) {
            String investorConditionTemplate = "and invest.user=''{0}'' ";
            hql += MessageFormat.format(investorConditionTemplate, condition.getInvestorId());
        }

        if (startTime != null) {
            String startTimeConditionTemplate = "and invest.time>=''{0}'' ";
            hql += MessageFormat.format(startTimeConditionTemplate, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime));
        }

        if (endTime != null) {
            String endTimeConditionTemplate = "and invest.time<=''{0}'' ";
            hql += MessageFormat.format(endTimeConditionTemplate, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime));
        }

        return hql;
    }

    public Double getSumMoney() {
        List<Invest> invests = ht.find(generateQueryHql());
        double sumMoney = 0;
        for (Invest invest : invests) {
            sumMoney += invest.getMoney();
        }
        return sumMoney;
    }

    public void setLazyModel(List<InvestItem> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public InvestItem getCondition() {
        if (condition == null) {
            condition = new InvestItem();
        }
        return condition;
    }

    public void setCondition(InvestItem condition) {
        this.condition = condition;
    }
}

