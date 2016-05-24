package com.esoft.jdp2p.loan.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.loan.model.Loan;


/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-1-21 下午3:59:31  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-21      wangzhi      1.0          
 */
@Service("loanBO")
public class LoanBO {
	
	@Resource
	HibernateTemplate ht;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String generateId() {
		String gid = DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD);
		String hql = "select loan from Loan loan where loan.id = (select max(loanM.id) from Loan loanM)";
		List<Loan> contractList = ht.find(hql);
		Integer itemp = 0;
		if (contractList.size() == 1) {
			Loan loan = contractList.get(0);
			ht.lock(loan, LockMode.UPGRADE);
			String temp = loan.getId();
			temp = temp.substring(temp.length() - 6);
			itemp = Integer.valueOf(temp);
		}
		itemp++;
		gid += String.format("%06d", itemp);
		return gid;
	}
	
}
