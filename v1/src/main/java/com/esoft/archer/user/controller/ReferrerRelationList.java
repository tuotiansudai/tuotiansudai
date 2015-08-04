package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.ReferrerInvest;
import com.esoft.archer.user.model.ReferrerRelation;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.ReferGradePtSysService;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.ScopeType;
import com.google.common.collect.Lists;
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
        String sql = "";
        List<Map<String, Object>> result = getHt().getSessionFactory().getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        for (int i=0;i<result.size();i++) {
            ReferrerInvest referrerInvest = new ReferrerInvest();
            referrerInvest.setInvestUserId(result.get(i).get("investUserId").toString());
            referrerInvest.setLevel(Integer.parseInt(result.get(i).get("level").toString()));
            referrerInvest.setInvestMoney(Double.parseDouble(result.get(i).get("investMoney").toString()));
            referrerInvest.setInvestTime();
            referrerInvest.setRewardMoney();
            referrerInvest.setRewardRate();
            referrerInvest.setRewardTime();
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
