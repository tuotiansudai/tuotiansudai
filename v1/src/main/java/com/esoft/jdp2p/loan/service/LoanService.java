package com.esoft.jdp2p.loan.service;

import java.util.Date;
import java.util.List;

import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.exception.BorrowedMoneyTooLittle;
import com.esoft.jdp2p.loan.exception.ExistWaitAffirmInvests;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.exception.InvalidExpectTimeException;
import com.esoft.jdp2p.loan.model.ApplyEnterpriseLoan;
import com.esoft.jdp2p.loan.model.Loan;
import org.springframework.transaction.annotation.Transactional;

/**
 * Filename: LoanService.java Description: 借款serviceCopyright: Copyright (c)2013
 * Company: jdp2p
 *
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-4 下午3:36:30
 * <p/>
 * Modification History: Date Author Version Description
 * ------------------------------------------------------------------
 * 2014-1-4 wangzhi 1.0
 */
public interface LoanService {
    /**
     * 流标
     *
     * @param loanId     借款id
     * @param operatorId 操作用户id
     * @throws ExistWaitAffirmInvests
     */
    public void fail(String loanId, String operatorId)
            throws ExistWaitAffirmInvests;

    /**
     * 延长借款募集时间，即延长预计执行时间
     *
     * @param loanId        借款id
     * @param newExpectTime 新的预计执行时间
     * @throws InvalidExpectTimeException 预计执行时间设置错误
     */
    public void delayExpectTime(String loanId, Date newExpectTime)
            throws InvalidExpectTimeException;

    /**
     * 借款放款，即借款执行，转钱给借款者。
     *
     * @param loanId
     * @throws BorrowedMoneyTooLittle 募集到的资金太少，为0、或者不足以支付借款保证金
     */
    public void giveMoneyToBorrower(String loanId)
            throws BorrowedMoneyTooLittle;

    /**
     * 借款申请，通过审核
     *
     * @param loanId       借款id
     * @param auditInfo    审核信息
     * @param verifyUserId 审核人编号
     * @throws InvalidExpectTimeException 预计执行时间设置错误
     */
    public void passApply(Loan loan) throws InvalidExpectTimeException,
            InsufficientBalance;

    /**
     * 借款申请，未通过审核，即拒绝借款申请
     *
     * @param loanId       借款id
     * @param refuseInfo   审核信息
     * @param verifyUserId 审核人编号
     */
    public void refuseApply(String loanId, String refuseInfo,
                            String verifyUserId);

    /**
     * 判断借款是否已完成，即是否所有的还款都还了
     *
     * @param loanId 借款id
     * @return 是否已完成
     */
    public boolean isCompleted(String loanId);

    /**
     * 处理借款完成工作，即改借款状态、与之相关的投资状态等。
     *
     * @param loanId 借款id
     * @return
     */
    public void dealComplete(String loanId);

    /**
     * 检查借款是否募集完成
     *
     * @param loanId 借款编号
     * @return
     * @throws NoMatchingObjectsException 找不到loan
     */
    public boolean isRaiseCompleted(String loanId)
            throws NoMatchingObjectsException;

    /**
     * 处理借款募集完成。
     *
     * @param loanId 借款编号
     * @throws NoMatchingObjectsException 找不到loan
     */
    public void dealRaiseComplete(String loanId)
            throws NoMatchingObjectsException;

    /**
     * 检查借款是否到预计执行时间
     *
     * @param loanId 借款id
     */
    public boolean isOverExpectTime(String loanId);

    /**
     * 借款到预计执行时间，处理借款
     *
     * @param loanId 借款id
     */
    public void dealOverExpectTime(String loanId);

    /**
     * 申请借款
     *
     * @param loan
     * @throws InsufficientBalance 余额不足以支付保证金
     */
    public void applyLoan(Loan loan) throws InsufficientBalance;

    /**
     * 申请企业借款
     *
     * @param ael 企业借款对象
     */
    public void applyEnterpriseLoan(ApplyEnterpriseLoan ael);

    /**
     * 审核企业借款
     *
     * @param ael 企业借款对象
     */
    public void verifyEnterpriseLoan(ApplyEnterpriseLoan ael);

    /**
     * 更新loan
     *
     * @param loan
     */
    public void update(Loan loan);

    /**
     * 管理员添加借款
     *
     * @param loan
     * @throws InsufficientBalance
     * @throws InvalidExpectTimeException
     */
    public void createLoanByAdmin(Loan loan) throws InvalidExpectTimeException,
            InsufficientBalance;

    /**
     * 获取某笔借款里成功的投资
     *
     * @param loanId
     */
    public List<Invest> getSuccessfulInvests(String loanId);

    /**
     * 检查是否有等待第三方资金托管确认的投资
     * @param loanId
     * @throws ExistWaitAffirmInvests 存在等待第三方资金托管确认的投资，不能放款。
     */
    void changeInvestFromWaitAffirmToUnfinished(String loanId) throws ExistWaitAffirmInvests;

    @Transactional
    void notifyInvestorsLoanOutSuccessful(String loanId);
}
