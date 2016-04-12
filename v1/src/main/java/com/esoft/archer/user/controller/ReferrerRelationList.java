package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.ReferrerInvest;
import com.esoft.archer.user.model.ReferrerRelation;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.ReferGradePtSysService;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.loan.LoanConstants;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Scope(ScopeType.VIEW)
public class ReferrerRelationList extends EntityQuery<User> {

    @Resource
    private LoginUserInfo loginUserInfo;
    @Resource
    private UserService userService;
    @Resource
    private ReferGradePtSysService referGradePtSysService;

    private int investRewardPageSize = 10;

    private int currentInvestRewardPage = 1;

    private int resultRowCount;

    public static final String INVESTOR = "INVESTOR";

    public static final String ROLE_MERCHANDISER = "ROLE_MERCHANDISER";

    private Date registerTimeStart;

    private Date registerTimeEnd;

    private String userName;

    private String referrerName;

    private Date rewardTimeStart;

    private Date rewardTimeEnd;

    private static final String LAZY_MODEL = "select distinct user from User user inner join user.referrers referrer where referrer.id=''{0}'' ";

    private static final String LAZY_MODEL_COUNT = "select count(distinct user) from User user inner join user.referrers referrer where referrer.id=''{0}''  ";

    private static final String QUERY_LEVEL = "select relation.level from ReferrerRelation relation where relation.referrerId=''{0}'' and relation.userId=''{1}''";

    public Date getRegisterTimeEnd() {
        return registerTimeEnd;
    }

    public void setRegisterTimeEnd(Date registerTimeEnd) {
        this.registerTimeEnd = registerTimeEnd;
    }

    public Date getRegisterTimeStart() {
        return registerTimeStart;
    }

