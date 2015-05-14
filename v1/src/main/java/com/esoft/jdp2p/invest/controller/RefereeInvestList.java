package com.esoft.jdp2p.invest.controller;

import com.esoft.archer.user.model.ReferrerRelation;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.invest.model.Invest;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
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
    public static final String QUERY_REWARD = "select investUserReferrer from InvestUserReferrer investUserReferrer where investUserReferrer.invest=''{0}'' and investUserReferrer.userId=''{1}''";

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
                InvestItem investItem = createInvestItem(invest, null);
                investItems.add(investItem);
            }
            for (User referrer : referrers) {
                if (referrer.getId().equals(condition.getReferrerId())
                        || Strings.isNullOrEmpty(condition.getReferrerId())) {
                    InvestItem investItem = createInvestItem(invest, referrer);
                    investItems.add(investItem);
                }
            }
        }

        return investItems;
    }

    private InvestItem createInvestItem(Invest invest, User referrer) {
        InvestItem investItem = new InvestItem();
        investItem.setInvestorId(invest.getUser().getId());
        investItem.setInvestTime(invest.getTime());
        investItem.setIsAutoInvest(invest.getIsAutoInvest());
        investItem.setLoanId(invest.getLoan().getId());
        investItem.setLoanStatus(invest.getLoan().getStatus());
        investItem.setLoadType(invest.getLoan().getType().getName());
        investItem.setMoney(invest.getMoney());
        investItem.setLoanName(invest.getLoan().getName());
        if (referrer != null) {
            investItem.setReferrerId(referrer.getId());
            List<ReferrerRelation> relations = ht.find(MessageFormat.format(QUERY_LEVEL, investItem.getReferrerId(), investItem.getInvestorId()));
            investItem.setRefereeLevel(relations.get(0).getLevel());
        }
        return investItem;
    }

    private String generateQueryHql() {
        String hql = "select invest from Invest invest ";

        if (!Strings.isNullOrEmpty(condition.getReferrerId())) {
            String referrerConditionTemplate = "inner join invest.user investor inner join investor.referrers referrer where referrer.id=''{0}'' ";
            hql += MessageFormat.format(referrerConditionTemplate, condition.getReferrerId());
        } else {
            hql += "where 1=1 ";
        }

        if (!Strings.isNullOrEmpty(condition.getLoanStatus())) {
            String loanStatusConditionTemplate = "and invest.loan.status=''{0}'' ";
            hql += MessageFormat.format(loanStatusConditionTemplate, condition.getLoanStatus());
        } else {
            hql += "and invest.loan.status in ('complete', 'wait_loaning_verify', 'cancel', 'bad_debt', 'wait_affirm', 'overdue', 'withdrawal', 'bid_success', 'repaying') ";
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
        return 0d;
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

