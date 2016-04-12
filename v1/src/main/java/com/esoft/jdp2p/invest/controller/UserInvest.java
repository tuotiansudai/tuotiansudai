package com.esoft.jdp2p.invest.controller;

import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.invest.service.InvestService;
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
public class UserInvest implements java.io.Serializable {

    @Resource
    HibernateTemplate ht;

    @Resource
    InvestService investService;

    private Date investStartTime;
    private Date investEndTime;
    private List<String> allChannelList;

    private UserInvestItem condition = new UserInvestItem();

    private LazyDataModel<UserInvestItem> lazyModel;

    public static final String SQL_TEMPLATE = "{0} {1} {2} {3}";
    public static final String FROM_TEMPLATE = "from invest join loan on invest.loan_id=loan.id " +
            "join user on invest.user_id=user.id ";
    public static final String ORDER_BY_TEMPLATE = "order by invest.time desc";

    public LazyDataModel<UserInvestItem> getLazyModel() {
        if (this.lazyModel == null) {
            this.lazyModel = new LazyDataModel<UserInvestItem>() {
                @Override
                public List<UserInvestItem> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                    Session currentSession = ht.getSessionFactory().getCurrentSession();
                    String sql = generateQuery() + MessageFormat.format(" limit {0}, {1}", String.valueOf(first), String.valueOf(pageSize));
                    List<UserInvestItem> investItems = Lists.newArrayList();
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

    private UserInvestItem createInvestItem(Map<String, Object> result) {
        UserInvestItem investItem = new UserInvestItem();
        investItem.setLoanId((String) result.get("loanId"));
        investItem.setLoanName((String) result.get("loanName"));
        investItem.setLoanDeadline((Integer) result.get("deadline"));
        investItem.setInvestorId((String) result.get("userId"));
        investItem.setInvestorName((String) result.get("userName"));
        investItem.setMoney((Double) result.get("money"));
        investItem.setInvestTime((Date) result.get("investTime"));
        investItem.setIsMerchandiser(((Number) result.get("isMerchandiser")).intValue() == 1);
        investItem.setSource((String) result.get("source"));
        investItem.setChannel((String) result.get("channel"));
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
                "invest.channel as channel, " +
                "user.id as userId, " +
                "user.realname as userName, " +
                "exists (select 1 from user_role where user.id=user_role.user_id and user_role.role_id='ROLE_MERCHANDISER') isMerchandiser ";

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

        if (!Strings.isNullOrEmpty(condition.getInvestorId())) {
            whereTemplate += " and user.id='" + condition.getInvestorId() + "'";
        }
        if (condition.getIsMerchandiser() != null && condition.getIsMerchandiser()) {
            whereTemplate += " and exists (select 1 from user_role where user_role.user_id = user.id and user_role.role_id='ROLE_MERCHANDISER')";
        }
        if (condition.getIsMerchandiser() != null && !condition.getIsMerchandiser()) {
            whereTemplate += " and exists (select 1 from user_role where user_role.user_id = user.id and user_role.role_id='INVESTOR')";
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
        if (!Strings.isNullOrEmpty(condition.getChannel())) {
            whereTemplate += " and invest.channel ='" + condition.getChannel() + "'";
        }

        return whereTemplate;
    }

    public double getSumMoney() {
        String queryTemplate = "select sum(invest.money) {0} {1}";

        String whereTemplate = generateWhereTemplate();

        String sql = MessageFormat.format(queryTemplate, FROM_TEMPLATE, whereTemplate);
        Session currentSession = ht.getSessionFactory().getCurrentSession();
        Double sumInvest = (Double) currentSession.createSQLQuery(sql).list().get(0);

        return sumInvest != null ? sumInvest : 0;
    }

    public UserInvestItem getCondition() {
        if (condition == null) {
            condition = new UserInvestItem();
        }
        return condition;
    }

    public void setCondition(UserInvestItem condition) {
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

    public List<String> getAllChannelList() {
        if(allChannelList == null){
            allChannelList = investService.getAllChannelName();
        }
        return allChannelList;
    }

    public void setAllChannelList(List<String> allChannelList) {
        this.allChannelList = allChannelList;
    }
}

