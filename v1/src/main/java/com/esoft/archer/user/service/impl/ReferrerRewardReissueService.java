package com.esoft.archer.user.service.impl;

import com.esoft.archer.user.UserBillConstants;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserBill;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.invest.model.InvestUserReferrer;
import com.esoft.umpay.loan.service.impl.UmPayLoanMoneyService;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/2.
 */
@Service
public class ReferrerRewardReissueService {

    @Logger
    Log log;

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
            " WHERE t.`status` = 'fail' and t.`bonus` > 0 ";

    private static final String particAccType = UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON;

    private static final String transAction = UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT;

    private static final String transferOutDetailFormat = "推荐人奖励补发，投资:{0}, 订单:{1}, 推荐人:{2}";

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void reward(){
        log.debug("start referrer reward:");
        System.out.println("start referrer reward:");
        System.out.println(needReward);
        Query query = ht.getSessionFactory().getCurrentSession().createSQLQuery(needReward).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> resultNeedReward = query.list();
        log.debug("need deal with count:"+resultNeedReward.size());
        System.out.println("need deal with count:" + resultNeedReward.size());
        for (int i=0;i<resultNeedReward.size();i++) {
            String orderId = resultNeedReward.get(i).get("invest_id").toString() + System.currentTimeMillis();
            double bonus = Double.parseDouble(resultNeedReward.get(i).get("bonus").toString());
            String transferOutDetail = MessageFormat.format(transferOutDetailFormat, resultNeedReward.get(i).get("invest_id").toString(), orderId, resultNeedReward.get(i).get("referrer_id").toString());
            try {
                String returnMsg = umPayLoanMoneyService.giveMoney2ParticUserId(orderId, bonus, particAccType, transAction, resultNeedReward.get(i).get("account_id").toString(), transferOutDetail);
                if(returnMsg.split("\\|")[0].equals("0000")){
                    transferIntoBalance(resultNeedReward.get(i).get("referrer_id").toString(),bonus,"referrer_reward",transferOutDetail);
                    this.updateInvestUserReferrer(resultNeedReward.get(i).get("id").toString());
                    log.debug("No." + (i + 1) + "deal success");
                    System.out.println("No." + (i + 1) + "deal success");
                } else {
                    log.debug("No."+(i+1)+"deal failed reason umpay return");
                    System.out.println("No." + (i + 1) + "deal failed reason umpay return");
                }
            } catch (ReqDataException | RetDataException e) {
                log.debug("No."+(i+1)+"deal failed reason exception");
                System.out.println("No." + (i + 1) + "deal failed reason exception");
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

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void transferIntoBalance(String userId, double money,
                                    String operatorInfo, String operatorDetail) {
        if (money < 0) {
            throw new RuntimeException("money cannot be less than zero!");
        }
        UserBill ibLastest = getLastestBill(userId);
        UserBill lb = new UserBill();
        lb.setId(IdGenerator.randomUUID());
        lb.setMoney(money);
        lb.setTime(new Date());
        lb.setDetail(operatorDetail);
        lb.setType(UserBillConstants.Type.TI_BALANCE);
        lb.setTypeInfo(operatorInfo);
        lb.setUser(new User(userId));
        lb.setOperator(userId);

        if (ibLastest == null) {
            lb.setSeqNum(1L);
            // 余额=money
            lb.setBalance(money);
            // 最新冻结金额=上一条冻结-取出的
            lb.setFrozenMoney(0D);
        } else {
            lb.setSeqNum(ibLastest.getSeqNum() + 1);
            // 余额=上一条余额+money
            lb.setBalance(ArithUtil.add(ibLastest.getBalance(), money));
            // 最新冻结金额=上一条冻结
            lb.setFrozenMoney(ibLastest.getFrozenMoney());
        }
        ht.save(lb);
    }

    public UserBill getLastestBill(String userId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(UserBill.class);
        criteria.addOrder(Order.desc("seqNum"));
        criteria.setLockMode(LockMode.UPGRADE);
        criteria.add(Restrictions.eq("user.id", userId));
        List<UserBill> ibs = ht.findByCriteria(criteria, 0, 1);
        if (ibs.size() > 0) {
            UserBill ub = ibs.get(0);
            if (ub.getBalance() == null || ub.getFrozenMoney() == null) {
                if (ub.getBalance() == null) {
                    double freeze = getSumByType(userId,
                            UserBillConstants.Type.FREEZE);
                    double transferIntoBalance = getSumByType(userId,
                            UserBillConstants.Type.TI_BALANCE);
                    double transferOutFromBalance = getSumByType(userId,
                            UserBillConstants.Type.TO_BALANCE);
                    double unfreeze = getSumByType(userId,
                            UserBillConstants.Type.UNFREEZE);
                    ub.setBalance(ArithUtil.add(ArithUtil.sub(ArithUtil.sub(
                                    transferIntoBalance, transferOutFromBalance),
                            freeze), unfreeze));
                }
                if (ub.getFrozenMoney() == null) {
                    double freeze = getSumByType(userId,
                            UserBillConstants.Type.FREEZE);
                    double transferOutFromFrozen = getSumByType(userId,
                            UserBillConstants.Type.TO_FROZEN);
                    double unfreeze = getSumByType(userId,
                            UserBillConstants.Type.UNFREEZE);
                    ub.setFrozenMoney(ArithUtil.sub(
                            ArithUtil.sub(freeze, unfreeze),
                            transferOutFromFrozen));
                }
                ht.update(ub);
            }
            return ub;
        }
        return null;
    }

    private double getSumByType(String userId, String type) {
        String hql = "select sum(ub.money) from UserBill ub where ub.user.id =? and ub.type=?";
        Double sum = (Double) ht.find(hql, new String[] { userId, type })
                .get(0);
        if (sum == null) {
            return 0;
        }
        return ArithUtil.round(sum.doubleValue(), 2);
    }
}
