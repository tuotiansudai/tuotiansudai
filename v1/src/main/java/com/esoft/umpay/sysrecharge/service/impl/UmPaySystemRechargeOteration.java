package com.esoft.umpay.sysrecharge.service.impl;

import com.esoft.archer.user.UserBillConstants;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.UserConstants.RechargeStatus;
import com.esoft.archer.user.service.UserBillService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.risk.SystemBillConstants;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.jdp2p.user.service.RechargeService;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.sysrecharge.model.SystemRecharge;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description : 平台充值操作(个人向平台转账, transfer_asyn)
 *
 * @author zt
 * @data 2015-3-9下午7:46:43
 */
@Service("umPaySystemRechargeOteration")
public class UmPaySystemRechargeOteration extends UmPayOperationServiceAbs<SystemRecharge> {

    @Resource
    RechargeService rechargeService;

    @Resource
    HibernateTemplate ht;

    @Resource
    TrusteeshipOperationBO trusteeshipOperationBO;

    @Resource
    UserBillService userBillService;

    @Resource
    SystemBillService systemBillService;

    @Logger
    static Log log;

    /**
     * 发送请求
     */
    @Transactional(rollbackFor = Exception.class)
    public TrusteeshipOperation createOperation(SystemRecharge recharge,
                                                FacesContext facesContext) throws IOException {
        recharge.setId(generateId());
        Map<String, String> sendMap = assembleSendMap(recharge);
        TrusteeshipOperation trusteeshipOperation = null;
        try {
            // 加密参数
            ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
            // 保存操作记录
            trusteeshipOperation = createTrusteeshipOperation(recharge.getId(), reqData.getUrl(),
                    recharge.getId(),
                    UmPayConstants.OperationType.TRANSFER_ASYN,
                    GsonUtil.fromMap2Json(reqData.getField()));
            // 发送请求
            sendOperation(trusteeshipOperation, facesContext);
        } catch (ReqDataException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return trusteeshipOperation;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> assembleSendMap(SystemRecharge recharge) {
        TrusteeshipAccount ta = getTrusteeshipAccount(recharge.getUser().getId());
        // 保存一个订单
        String id = createSystemRechargeOrder(recharge);
        // 获取拼装map
        Map<String, String> sendMap = UmPaySignUtil
                .getSendMapDate(UmPayConstants.OperationType.TRANSFER_ASYN);
        // 同步地址
        sendMap.put("ret_url", UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
                + UmPayConstants.OperationType.TRANSFER_ASYN);
        // 异步地址
        sendMap.put("notify_url", UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL
                + UmPayConstants.OperationType.TRANSFER_ASYN);
        sendMap.put("order_id", id);
        sendMap.put("mer_date", DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
        sendMap.put("partic_user_id", ta.getId());
        sendMap.put("partic_acc_type", "01");
        // 充值的时候必须发送的请求是整数倍 而且单位是分
        int amount = (int) (recharge.getMoney().doubleValue() * 100);
        // 充值金额
        sendMap.put("amount", String.valueOf(amount));
        return sendMap;
    }

    /**
     * WEB通知
     */
    @Override
    @Transactional
    public void receiveOperationPostCallback(ServletRequest request)
            throws TrusteeshipReturnException {
        Map<String, String> paramMap = null;
        try {
            log.info("system-recharge-web-data:" + paramMap);
            paramMap = UmPaySignUtil.getMapDataByRequest(request);
            // 状态码
            String ret_code = paramMap.get("ret_code");

            receiveOperationCallback(paramMap);
            if (!"0000".equals(ret_code)) {
                // 错误原因
                throw new TrusteeshipReturnException("错误代码:" + ret_code
                        + "-错误信息:" + paramMap.get("ret_msg"));
            }

        } catch (VerifyException e) {
            log.debug(e);
            throw new TrusteeshipReturnException("验签失败!");
        }
    }

    /**
     * S2S通知
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveOperationS2SCallback(ServletRequest request,
                                            ServletResponse response) {
        Map<String, String> paramMap = null;
        try {
            log.info("recharge-s2s-data:" + paramMap);
            paramMap = UmPaySignUtil.getMapDataByRequest(request);
            if (null != paramMap) {
                // 状态码
                String ret_code = paramMap.get("ret_code");
                // 订单编号
                String order_id = paramMap.get("order_id");

                receiveOperationCallback(paramMap);

                // 不管充值成功还是失败,上面只要通过了就证明验签没问题,至于充值失败也要返回给umpay数据
                try {
                    response.setCharacterEncoding("utf-8");
                    // 返回数据
                    String responseData = getResponseData(order_id, ret_code);
                    response.getWriter().print(responseData);
                    FacesUtil.getCurrentInstance().responseComplete();
                } catch (IOException e) {
                    log.debug(e);
                }
            }
        } catch (VerifyException e) {
            log.debug(e);
            throw new RuntimeException("验签失败!");
        }

    }

    private String generateId() {
        String gid = DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD);
        String hql = "select recharge from SystemRecharge recharge where recharge.id = (select max(rechargeM.id) from SystemRecharge rechargeM where rechargeM.id like ?)";
        List<SystemRecharge> contractList = ht.find(hql, gid + "%");
        Integer itemp = 0;
        if (contractList.size() == 1) {
            SystemRecharge recharge = contractList.get(0);
            ht.lock(recharge, LockMode.UPGRADE);
            Session session = null;
            try {
                session = ht.getSessionFactory().openSession();
                List<SystemRecharge> rechargeList = session.createQuery(hql).setParameter(0, gid + "%").list();
                String temp = rechargeList.get(0).getId();
                temp = temp.substring(temp.length() - 6);
                itemp = Integer.valueOf(temp);
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }
        itemp++;
        gid += String.format("%04d", itemp);
        return gid;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    private String createSystemRechargeOrder(SystemRecharge recharge) {
        recharge.setTime(new Date());
        recharge.setStatus(UserConstants.RechargeStatus.WAIT_PAY);
        ht.save(recharge);
        return recharge.getId();
    }

    private void receiveOperationCallback(Map<String, String> paramMap) {
        // 状态码
        String ret_code = paramMap.get("ret_code");
        // 订单编号
        String order_id = paramMap.get("order_id");
        // 操作记录
        TrusteeshipOperation to = trusteeshipOperationBO.get(
                UmPayConstants.OperationType.TRANSFER_ASYN, order_id,
                UmPayConstants.OperationType.UMPAY);
        // 充值成功
        if ("0000".equals(ret_code)) {
            to.setStatus(TrusteeshipConstants.Status.PASSED);
            to.setResponseTime(new Date());
            to.setResponseData(paramMap.toString());
            ht.update(to);
            systemRechargeSuccessCallBack(order_id);
        } else {
            to.setStatus(TrusteeshipConstants.Status.REFUSED);
            to.setResponseData(paramMap.toString());
            ht.update(to);
            systemRechargeFailCallBack(order_id);
        }
    }

    private void systemRechargeFailCallBack(String orderId) {
        // 充值失败
        SystemRecharge recharge = ht.get(SystemRecharge.class, orderId);
        recharge.setStatus(RechargeStatus.FAIL);
        ht.update(recharge);
    }

    private void systemRechargeSuccessCallBack(String orderId) {
        // 充值成功
        SystemRecharge recharge = ht.get(SystemRecharge.class, orderId);
        // 成功后不再处理
        if(RechargeStatus.SUCCESS.equals(recharge.getStatus())){
            return;
        }
        recharge.setStatus(RechargeStatus.SUCCESS);
        ht.update(recharge);
        try {
            systemBillService.transferInto(recharge.getMoney(),
                    UserBillConstants.OperatorInfo.ADMIN_OPERATION,
                    recharge.getRemark()
            );
            userBillService.transferOutFromBalance(recharge.getUser().getId(),
                    recharge.getMoney(),
                    UserBillConstants.OperatorInfo.ADMIN_OPERATION,
                    recharge.getRemark()
            );
        } catch (InsufficientBalance insufficientBalance) {
            log.error(insufficientBalance);
        }
    }
}
