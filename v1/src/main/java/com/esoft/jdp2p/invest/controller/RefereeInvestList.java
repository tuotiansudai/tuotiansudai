package com.esoft.jdp2p.invest.controller;

import com.esoft.archer.user.model.ReferrerRelation;
import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.model.InvestUserReferrer;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Scope(ScopeType.VIEW)
public class RefereeInvestList implements java.io.Serializable {

    public static final String QUERY_LEVEL = "select relation from ReferrerRelation relation where relation.referrerId=''{0}'' and relation.userId=''{1}''";
    public static final String QUERY_REWARD = "select investUserReferrer from InvestUserReferrer investUserReferrer where investUserReferrer.invest=''{0}'' and investUserReferrer.referrer=''{1}''";
    public static final String QUERY_ROLE = "select role from Role role where role.id=''{0}''";

    @Resource
    HibernateTemplate ht;

    private Date startTime;
    private Date endTime;

    private InvestItem condition = new InvestItem();

    private LazyDataModel<InvestItem> lazyModel;

    public LazyDataModel<InvestItem> getLazyModel() {
        if (this.lazyModel == null) {
            this.lazyModel = new LazyDataModel<InvestItem>() {
                @Override
                public List<InvestItem> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                    DetachedCriteria criteria = generateQueryCriteria();
                    List<Invest> invests = ht.findByCriteria(criteria, first, pageSize);
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

                    Long count = (Long) DataAccessUtils.uniqueResult(ht.findByCriteria(generateQueryCriteria().setProjection(Projections.rowCount())));
                    this.setRowCount(count.intValue());

                    return investItems;
                }
            };
        }
        return lazyModel;
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
                investItem.setReferrerRole(roleList.get(0).getName());
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

    private DetachedCriteria generateQueryCriteria() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Invest.class);
        criteria.createAlias("user", "user")
                .createAlias("loan", "loan")
                .createAlias("user.referrers", "referrer", Criteria.LEFT_JOIN);

        if (!Strings.isNullOrEmpty(condition.getReferrerId())) {
            criteria.add(Restrictions.eq("referrer.id", condition.getReferrerId()));
        }

        if (!Strings.isNullOrEmpty(condition.getInvestStatus())) {
            criteria.add(Restrictions.eq("loan.status", condition.getInvestStatus()));
        }

        if (!Strings.isNullOrEmpty(condition.getLoanId())) {
            criteria.add(Restrictions.eq("loan.id", condition.getLoanId()));
        }

        if (!Strings.isNullOrEmpty(condition.getInvestorId())) {
            criteria.add(Restrictions.eq("user.id", condition.getInvestorId()));
        }

        if (startTime != null) {
            criteria.add(Restrictions.ge("time", startTime));
        }

        if (endTime != null) {
            criteria.add(Restrictions.le("time", endTime));
        }

        criteria.addOrder(Order.desc("time"))
                .addOrder(Order.asc("id"));
        return criteria;
    }

    public Double getSumMoney() {
        DetachedCriteria criteria = generateQueryCriteria();
        criteria.setProjection(Projections.sum("money"));

        Double count = (Double) DataAccessUtils.uniqueResult(ht.findByCriteria(criteria));
        return count == null ? 0D : count;
    }

    public Double getSumReward() {
        DetachedCriteria criteria = DetachedCriteria.forClass(InvestUserReferrer.class);
        criteria.createAlias("invest", "invest")
                .createAlias("invest.user", "user")
                .createAlias("invest.loan", "loan")
                .createAlias("referrer", "referrer");

        if (!Strings.isNullOrEmpty(condition.getReferrerId())) {
            criteria.add(Restrictions.eq("referrer.id", condition.getReferrerId()));
        }

        if (!Strings.isNullOrEmpty(condition.getInvestStatus())) {
            criteria.add(Restrictions.eq("loan.status", condition.getInvestStatus()));
        }

        if (!Strings.isNullOrEmpty(condition.getLoanId())) {
            criteria.add(Restrictions.eq("loan.id", condition.getLoanId()));
        }

        if (!Strings.isNullOrEmpty(condition.getInvestorId())) {
            criteria.add(Restrictions.eq("user.id", condition.getInvestorId()));
        }

        if (startTime != null) {
            criteria.add(Restrictions.ge("invest.time", startTime));
        }

        if (endTime != null) {
            criteria.add(Restrictions.le("invest.time", endTime));
        }


        criteria.setProjection(Projections.sum("bonus"));

        Double count = (Double) DataAccessUtils.uniqueResult(ht.findByCriteria(criteria));
        return count == null ? 0D : count;
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

