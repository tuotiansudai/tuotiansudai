package com.esoft.jdp2p.invest.controller;

import com.esoft.archer.system.model.Dict;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.invest.model.InvestUserReferrer;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Scope(ScopeType.VIEW)
public class RefereeInvestList implements java.io.Serializable {

    @Resource
    HibernateTemplate ht;

    private Date investStartTime;
    private Date investEndTime;

    private Date rewardStartTime;
    private Date rewardEndTime;

    private static List<Dict> SUCCESS_INVEST_STATUS = Lists.newArrayList();

    private InvestItem condition = new InvestItem();

    private LazyDataModel<InvestItem> lazyModel;
    public static final String SQL_TEMPLATE = "{0} {1} {2} {3}";
    public static final String FROM_TEMPLATE = "from invest join loan on invest.loan_id=loan.id " +
            "join user investor on invest.user_id=investor.id " +
            "join referrer_relation rr on rr.user_id=investor.id " +
            "join user referrer on rr.referrer_id=referrer.id " +
            "join invest_userReferrer reward on reward.invest_id=invest.id and reward.referrer_id=referrer.id";
    public static final String ORDER_BY_TEMPLATE = "order by invest.time desc, invest.id, referrer.id";

    public LazyDataModel<InvestItem> getLazyModel() {
        if (this.lazyModel == null) {
            this.lazyModel = new LazyDataModel<InvestItem>() {
                @Override
                public List<InvestItem> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                    Session currentSession = ht.getSessionFactory().getCurrentSession();
                    String sql = generateQuery() + MessageFormat.format(" limit {0}, {1}", String.valueOf(first), String.valueOf(pageSize));
                    List<InvestItem> investItems = Lists.newArrayList();
                    List<Map<String, Object>> results = currentSession.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
                    for (Map<String, Object> result : results) {
                        investItems.add(createInvestItem(result));
                    }

                    String countQuery = generateCountQuery();
                    BigInteger count = (BigInteger) currentSession.createSQLQuery(countQuery).list().get(0);
                    this.setRowCount(count.intValue());

                    return investItems;
                }
            };
        }
        return lazyModel;
    }

    private String generateCountQuery() {
        String selectTemplate = "select count(1)";

        String whereTemplate = generateWhereTemplate();

        return MessageFormat.format(SQL_TEMPLATE, selectTemplate, FROM_TEMPLATE, whereTemplate, "");
    }

    private InvestItem createInvestItem(Map<String, Object> result) {
        InvestItem investItem = new InvestItem();
        investItem.setLoanId((String) result.get("loanId"));
        investItem.setInvestorId((String) result.get("userId"));
        investItem.setInvestorName((String) result.get("userName"));
        investItem.setSource((String) result.get("source"));
        investItem.setInvestTime((Date) result.get("investTime"));
        investItem.setMoney((Double) result.get("money"));
        investItem.setLoanName((String) result.get("loanName"));
        investItem.setLoanDeadline((Integer) result.get("deadline"));
        investItem.setReferrerId((String) result.get("referrerId"));
        investItem.setReferrerName((String) result.get("referrerName"));
        investItem.setRefereeLevel((Integer) result.get("level"));
        investItem.setReward((Double) result.get("reward"));
        investItem.setRewardTime((Date) result.get("rewardTime"));
        investItem.setIsMerchandiser("ROLE_MERCHANDISER".equalsIgnoreCase((String)result.get("referrerRole")) ? true : false);

        if (InvestUserReferrer.SUCCESS.equals(result.get("rewardStatus"))) {
            investItem.setRewardStatus("已入账");
        }

        String referrerRole = (String) result.get("referrerRole");
        if ("INVESTOR".equalsIgnoreCase(referrerRole) && InvestUserReferrer.FAIL.equalsIgnoreCase((String) result.get("rewardStatus"))) {
            investItem.setRewardStatus("入账失败");
        }
        if ("ROLE_MERCHANDISER".equalsIgnoreCase(referrerRole) && InvestUserReferrer.FAIL.equalsIgnoreCase((String) result.get("rewardStatus"))) {
            investItem.setRewardStatus("已记录");
        }

        return investItem;
    }

    private String generateQuery() {
        String selectTemplate = "select " +
                "loan.id as loanId, " +
                "loan.name as loanName, " +
                "loan.deadline as deadline, " +
                "invest.id as investId, " +
                "invest.status as investStatus, " +
                "invest.time as investTime, " +
                "invest.money as money, " +
                "invest.source as source, " +
                "investor.id as userId, " +
                "investor.realname as userName, " +
                "referrer.id as referrerId, " +
                "referrer.realname as referrerName, " +
                "rr.level as level, " +
                "reward.role_name as referrerRole, " +
                "reward.bonus as reward, " +
                "reward.status as rewardStatus, " +
                "reward.time as rewardTime";

        String whereTemplate = generateWhereTemplate();

        return MessageFormat.format(SQL_TEMPLATE,
                selectTemplate,
                FROM_TEMPLATE,
                whereTemplate,
                ORDER_BY_TEMPLATE);
    }

    private String generateWhereTemplate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String whereTemplate = "where invest.status in ('complete', 'bid_success', 'repaying')";

        if (!Strings.isNullOrEmpty(condition.getReferrerId())) {
            whereTemplate += " and referrer.id='" + condition.getReferrerId() + "'";
        }
        if (!Strings.isNullOrEmpty(condition.getInvestorId())) {
            whereTemplate += " and investor.id='" + condition.getInvestorId() + "'";
        }
        if (condition.getRefereeLevel() != null) {
            whereTemplate += " and rr.level=" + condition.getRefereeLevel();
        }
        if (condition.getIsMerchandiser() != null && condition.getIsMerchandiser()) {
            whereTemplate += "and exists (select 1 from user_role where user_role.user_id = referrer.id and user_role.role_id='ROLE_MERCHANDISER')";
        }

        if (condition.getIsMerchandiser() != null && !condition.getIsMerchandiser()) {
            whereTemplate += "and exists (select 1 from user_role where user_role.user_id = referrer.id and user_role.role_id='INVESTOR')";
        }
        if(StringUtils.isNotEmpty(condition.getSource())){
            whereTemplate += " and invest.source='" + condition.getSource() + "'";
        }
        if (investStartTime != null) {
            whereTemplate += " and invest.time >='" + dateFormat.format(investStartTime) + "'";
        }
        if (investEndTime != null) {
            whereTemplate += " and invest.time <='" + dateFormat.format(investEndTime) + "'";
        }

        if (rewardStartTime != null) {
            whereTemplate += " and reward.time >='" + dateFormat.format(rewardStartTime) + "'";
        }
        if (rewardEndTime != null) {
            whereTemplate += " and reward.time <='" + dateFormat.format(rewardEndTime) + "'";
        }

        return whereTemplate;
    }

    public double getSumMoney() {
        String queryTemplate = "select sum(temp.money) from (select distinct invest.id, invest.money {0} {1}) temp";

        String whereTemplate = generateWhereTemplate();

        String sql = MessageFormat.format(queryTemplate, FROM_TEMPLATE, whereTemplate);
        Session currentSession = ht.getSessionFactory().getCurrentSession();
        Double sumInvest = (Double) currentSession.createSQLQuery(sql).list().get(0);

        return sumInvest != null ? sumInvest : 0;
    }

    public double getSumReward() {
        String selectTemplate = "select sum(reward.bonus)";

        String whereTemplate = generateWhereTemplate();

        String sql = MessageFormat.format(SQL_TEMPLATE, selectTemplate, FROM_TEMPLATE, whereTemplate, "");
        Session currentSession = ht.getSessionFactory().getCurrentSession();
        Double sumReward = (Double) currentSession.createSQLQuery(sql).list().get(0);

        return sumReward != null ? sumReward : 0;
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

    public Date getInvestStartTime() {
        return investStartTime;
    }

    public void setInvestStartTime(Date investStartTime) {
        this.investStartTime = investStartTime;
    }

    public Date getInvestEndTime() {
        return investEndTime;
    }

    public void setInvestEndTime(Date investEndTime) {
        this.investEndTime = investEndTime;
    }

    public Date getRewardStartTime() {
        return rewardStartTime;
    }

    public void setRewardStartTime(Date rewardStartTime) {
        this.rewardStartTime = rewardStartTime;
    }

    public Date getRewardEndTime() {
        return rewardEndTime;
    }

    public void setRewardEndTime(Date rewardEndTime) {
        this.rewardEndTime = rewardEndTime;
    }
}

