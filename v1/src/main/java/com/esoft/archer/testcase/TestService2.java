package com.esoft.archer.testcase;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.loan.LoanConstants;
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
@Service("testService2")
public class TestService2 {
	@Resource
	private HibernateTemplate ht;
	
	@Logger
	static Log log;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void test(){
		User user = ht.get(User.class, "111qqq");
		user.setEmail("aa@adda.com");
		ht.update(user);
	}
	
	public void test2() {
		test();
	}
}
