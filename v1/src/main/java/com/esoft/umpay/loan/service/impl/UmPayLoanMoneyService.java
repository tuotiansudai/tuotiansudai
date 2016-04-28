package com.esoft.umpay.loan.service.impl;

import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.jdp2p.trusteeship.service.impl.TrusteeshipOperationBO;
import com.esoft.umpay.repay.service.impl.UmPayImitateSign;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;
import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

/**
 * Description :控制第三方标的里面的资金
 *
 * @author zt
 * @data 2015-3-25上午11:54:11
 */
@Service("umPayLoanMoneyService")
public class UmPayLoanMoneyService {

    @Resource
    TrusteeshipOperationBO trusteeshipOperationBO;

    @Resource
    HibernateTemplate ht;

    @Resource
    UmPayImitateSign umPayImitateSignService;

    @Resource
    SystemBillService systemBillService;

    @Logger
    Log log;

    /**
     * 平台收费
     *
     * @param orderId   订单号
     * @param money     订单金额
     * @param projectId 项目ID
     * @throws ReqDataException
     * @throws RetDataException
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public void loanMoney2Mer(String orderId, Double money, String projectId) throws ReqDataException, RetDataException {
        DecimalFormat currentNumberFormat = new DecimalFormat("#");
        // 标的转账接口
        Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.PROJECT_TRANSFER);
        // 传入参数
        sendMap.put("order_id", orderId);
        sendMap.put("project_id", projectId);
        sendMap.put("amount", currentNumberFormat.format(money * 100)); // 单位为分
        // 其他参数
        sendMap.put("ret_url", UmPayConstants.ResponseWebUrl.PRE_RESPONSE_URL);
        sendMap.put("notify_url", UmPayConstants.ResponseS2SUrl.PRE_RESPONSE_URL);
        sendMap.put("mer_date", DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
        sendMap.put("serv_type", UmPayConstants.TransferProjectStatus.SERV_TYPE_PLATFORM_FEE);
        sendMap.put("trans_action", UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT);
        sendMap.put("partic_type", UmPayConstants.TransferProjectStatus.PARTIC_TYPE_P2P);
        sendMap.put("partic_acc_type", UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_MER);
        sendMap.put("partic_user_id", UmPayConstants.Config.MER_CODE); // 这里是商户号
        ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
        log.debug("第三方标的转账-平台收费-发送数据: " + reqData.toString());
        TrusteeshipOperation to = createTrusteeshipOperation(orderId, reqData.getUrl(), projectId, UmPayConstants.OperationType.PROJECT_TRANSFER, reqData.getPlain());
        String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(to.getRequestUrl());
        Map<String, String> resData = Plat2Mer_v40.getResData(responseBodyAsString);
        log.debug("第三方标的转账-平台收费-接收数据: " + resData.toString());
        String ret_code = resData.get("ret_code");
        if ("0000".equals(ret_code)) { // 成功将钱划给商户
            log.debug("标的转账-平台收费-成功!");
            to.setStatus(TrusteeshipConstants.Status.PASSED);
        } else {
            log.error("标的转账-平台收费-失败-编号:" + orderId + "-原因: " + resData.get("ret_msg"));
            to.setStatus(TrusteeshipConstants.Status.REFUSED);
        }
        to.setResponseData(resData.toString());
        to.setResponseTime(new Date());
        ht.update(to);
    }

    /**
     * 普通转账免密(划账)
     *
     * @param orderId       订单号
     * @param money         订单金额
     * @param particAccType 转账方账户类型(实际收款方)
     * @param transAction   转账方向
     * @param particUserId  转账方用户号(实际收款方)
     * @throws ReqDataException
     * @throws RetDataException
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public String giveMoney2ParticUserId(String orderId, Double money, String particAccType, String transAction, String particUserId, String transferOutDetail) throws ReqDataException, RetDataException {
        DecimalFormat currentNumberFormat = new DecimalFormat("#");
        // 平台划账接口
        Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.TRANSFER);
        // 传入参数
        sendMap.put("order_id", orderId);
        sendMap.put("partic_acc_type", particAccType); // 转账方账户类型(实际收款方)
        sendMap.put("trans_action", transAction); // 转账方向
        sendMap.put("partic_user_id", particUserId); //转账方用户号(实际收款方)
        sendMap.put("amount", currentNumberFormat.format(money * 100)); // 单位为分
        // 其他参数
        sendMap.put("mer_date", DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
        ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
        log.debug("普通转账免密(划账)-发送数据: " + reqData.toString());
        TrusteeshipOperation to = createTrusteeshipOperation(orderId, reqData.getUrl(), "projectId:" + orderId, UmPayConstants.OperationType.TRANSFER, reqData.getPlain());
        String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(to.getRequestUrl());
        Map<String, String> resData = Plat2Mer_v40.getResData(responseBodyAsString);
        //Map<String, String> resData = umPayImitateSignService.getResData(responseBodyAsString);
        log.debug("普通转账免密(划账)-接收数据: " + resData.toString());
        String ret_code = resData.get("ret_code");

        if ("0000".equals(ret_code)) { // 成功划账
            log.debug("普通转账免密(划账)-成功!");
            to.setStatus(TrusteeshipConstants.Status.PASSED);
            try {
                systemBillService.transferOut(money, "referrer_reward", transferOutDetail);
            } catch (InsufficientBalance insufficientBalance) {
                log.error(insufficientBalance.getLocalizedMessage(), insufficientBalance);
            }
        } else {
            log.error("普通转账免密(划账)-失败-编号:" + orderId + "-原因: " + resData.get("ret_msg"));
            to.setStatus(TrusteeshipConstants.Status.REFUSED);
        }
        to.setResponseData(resData.toString());
        to.setResponseTime(new Date());
        ht.update(to);
        StringBuffer sb = new StringBuffer(ret_code);
        sb.append("|");
        sb.append(resData.get("ret_msg") != null ? resData.get("ret_msg") : "联动优势划账成功");
        return sb.toString();
    }

    /**
     * 将标的里面的钱转给投资人
     *
     * @param orderId   订单号
     * @param money     订单金额
     * @param projectId 项目ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void loanMoney2Invester(String orderId, Double money,
                                   String projectId) {
    }

    /**
     * 保存操作
     *
     * @param markId
     * @param requestUrl
     * @param operator
     * @param type
     * @param content
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public TrusteeshipOperation createTrusteeshipOperation(String markId,
                                                           String requestUrl, String operator, String type, String content) {
        TrusteeshipOperation to = new TrusteeshipOperation();
        to.setId(IdGenerator.randomUUID());
        to.setMarkId(markId);
        to.setOperator(operator);
        to.setRequestUrl(requestUrl);
        to.setCharset("utf-8");
        to.setRequestData(content);
        to.setType(type);
        to.setTrusteeship("umpay");
        to.setRequestTime(new Date());
        to.setStatus(TrusteeshipConstants.Status.SENDED);
        trusteeshipOperationBO.save(to);
        return to;
    }

}
