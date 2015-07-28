package com.esoft.umpay.bankcard.service.impl;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.GsonUtil;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import org.apache.commons.logging.Log;
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
 * 签约协议
 */
@Service("umPayBindingAgreementOperation")
public class UmPayBindingAgreementOperation extends
        UmPayOperationServiceAbs<String> {

    @Resource
    private HibernateTemplate ht;
    @Resource
    private TrusteeshipOperationBO trusteeshipOperationBO;
    @Logger
    Log log;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TrusteeshipOperation createOperation(String userId, FacesContext facesContext) throws IOException {
        TrusteeshipAccount ta = getTrusteeshipAccount(userId);
        String hql = "from BankCard where user.id =? and (isOpenFastPayment <> ? or isOpenFastPayment is null) and status = ?";

        List<BankCard> userBankCard = ht.find(hql, new Object[]{userId, true,"passed"});
        if (userBankCard == null || userBankCard.size() == 0) {
            return null;
        }
        Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.MER_BIND_AGREEMENT);
        // 同步地址
        sendMap.put("ret_url", UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL
                + UmPayConstants.OperationType.MER_BIND_AGREEMENT);
        // 后台地址
        sendMap.put("notify_url",
                UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL
                        + UmPayConstants.OperationType.MER_BIND_AGREEMENT);
        sendMap.put("user_id", ta.getId());
        sendMap.put("account_id", ta.getAccountId());
        sendMap.put("user_bind_agreement_list", "ZKJP0700");
        TrusteeshipOperation to = null;
        try {
            // 加密参数
            ReqData reqData = Mer2Plat_v40.makeReqDataByPost(sendMap);
            log.debug("签约协议发送数据:" + reqData);
            // 保存操作记录
            to = createTrusteeshipOperation(ta.getId(), reqData.getUrl(),
                    userId,
                    UmPayConstants.OperationType.MER_BIND_AGREEMENT,
                    GsonUtil.fromMap2Json(reqData.getField()));
            // 发送请求
            sendOperation(to, facesContext);
        } catch (ReqDataException e) {
            log.error(e.getStackTrace());
        }
        return to;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveOperationPostCallback(ServletRequest request) throws TrusteeshipReturnException, IOException {
        Map<String, String> paramMap = null;
        try {
            // 解密
            paramMap = UmPaySignUtil.getMapDataByRequest(request);
            log.debug("签约协议-前台-通知:" + paramMap.toString());

            String ret_code = paramMap.get("ret_code");
            String user_id = paramMap.get("user_id");
            TrusteeshipAccount trusteeshipAccount = ht.get(TrusteeshipAccount.class,user_id);
            // 操作记录
            TrusteeshipOperation to = trusteeshipOperationBO.get(
                    UmPayConstants.OperationType.MER_BIND_AGREEMENT, user_id, trusteeshipAccount.getUser().getId(),
                    UmPayConstants.OperationType.UMPAY);


            if ("0000".equals(ret_code)) {

                to.setStatus(TrusteeshipConstants.Status.PASSED);
                to.setResponseTime(new Date());
                to.setResponseData(paramMap.toString());
                ht.update(to);
                String hql = "from BankCard where user.id =? and status = ? and isOpenFastPayment = false";
                List<BankCard> userBankCard = ht
                        .find(hql,new String[]{trusteeshipAccount.getUser().getId(),"passed"});
                if (null != userBankCard && userBankCard.size() >0) {
                    for (int i=0;i<userBankCard.size();i++) {
                        BankCard bankCard = new BankCard();
                        bankCard = userBankCard.get(i);
                        bankCard.setIsOpenFastPayment(true);
                        ht.update(bankCard);
                    }
                    log.debug(("用户:"
                            + userBankCard.get(0).getUser()
                            +"签约协议成功!"));
                }
            } else {
                to.setStatus(TrusteeshipConstants.Status.REFUSED);
                to.setResponseTime(new Date());
                to.setResponseData(paramMap.toString());
                ht.update(to);
                // 真实错误原因
                throw new RuntimeException(new TrusteeshipReturnException(
                        ret_code + ":" + paramMap.get("ret_msg")));
            }
        } catch (VerifyException e) {
            log.error(e.getStackTrace());
            throw new TrusteeshipReturnException("签约协议失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receiveOperationS2SCallback(ServletRequest request, ServletResponse response) {
        {
            try {
                // 解密
                Map<String, String> paramMap = UmPaySignUtil
                        .getMapDataByRequest(request);
                receiveOperationPostCallback(request);
                log.debug("签约协议-后台-通知:" + paramMap.toString());
                if (null != paramMap) {
                    String ret_code = paramMap.get("ret_code");
                    String user_id = paramMap.get("user_id");
                    TrusteeshipAccount trusteeshipAccount = ht.get(TrusteeshipAccount.class, user_id);
                    if ("0000".equals(ret_code)) {

                        String hql = "from BankCard where user.id =? and status = ? and isOpenFastPayment = false";
                        List<BankCard> userBankCard = ht
                                .find(hql,new String[]{trusteeshipAccount.getUser().getId(),"passed"});
                        if (null != userBankCard && userBankCard.size() >0) {
                            for (int i=0;i<userBankCard.size();i++) {
                                BankCard bankCard = new BankCard();
                                bankCard = userBankCard.get(i);
                                bankCard.setIsOpenFastPayment(true);
                                ht.update(bankCard);
                            }
                            log.debug(("用户:"
                                    + userBankCard.get(0).getUser()
                                    +"签约协议成功!"));
                        }
                    }
                    try {
                        response.setCharacterEncoding("utf-8");
                        // 返回数据
                        String responseData = getResponseData(user_id, ret_code);
                        response.getWriter().print(responseData);
                        FacesUtil.getCurrentInstance().responseComplete();
                    } catch (IOException e) {
                        log.error(e.getStackTrace());
                    }
                }
            } catch (VerifyException e) {
                log.error(e.getStackTrace());
            } catch (TrusteeshipReturnException e) {
               log.error(e.getStackTrace());
            } catch (IOException e) {
               log.error(e.getStackTrace());
            }
        }
    }
}
