package com.ttsd.api.dao.impl;

import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.loan.LoanConstants;
import com.ttsd.api.dao.MobileAppReferrerInvestListDao;
import com.ttsd.api.dto.ReferrerInvestResponseDataDto;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppReferrerInvestListDaoImpl implements MobileAppReferrerInvestListDao {
    @Resource
    private HibernateTemplate ht;

    @Override
    public Integer getTotalCount(String referrerId) {
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(getQueryCondition("totalCount"));
        sqlQuery.setParameter(0, referrerId);
        return ((Number) sqlQuery.uniqueResult()).intValue();
    }

    @Override
    public List<ReferrerInvestResponseDataDto> getReferrerInvestList(Integer index, Integer pageSize, String referrerId) {
        List<ReferrerInvestResponseDataDto> referrerInvestResponseDataDtos = new ArrayList<>();

        Query sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(getQueryCondition("referrerInvest")).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        sqlQuery.setParameter(0, referrerId);
        sqlQuery.setParameter(1, (index - 1) * pageSize);
        sqlQuery.setParameter(2, pageSize);
        List<Map<String, Object>> result = sqlQuery.list();
        for (int i = 0; i < result.size(); i++) {
            ReferrerInvestResponseDataDto referrerInvestResponseDataDto = new ReferrerInvestResponseDataDto();
            referrerInvestResponseDataDto.setUserId(result.get(i).get("investUserId").toString());
            referrerInvestResponseDataDto.setLevel(result.get(i).get("level").toString());
            referrerInvestResponseDataDto.setInvestMoney(result.get(i).get("investMoney").toString());
            referrerInvestResponseDataDto.setInvestTime(new SimpleDateFormat("yyyy-MM-dd").format(result.get(i).get("investTime")));
            referrerInvestResponseDataDto.setRewardMoney(result.get(i).get("rewardMoney").toString());
            referrerInvestResponseDataDto.setLoanId(result.get(i).get("loanId").toString());
            referrerInvestResponseDataDto.setLoanName(result.get(i).get("loanName").toString());
            referrerInvestResponseDataDto.setRewardTime(new SimpleDateFormat("yyyy-MM-dd").format(result.get(i).get("rewardTime")));
            referrerInvestResponseDataDto.setDeadline(result.get(i).get("deadLine").toString());
            referrerInvestResponseDataDtos.add(referrerInvestResponseDataDto);
        }

        return referrerInvestResponseDataDtos;
    }

    @Override
    public double getRewardTotalMoney(String referrerId) {
        double resultMoney = 0.0d;
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(getQueryCondition("rewardTotalMoney"));
        sqlQuery.setParameter(0, referrerId);
        Object obj = sqlQuery.uniqueResult();
        if (obj != null) {
            resultMoney = ((Number) sqlQuery.uniqueResult()).doubleValue();
        }
        return resultMoney;
    }

    @Override
    public String getQueryCondition(String queryType) {
        String queryResult = "";
        if ("rewardTotalMoney".equals(queryType)) {
            queryResult = " sum(n.`rewardMoney`) ";
        } else if ("totalCount".equals(queryType)) {
            queryResult = " count(1) ";
        } else {
            queryResult = "  n.`investUserId`, " +
                    "  n.`level`, " +
                    "  n.`investMoney`, " +
                    "  n.`investTime`, " +
                    "  n.`rewardMoney`, " +
                    "  n.`rewardTime`, " +
                    "  n.`loanId`, " +
                    "  l.`name` AS `loanName`, " +
                    "  l.`loan_activity_type` AS `loanActivityType`," +
                    "  l.`deadline` AS `deadLine` ";
        }
        String sql = "SELECT " + queryResult +
                " FROM " +
                "  (SELECT " +
                "    temp.`investUserId`, " +
                "    r.`level`, " +
                "    temp.`investMoney`, " +
                "    temp.`investTime`, " +
                "    temp.`rewardMoney`, " +
                "    temp.`rewardTime`, " +
                "    temp.`loanId` " +
                "  FROM " +
                "    referrer_relation r " +
                "    JOIN " +
                "      (SELECT " +
                "        i.`user_id` AS investUserId, " +
                "        i.`money` AS investMoney, " +
                "        i.`time` AS investTime, " +
                "        i.`loan_id` AS loanId, " +
                "        t.`bonus` AS rewardMoney, " +
                "        t.`time` AS rewardTime, " +
                "        t.`referrer_id` " +
                "      FROM " +
                "        invest_userReferrer t " +
                "        JOIN invest i " +
                "          ON t.`invest_id` = i.`id` " +
                " AND i.`status` NOT IN ('" + InvestConstants.InvestStatus.CANCEL + "', '" + InvestConstants.InvestStatus.UNFINISHED + "') " +
                "      WHERE t.`referrer_id` = ? " +
                ") temp " +
                "      ON r.`user_id` = temp.`investUserId` " +
                "      AND r.`referrer_id` = temp.`referrer_id`) n " +
                "  JOIN loan l " +
                "    ON n.`loanId` = l.`id` " +
                "    AND l.`status` IN ('" + LoanConstants.LoanStatus.COMPLETE + "', '" + LoanConstants.LoanStatus.REPAYING + "') " ;
                if("referrerInvest".equals(queryType)){
                    sql+=" ORDER BY n.`rewardTime` DESC limit ?,?";
                }


        return sql;
    }


}
