package com.ttsd.api.dao.impl;

import com.esoft.archer.user.service.ReferGradePtSysService;
import com.esoft.archer.user.service.UserService;
import com.ttsd.api.dao.MobileAppReferrerListDao;
import com.ttsd.api.dto.ReferrerResponseDataDto;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppReferrerListDaoImpl implements MobileAppReferrerListDao {

    public static final String INVESTOR = "INVESTOR";

    public static final String ROLE_MERCHANDISER = "ROLE_MERCHANDISER";

    @Autowired
    UserService userService;
    @Autowired
    ReferGradePtSysService referGradePtSysService;
    @Resource
    private HibernateTemplate ht;

    private static String referrerRelationListSql = "select r.referrer_id,r.user_id,r.level,u.register_time from referrer_relation r join user u on  r.user_id = u.id "
            + "where r.referrer_id = ? and r.level <= (select max(grade) from globle_refer_grade_profitrate where grade_role = ? )  limit ?,?";

    private static String referrerRelationCountSql = "select count(*) from referrer_relation r join user u on  r.user_id = u.id "
            + " where r.referrer_id = ? and r.level <= (select max(grade) from globle_refer_grade_profitrate where grade_role = ?) ";


    @Override
    public Integer getTotalCount(String referrerId) {
        String userRole = this.getUserRoleByUserId(referrerId);
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(referrerRelationCountSql);
        sqlQuery.setParameter(0, referrerId);
        sqlQuery.setParameter(1,userRole);
        return ((Number) sqlQuery.uniqueResult()).intValue();
    }



    @Override
    public List<ReferrerResponseDataDto> getReferrerRelationList(Integer index, Integer pageSize, String referrerId) {
        String userRole = this.getUserRoleByUserId(referrerId);
        List<ReferrerResponseDataDto> referrerResponseDataDtos = new ArrayList<>();

        Query sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(referrerRelationListSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        sqlQuery.setParameter(0, referrerId);
        sqlQuery.setParameter(1,userRole);
        sqlQuery.setParameter(2, (index - 1) * pageSize);
        sqlQuery.setParameter(3, pageSize);
        List<Map<String, Object>> result = sqlQuery.list();
        for (int i=0;i<result.size();i++) {
            ReferrerResponseDataDto referrerResponseDataDto = new ReferrerResponseDataDto();
            referrerResponseDataDto.setUserId(result.get(i).get("user_id").toString());
            referrerResponseDataDto.setTime(result.get(i).get("register_time").toString().substring(0, 10));
            referrerResponseDataDto.setLevel(result.get(i).get("level").toString());
            referrerResponseDataDtos.add(referrerResponseDataDto);
        }

        return referrerResponseDataDtos;
    }
    @Override
    public String getUserRoleByUserId(String userId) {
        boolean isMerchandiser = userService.hasRole(userId, ROLE_MERCHANDISER);//是否业务员
        boolean isInvestor = false;//是否投资人
        String userRole = "";
        if (isMerchandiser) {
            userRole = ROLE_MERCHANDISER;
        } else {
            isInvestor = userService.hasRole(userId, INVESTOR);
            if (isInvestor) {
                userRole = INVESTOR;
            }
        }
        return userRole;
    }


}
