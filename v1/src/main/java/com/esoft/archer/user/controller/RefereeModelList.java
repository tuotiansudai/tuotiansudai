package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.RefereeModel;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.invest.InvestConstants.InvestStatus;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

/**
 * @author hch 推荐人模型控制器
 */
@Component
@Scope(ScopeType.REQUEST)
public class RefereeModelList extends EntityQuery<RefereeModel> {
    private static final String lazyModelCount = "select "
            + "count(distinct referrers.id) "
            + "from Invest invest right outer join invest.user user "
            + "inner join user.referrers referrers "
            + "where invest.status is null or "
            + "invest.status in ('" + InvestStatus.BID_SUCCESS + "','"
            + InvestStatus.REPAYING + "','" + InvestStatus.OVERDUE + "','"
            + InvestStatus.COMPLETE + "','" + InvestStatus.BAD_DEBT + "') ";

    private static final String lazyModel = "select "
            + "new com.esoft.archer.user.model.RefereeModel(referrers.id,sum(invest.money),min(invest.time),max(invest.time)) "
            + "from Invest invest right outer join invest.user user "
            + "inner join user.referrers referrers "
            + "where invest.status is null or "
            + "invest.status in ('" + InvestStatus.BID_SUCCESS + "','"
            + InvestStatus.REPAYING + "','" + InvestStatus.OVERDUE + "','"
            + InvestStatus.COMPLETE + "','" + InvestStatus.BAD_DEBT + "') ";

    // 查询条件 start
    private String referee;// 推荐人
    private Date searchCommitMinTime, searchCommitMaxTime;

    // 查询条件 end

    public RefereeModelList() {
        setCountHql(lazyModelCount);
        setHql(lazyModel);
        setOrderColumn(Lists.newArrayList("referrers.id"));
        setOrderDirection(Lists.newArrayList("asc"));

        final String[] RESTRICTIONS = {
                "invest.time >= #{refereeModelList.searchCommitMinTime}",
                "invest.time <= #{refereeModelList.searchCommitMaxTime}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    @Override
    protected String getRenderedCountHql() {
        String countHql = getCountHql();
        Object expressionValue = FacesUtil.getExpressionValue("#{refereeModelList.referee}");
        if (expressionValue != null && !expressionValue.toString().equals("")) {
            countHql += (" and referrers.id='" + expressionValue.toString()) + "'";
        }

        return countHql;
    }

    @Override
    protected String parseHql(final String hql) {
        String sql = super.parseHql(hql);
        Object expressionValue = FacesUtil.getExpressionValue("#{refereeModelList.referee}");
        if (expressionValue != null && !expressionValue.toString().equals("")) {
            sql += (" group by referrers.id having referrers.id='" + expressionValue.toString()) + "'";
        } else {
            sql += " group by referrers.id";
        }

        return sql;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public Date getSearchCommitMinTime() {
        return searchCommitMinTime;
    }

    public void setSearchCommitMinTime(Date searchCommitMinTime) {
        this.searchCommitMinTime = searchCommitMinTime;
    }

    public Date getSearchCommitMaxTime() {
        return searchCommitMaxTime;
    }

    public void setSearchCommitMaxTime(Date searchCommitMaxTime) {
        this.searchCommitMaxTime = searchCommitMaxTime;
    }
}
