package com.esoft.jdp2p.creditorRepayPlan.service.impl;

import com.esoft.jdp2p.creditorRepayPlan.model.CreditorRepayPlan;
import com.esoft.jdp2p.creditorRepayPlan.service.CreditorRepayPlanService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/16.
 */
@Service("creditorRepayPlanService")
public class CreditorRepayPlanServiceImpl implements CreditorRepayPlanService{

    @Resource
    private HibernateTemplate ht;

    private String completeSql = " SELECT " +
            "    DATE_FORMAT(t.`time`, '%Y-%m') AS repayTime," +
            "    ROUND(SUM(t.`corpus`), 2) AS corpus," +
            "    ROUND(SUM(t.`interest`), 2) AS interest," +
            "    ROUND(SUM(t.`default_interest`), 2) AS defaultInterest," +
            "    ROUND(SUM(t.`fee`), 2) AS fee " +
            "  FROM" +
            "    `loan_repay` t " +
            "  WHERE t.`status` = 'complete' ";

    private String unCompleteSql = " SELECT " +
            "    DATE_FORMAT(t.`repay_day`, '%Y-%m') AS repayTime," +
            "    ROUND(SUM(t.`corpus`), 2) AS corpus," +
            "    ROUND(SUM(t.`interest`), 2) AS interest," +
            "    ROUND(SUM(t.`default_interest`), 2) AS defaultInterest," +
            "    ROUND(SUM(t.`fee`), 2) AS fee " +
            "  FROM" +
            "    `loan_repay` t " +
            "  WHERE t.`status` NOT IN ('complete','test') ";

    private String detailSql = "SELECT " +
            "  DATE_FORMAT(IF(" +
            "    t.`status` = 'complete'," +
            "    t.`time`," +
            "    t.`repay_day`" +
            "  ),'%Y-%m-%d') AS actualDay," +
            "  DATE_FORMAT(t.`repay_day`,'%Y-%m-%d') AS repayDay," +
            "  ROUND(" +
            "    (" +
            "      t.`corpus` + t.`default_interest` + t.`interest` + t.`fee`" +
            "    )," +
            "    2" +
            "  ) AS repayMoney," +
            "  m.`name` AS loanName," +
            "  m.`id` AS loanId,"+
            "  IFNULL(m.`agent`, m.`user_id`) AS userId," +
            "  CASE" +
            "    t.`status` " +
            "    WHEN 'complete' " +
            "    THEN '还款完成' " +
            "    WHEN 'repaying' " +
            "    THEN '尚未还款' " +
            "    ELSE '逾期还款' " +
            "  END AS repayStatus " +
            " FROM" +
            "  loan_repay t " +
            "  JOIN loan m " +
            "    ON t.loan_id = m.id " +
            "    AND m.status IN ('overdue', 'complete', 'repaying') ";

    public List<CreditorRepayPlan> searchList(String status,Date startTime,Date endTime){
        StringBuffer sbListSql = getListStringBuffer(status, startTime, endTime);
        Query query = ht.getSessionFactory().getCurrentSession().createSQLQuery(sbListSql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> list = query.list();
        List<CreditorRepayPlan> returnList = new ArrayList<CreditorRepayPlan>();
        for (int i = 0;i < list.size();i++){
            CreditorRepayPlan creditorRepayPlan = new CreditorRepayPlan();
            creditorRepayPlan.setRepayTime(list.get(i).get("repayTime").toString());
            creditorRepayPlan.setTotalMoney(new BigDecimal(list.get(i).get("totalMoney").toString()).toString());
            returnList.add(creditorRepayPlan);
        }
        return returnList;
    }

    private StringBuffer getDetailStringBuffer(String repayTime) {
        StringBuffer sbSql = new StringBuffer();
        sbSql.append(detailSql);
        if (repayTime != null) {
            sbSql.append("WHERE DATE_FORMAT( " +
                    "   t.`repay_day`,"+
                    "    '%Y-%m'" +
                    "  ) = '"+repayTime+"'");
        }
        sbSql.append(" ORDER BY repayDay ");
        return sbSql;
    }

    private StringBuffer getListStringBuffer(String status, Date startTime, Date endTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT temp.repayTime, (SUM(temp.corpus) + SUM(temp.interest) + SUM(temp.defaultInterest) + SUM(temp.fee)) AS totalMoney FROM (");
        if (status.equals("complete")) {
            sbSql.append(completeSql);
            if (startTime != null) {
                sbSql.append(" and t.`time` >= '"+simpleDateFormat.format(startTime)+"'");
            }
            if (endTime != null) {
                sbSql.append(" and t.`time` <= '"+simpleDateFormat.format(endTime)+"'");
            }
            sbSql.append(" GROUP BY DATE_FORMAT(t.`time`, '%Y-%m') ");
        } else if (status.equals("unComplete")){
            sbSql.append(unCompleteSql);
            if (startTime != null) {
                sbSql.append(" and t.`repay_day` >= '"+simpleDateFormat.format(startTime)+"'");
            }
            if (endTime != null) {
                sbSql.append(" and t.`repay_day` <= '"+simpleDateFormat.format(endTime)+"'");
            }
            sbSql.append(" GROUP BY DATE_FORMAT(t.`repay_day`, '%Y-%m') ");
        } else {
            sbSql.append(completeSql);
            if (startTime != null) {
                sbSql.append(" and t.`time` >= '"+simpleDateFormat.format(startTime)+"'");
            }
            if (endTime != null) {
                sbSql.append(" and t.`time` <= '"+simpleDateFormat.format(endTime)+"'");
            }
            sbSql.append(" GROUP BY DATE_FORMAT(t.`time`, '%Y-%m') ");
            sbSql.append(" UNION ALL ");
            sbSql.append(unCompleteSql);
            if (startTime != null) {
                sbSql.append(" and t.`repay_day` >= '"+simpleDateFormat.format(startTime)+"'");
            }
            if (endTime != null) {
                sbSql.append(" and t.`repay_day` <= '"+simpleDateFormat.format(endTime)+"'");
            }
            sbSql.append(" GROUP BY DATE_FORMAT(t.`repay_day`, '%Y-%m') ");
        }
        sbSql.append(") temp GROUP BY temp.repayTime ORDER BY temp.repayTime");
        return sbSql;
    }

    public List<CreditorRepayPlan> searchDetail(String repayTime){
        StringBuffer sbDetailSql = getDetailStringBuffer(repayTime);
        Query query = ht.getSessionFactory().getCurrentSession().createSQLQuery(sbDetailSql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> list = query.list();
        List<CreditorRepayPlan> returnList = new ArrayList<CreditorRepayPlan>();
        for (int i = 0;i < list.size();i++){
            CreditorRepayPlan creditorRepayPlan = new CreditorRepayPlan();
            creditorRepayPlan.setRepayDay(list.get(i).get("repayDay").toString());
            creditorRepayPlan.setRepayMoney(new BigDecimal(list.get(i).get("repayMoney").toString()).toString());
            creditorRepayPlan.setLoanName(list.get(i).get("loanName").toString());
            creditorRepayPlan.setLoanId(list.get(i).get("loanId").toString());
            creditorRepayPlan.setUserId(list.get(i).get("userId").toString());
            creditorRepayPlan.setStatus(list.get(i).get("repayStatus").toString());
            creditorRepayPlan.setActualDay(list.get(i).get("actualDay").toString());
            returnList.add(creditorRepayPlan);
        }
        return returnList;
    }
}
