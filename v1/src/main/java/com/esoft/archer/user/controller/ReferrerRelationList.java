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
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
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


    public static final String INVESTOR = "INVESTOR";

    public static final String ROLE_MERCHANDISER = "ROLE_MERCHANDISER";

    private Date registerTimeStart;

    private Date registerTimeEnd;

    private String userName;

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

    public List<ReferrerInvest> getReferrerInvestList() {
        List<ReferrerInvest> listResult = Lists.newArrayList();
        String referrerId = loginUserInfo.getLoginUserId();
        String sql = "SELECT " +
                "  temp.`investUserId`, " +
                "  r.`level`, " +
                "  temp.`investMoney`, " +
                "  temp.`investTime`, " +
                "  temp.`rewardMoney`, " +
                "  temp.`rewardRate`, " +
                "  temp.`rewardTime` " +
                "FROM " +
                "  referrer_relation r  " +
                "  JOIN  " +
                "    (SELECT  " +
                "      i.`user_id` AS investUserId, " +
                "      i.`money` AS investMoney, " +
                "      i.`time` AS investTime, " +
                "      t.`bonus` AS rewardMoney, " +
                "      IF( " +
                "        i.`money` != 0, " +
                "        ROUND((t.`bonus` * 100) / i.`money`,2), " +
                "        0 " +
                "      ) AS rewardRate, " +
                "      t.`time` AS rewardTime, " +
                "      t.`referrer_id` " +
                "    FROM " +
                "      invest_userReferrer t  " +
                "      JOIN invest i  " +
                "        ON t.`invest_id` = i.`id`  " ;
        if (StringUtils.isNotEmpty(userName)) {
            sql += "AND i.`user_id` LIKE #{referrerRelationList.userName} ";
        }
        sql += "        AND i.`status` NOT IN (''{0}'', ''{1}'')  " +
                "    WHERE t.`referrer_id` = ''{2}''" ;
        if () {
            sql += " AND t.`time` BETWEEN '"++"' AND '"++"'";
        }
        sql += " ) temp  " +
                "    ON r.`user_id` = temp.`investUserId`  " +
                "    AND r.`referrer_id` = temp.`referrer_id`  " +
                "ORDER BY temp.`rewardTime` DESC " +
                "limit 0,7";
        List<Map<String, Object>> result = getHt().getSessionFactory().getCurrentSession().createSQLQuery(MessageFormat.format(sql, InvestConstants.InvestStatus.CANCEL,InvestConstants.InvestStatus.UNFINISHED,referrerId)).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        for (int i=0;i<result.size();i++) {
            ReferrerInvest referrerInvest = new ReferrerInvest();
            referrerInvest.setInvestUserId(result.get(i).get("investUserId").toString());
            referrerInvest.setLevel(Integer.parseInt(result.get(i).get("level").toString()));
            referrerInvest.setInvestMoney(Double.parseDouble(result.get(i).get("investMoney").toString()));
            referrerInvest.setInvestTime(result.get(i).get("investTime").toString().substring(0,10));
            referrerInvest.setRewardMoney(Double.parseDouble(result.get(i).get("rewardMoney").toString()));
            referrerInvest.setRewardRate(Double.parseDouble(result.get(i).get("rewardRate").toString()));
            referrerInvest.setRewardTime(result.get(i).get("rewardTime").toString().substring(0,10));
            listResult.add(referrerInvest);
        }
        return listResult;
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
