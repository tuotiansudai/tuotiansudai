package com.esoft.archer.testcase;

import java.util.List;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.invest.InvestConstants.InvestStatus;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.LoanConstants.LoanStatus;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.model.Repay;

/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-3-14 下午8:12:57  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-3-14      wangzhi      1.0          
 */
@Service("testService")
public class TestService {
	@Resource
	private HibernateTemplate ht;
	
	@Logger
	static Log log;
	
	@Resource
	private TestService2 testService2;
	
	
	@Transactional(readOnly = false)
	public void test(){
		Loan loan = ht.get(Loan.class, "20140922000021");
		System.out.println(loan.getStatus());
		for (Invest invest : loan.getInvests()) {
			invest.setStatus(InvestStatus.BID_SUCCESS);
			ht.update(invest);
		}
		loan.setStatus(LoanStatus.RECHECK);
		ht.update(loan);
		loan = ht.get(Loan.class, "20140922000021");
		System.out.println(loan.getStatus());
		List<Invest> invests = loan.getInvests();
		loan.setStatus(LoanConstants.LoanStatus.REPAYING);
		for (Invest invest : invests) {
			System.out.println(invest.getStatus());
		}
		System.out.println(loan.getStatus());
		ht.update(loan);
	}
	
	public void test2() {
		test();
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void test3(){
		User user = ht.get(User.class, "123abc");
		user.setEmail("aaw@eee.com");
		ht.update(user);
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation=Propagation.REQUIRES_NEW)
	public void test4(){
		User user = ht.get(User.class, "111qqq");
		user.setEmail("aaw@eee.com");
		ht.update(user);
		throw new RuntimeException();
	}
}
