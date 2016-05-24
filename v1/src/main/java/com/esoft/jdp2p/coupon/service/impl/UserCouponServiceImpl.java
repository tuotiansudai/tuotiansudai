package com.esoft.jdp2p.coupon.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.core.PrioritizedParameterNameDiscoverer;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.model.User;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.coupon.CouponConstants;
import com.esoft.jdp2p.coupon.model.Coupon;
import com.esoft.jdp2p.coupon.model.UserCoupon;
import com.esoft.jdp2p.coupon.service.UserCouponService;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-7-19 下午2:01:16
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-7-19 wangzhi 1.0
 */
@Service
public class UserCouponServiceImpl implements UserCouponService {

	@Resource
	private HibernateTemplate ht;

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void giveUserCoupon(String userId, String couponId,
			Integer periodOfValidity, String description) {
		Coupon c = ht.get(Coupon.class, couponId);
		User u = ht.get(User.class, userId);
		UserCoupon uc = new UserCoupon();
		uc.setCoupon(c);
		uc.setDeadline(DateUtil.addSecond(new Date(),
				periodOfValidity == null ? c.getPeriodOfValidity()
						: periodOfValidity));
		uc.setDescription(description);
		uc.setGenerateTime(new Date());
		uc.setId(IdGenerator.randomUUID());
		uc.setStatus(CouponConstants.UserCouponStatus.UNUSED);
		uc.setUser(u);
		ht.save(uc);
	}

	/**
	 * 使用代金券
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void userUserCoupon(String userCouponId) {
		UserCoupon uc = ht.get(UserCoupon.class, userCouponId);
		uc.setStatus(CouponConstants.UserCouponStatus.USED);
		uc.setUsedTime(new Date());
		ht.update(uc);
	}

	@Override
	public void disable(String userCouponId) {
		//TODO:implement
		throw new RuntimeException("you must override this method!");
	}

	@Override
	public void exceedTimeLimit(String userCouponId) {
		//TODO:implement
		throw new RuntimeException("you must override this method!");
	}

}
