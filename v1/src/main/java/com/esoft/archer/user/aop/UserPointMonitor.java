package com.esoft.archer.user.aop;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.config.ConfigConstants;
import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.user.UserConstants.UserPointType;
import com.esoft.archer.user.service.UserPointService;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.invest.InvestConstants.InvestStatus;
import com.esoft.jdp2p.invest.model.Invest;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 积分监听器，给用户加积分
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-2-7 下午4:58:10
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-2-7 wangzhi 1.0
 */
@Component
@Aspect
public class UserPointMonitor {

	@Resource
	HibernateTemplate ht;

	@Resource
	UserPointService userPointService;

	@Resource
	ConfigService configService;

	/**
	 * 项目正常放款后，给成功投资者送积分 一块钱对应一积分
	 * 
	 * @param user
	 * @param role
	 */
	@AfterReturning(argNames = "loanId", value = "execution(public void com.esoft.jdp2p.loan.service.LoanService.giveMoneyToBorrower(..)) && args(loanId)")
	public void addPoint(String loanId) {
		String investRate = configService
				.getConfigValue(ConfigConstants.UserPointMonitor.INVEST);
		if (StringUtils.isNotEmpty(investRate)
				&& !"0".equals(investRate.trim())) {
			String hql = "from Invest i where i.loan.id=? and i.status=?";
			List<Invest> is = ht.find(hql, new String[] { loanId,
					InvestStatus.REPAYING });
			for (Invest invest : is) {
				userPointService.add(
						invest.getUser().getId(),
						Double.valueOf(
								invest.getInvestMoney().intValue()
										* Double.parseDouble(investRate))
								.intValue(), UserPointType.COST, "投标成功，赠送积分",
						"投标编号：" + invest.getId());
			}
		}
	}

}
