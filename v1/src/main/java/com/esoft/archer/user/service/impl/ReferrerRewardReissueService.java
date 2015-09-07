package com.esoft.archer.user.service.impl;

import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.invest.model.InvestUserReferrer;
import com.esoft.umpay.loan.service.impl.UmPayLoanMoneyService;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import org.apache.commons.logging.Log;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/2.
 */
@Service
public class ReferrerRewardReissueService {

    @Logger
    Log log;

    @Autowired
    private UserBillBO userBillBO;

    @Resource
    private HibernateTemplate ht;

    @Resource
    private UmPayLoanMoneyService umPayLoanMoneyService;

    private String needReward = "SELECT" +
            "  t.`referrer_id`," +
            "  t.`bonus`," +
            "  t.`id`, " +
            "  t.`invest_id`, "+
            "  m.`id` AS account_id "+
            " FROM" +
            "  invest_userReferrer t " +
            "  JOIN trusteeship_account m " +
            "    ON t.`referrer_id` = m.`user_id` " +
            " WHERE t.`status` = 'fail' and t.`bonus` > 0 and t.`referrer_id` = 'hourglasskoala' ";

    private static final String particAccType = UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON;

    private static final String transAction = UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT;

    private static final String transferOutDetailFormat = "推荐人奖励补发，投资:{0}, 订单:{1}, 推荐人:{2}";

    @Transactional
    public void reward(){
        log.debug("start referrer reward:");
        System.out.println(needReward);
        Query query = ht.getSessionFactory().getCurrentSession().createSQLQuery(needReward).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> resultNeedReward = query.list();
        log.debug("need deal with count:"+resultNeedReward.size());
        for (int i=0;i<resultNeedReward.size();i++) {
            String orderId = resultNeedReward.get(i).get("invest_id").toString() + System.currentTimeMillis();
            double bonus = Double.parseDouble(resultNeedReward.get(i).get("bonus").toString());
            String transferOutDetail = MessageFormat.format(transferOutDetailFormat, resultNeedReward.get(i).get("invest_id").toString(), orderId, resultNeedReward.get(i).get("referrer_id").toString());
            try {
                String returnMsg = umPayLoanMoneyService.giveMoney2ParticUserId(orderId, bonus, particAccType, transAction, resultNeedReward.get(i).get("account_id").toString(), transferOutDetail);
                if(returnMsg.split("\\|")[0].equals("0000")){
                    userBillBO.transferIntoBalance(resultNeedReward.get(i).get("referrer_id").toString(),bonus,"referrer_reward",transferOutDetail);
                    this.updateInvestUserReferrer(resultNeedReward.get(i).get("id").toString());
                    log.debug("No."+(i+1)+"deal success");
                } else {
                    log.debug("No."+(i+1)+"deal failed reason umpay return");
                }
            } catch (ReqDataException | RetDataException e) {
                log.debug("No."+(i+1)+"deal failed reason exception");
                log.error(e.getLocalizedMessage(), e);
                continue;
            }
        }
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    private void updateInvestUserReferrer(String id) {
        InvestUserReferrer investUserReferrer = ht.get(InvestUserReferrer.class, id);
        investUserReferrer.setStatus(InvestUserReferrer.SUCCESS);
        ht.update(investUserReferrer);
    }

}