    public void setRegisterTimeStart(Date registerTimeStart) {
        this.registerTimeStart = registerTimeStart;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReferrerName() {
        return referrerName;
    }

    public void setReferrerName(String referrerName) {
        this.referrerName = referrerName;
    }

    public Date getRewardTimeStart() {
        return rewardTimeStart;
    }

    public void setRewardTimeStart(Date rewardTimeStart) {
        this.rewardTimeStart = rewardTimeStart;
    }

    public Date getRewardTimeEnd() {
        return rewardTimeEnd;
    }

    public void setRewardTimeEnd(Date rewardTimeEnd) {
        this.rewardTimeEnd = rewardTimeEnd;
    }

    public int getInvestRewardPageSize() {
        return investRewardPageSize;
    }

    public void setInvestRewardPageSize(int investRewardPageSize) {
        this.investRewardPageSize = investRewardPageSize;
    }

    public int getCurrentInvestRewardPage() {
        return currentInvestRewardPage;
    }

    public void setCurrentInvestRewardPage(int currentInvestRewardPage) {
        this.currentInvestRewardPage = currentInvestRewardPage;
    }

    public int getResultRowCount() {
        List<ReferrerInvest> referrerInvestList = getReferrerInvestList(false);
        return referrerInvestList.size();
    }

    public void setResultRowCount(int resultRowCount) {
        this.resultRowCount = resultRowCount;
    }

    @Override

    public List<User> getLazyModelData() {
        setCountHql(MessageFormat.format(LAZY_MODEL_COUNT, loginUserInfo.getLoginUserId()));
        setHql(MessageFormat.format(LAZY_MODEL, loginUserInfo.getLoginUserId()));
        final String[] RESTRICTIONS = {"user.registerTime >= #{referrerRelationList.registerTimeStart}",
                "user.registerTime <= #{referrerRelationList.registerTimeEnd}",
                "user.username like #{referrerRelationList.userName}",
        };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        return super.getLazyModelData();
    }

    public List<ReferrerInvest> getReferrerInvestList(boolean flag) {
        List<ReferrerInvest> listResult = Lists.newArrayList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String referrerId = loginUserInfo.getLoginUserId();
        String sql = "SELECT " +
                "  n.`investUserId`, " +
                "  n.`level`, " +
                "  n.`investMoney`, " +
                "  n.`investTime`, " +
                "  n.`rewardMoney`, " +
                "  n.`rewardTime`, " +
                "  n.`loanId`, " +
                "  l.`name` AS `loanName`, " +
                "  l.`loan_activity_type` AS `loanActivityType`,"+
                "  l.`deadline` AS `deadLine` "+
                "FROM " +
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
                "          ON t.`invest_id` = i.`id` ";
                if (StringUtils.isNotEmpty(referrerName)) {
                    sql += " AND i.`user_id` = ''"+referrerName+"'' ";
                }
               sql += " AND i.`status` NOT IN (''{0}'', ''{1}'') " +
                "      WHERE t.`referrer_id` = ''{2}'' ";
                if (rewardTimeStart != null) {
                    sql += " AND t.`time` >= ''"+simpleDateFormat.format(rewardTimeStart)+"'' ";
                }
                if (rewardTimeEnd != null) {
                    sql += " AND t.`time` <= ''"+simpleDateFormat.format(rewardTimeEnd)+"'' ";
                }
                sql += ") temp " +
                "      ON r.`user_id` = temp.`investUserId` " +
                "      AND r.`referrer_id` = temp.`referrer_id`) n " +
                "  JOIN loan l " +
                "    ON n.`loanId` = l.`id` " +
                "    AND l.`status` IN (''{3}'', ''{4}'') " +
                " ORDER BY n.`rewardTime` DESC ";
                if (flag) {
                 sql +=  " LIMIT " + (currentInvestRewardPage - 1) * 10 + "," + investRewardPageSize;
                }
        String finalSql = MessageFormat.format(sql, InvestConstants.InvestStatus.CANCEL, InvestConstants.InvestStatus.UNFINISHED, referrerId, LoanConstants.LoanStatus.COMPLETE, LoanConstants.LoanStatus.REPAYING);
        Query query = getHt().getSessionFactory().getCurrentSession().createSQLQuery(finalSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> result = query.list();
        for (int i=0;i<result.size();i++) {
            ReferrerInvest referrerInvest = new ReferrerInvest();
            referrerInvest.setInvestUserId(result.get(i).get("investUserId").toString());
            referrerInvest.setLevel(Integer.parseInt(result.get(i).get("level").toString()));
            referrerInvest.setInvestMoney(Double.parseDouble(result.get(i).get("investMoney").toString()));
            referrerInvest.setInvestTime(result.get(i).get("investTime").toString().substring(0, 10));
            referrerInvest.setRewardMoney(Double.parseDouble(result.get(i).get("rewardMoney").toString()));
            referrerInvest.setLoanId(result.get(i).get("loanId").toString());
            referrerInvest.setLoanName(result.get(i).get("loanName").toString());
            referrerInvest.setLoanActivityType(result.get(i).get("loanActivityType").toString());
            referrerInvest.setRewardTime(result.get(i).get("rewardTime").toString().substring(0,10));
            referrerInvest.setDeadLine(Integer.parseInt(result.get(i).get("deadLine").toString()));
            listResult.add(referrerInvest);
        }
        return listResult;
    }

    public double getRewardTotalMoney() {
        double resultMoney = 0.0;
        List<ReferrerInvest> referrerInvestList = getReferrerInvestList(false);
        for (int i=0;i<referrerInvestList.size();i++) {
            resultMoney += referrerInvestList.get(i).getRewardMoney();
        }
        BigDecimal bigDecimal = new BigDecimal(resultMoney);
        return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public List<ReferrerRelation> getReferrerRelations() {

        String referrerId = loginUserInfo.getLoginUserId();
        Integer maxLevel =  getMaxGradeByRoleUserId(referrerId);
        List<User> refereeList = this.getLazyModelData();
        List<ReferrerRelation> relations = Lists.newArrayList();
        for (User referee : refereeList) {
            ReferrerRelation relation = new ReferrerRelation();
            Integer level = (Integer) getHt().find(MessageFormat.format(QUERY_LEVEL, referrerId, referee.getId())).get(0);
            if ( level != null && level.intValue()> maxLevel.intValue()){
                continue;
            }
            relation.setUser(referee);
            relation.setLevel(level);
            relations.add(relation);
        }
        return relations;
    }

    private Integer getMaxGradeByRoleUserId(String userId) {
        boolean isMerchandiser = userService.hasRole(userId, ROLE_MERCHANDISER);//是否业务员
        boolean isInvestor = false;//是否投资人
        Integer grade = -1;

        if (isMerchandiser){
            grade = referGradePtSysService.getMerchandiserMaxGrade();
        }else{
            isInvestor = userService.hasRole(userId,INVESTOR);
            if(isInvestor){
                grade = referGradePtSysService.getInvestMaxGrade();
            }
        }
        return grade;
    }


}
