package com.esoft.jdp2p.borrower.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.system.service.SpringSecurityService;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.impl.UserBO;
import com.esoft.jdp2p.borrower.BorrowerConstant;
import com.esoft.jdp2p.borrower.model.BorrowerAdditionalInfo;
import com.esoft.jdp2p.borrower.model.BorrowerAuthentication;
import com.esoft.jdp2p.borrower.model.BorrowerInfo;
import com.esoft.jdp2p.borrower.model.BorrowerPersonalInfo;
import com.esoft.jdp2p.borrower.service.BorrowerService;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-14 上午10:21:44
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-14 wangzhi 1.0
 */
@Service("borrowService")
public class BorrowerServiceImpl implements BorrowerService {

	@Resource
	private HibernateTemplate ht;

	@Resource
	private UserBO userBO;

	@Resource
	private SpringSecurityService springSecurityService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esoft.jdp2p.borrower.service.impl.BorrowerService#
	 * initBorrowerPersonalInfo(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public BorrowerPersonalInfo initBorrowerPersonalInfo(String userId)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("userId:" + userId);
		}
		BorrowerPersonalInfo bpi = ht.get(BorrowerPersonalInfo.class, userId);
		if (bpi == null) {
			bpi = new BorrowerPersonalInfo();
			bpi.setVerified(BorrowerConstant.Verify.unverified);
		}
		if (bpi.getBorrowerInfo() == null) {
			bpi.setBorrowerInfo(getBorrowerInfo(user));
		}
		return bpi;
	}

	/**
	 * 查询borrowerInfo，如果没有，则创建
	 * 
	 * @param userId
	 * @return
	 */
	private BorrowerInfo getBorrowerInfo(User user) {
		BorrowerInfo bi = ht.get(BorrowerInfo.class, user.getId());
		if (bi == null) {
			bi = new BorrowerInfo(user);
			ht.save(bi);
		}
		return bi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esoft.jdp2p.borrower.service.impl.BorrowerService#
	 * saveOrUpdateBorrowerPersonalInfo
	 * (com.esoft.jdp2p.borrower.model.BorrowerPersonalInfo)
	 */
	/**
	 * 保存BorrowerPersonalInfo，借款人普通信息
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void saveOrUpdateBorrowerPersonalInfo(BorrowerPersonalInfo bpi) {
		// TODO：验证，抛异常
		ht.saveOrUpdate(bpi);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esoft.jdp2p.borrower.service.impl.BorrowerService#
	 * verifyBorrowerPersonalInfo(java.lang.String, boolean, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void verifyBorrowerPersonalInfo(String bpiId, boolean isPassed,
			String msg, String verifiedUserId) {
		// TODO:验证
		BorrowerPersonalInfo bpi = ht.get(BorrowerPersonalInfo.class, bpiId);
		if (bpi == null) {
			// 抛异常
		}
		bpi.setIsPassedVerify(isPassed);
		bpi.setVerifiedTime(new Date());
		bpi.setVerifiedUser(new User(verifiedUserId));
		bpi.setVerifiedMessage(msg);
		ht.update(bpi);

		grantBorrowerRole(bpi.getUserId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esoft.jdp2p.borrower.service.impl.BorrowerService#
	 * initBorrowerAdditionalInfo(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public BorrowerAdditionalInfo initBorrowerAdditionalInfo(String userId)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("userId:" + userId);
		}
		BorrowerAdditionalInfo bai = ht.get(BorrowerAdditionalInfo.class,
				userId);
		if (bai == null) {
			bai = new BorrowerAdditionalInfo();
			// 初始化为工薪阶层，以便前台验证用。
			bai.setOccupation("工薪阶层");
			bai.setVerified(BorrowerConstant.Verify.unverified);
		}
		if (bai.getBorrowerInfo() == null) {
			bai.setBorrowerInfo(getBorrowerInfo(user));
		}
		return bai;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esoft.jdp2p.borrower.service.impl.BorrowerService#
	 * saveOrUpdateBorrowerAdditionalInfo
	 * (com.esoft.jdp2p.borrower.model.BorrowerAdditionalInfo)
	 */
	/**
	 * 保存BorrowerAdditionalInfo，借款人工作财务信息
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void saveOrUpdateBorrowerAdditionalInfo(BorrowerAdditionalInfo bai) {
		// TODO：验证，抛异常
		ht.saveOrUpdate(bai);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esoft.jdp2p.borrower.service.impl.BorrowerService#
	 * verifyBorrowerAdditionalInfo(java.lang.String, boolean, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void verifyBorrowerAdditionalInfo(String baiId, boolean isPassed,
			String msg, String verifiedUserId) {
		// TODO:验证
		BorrowerAdditionalInfo bai = ht
				.get(BorrowerAdditionalInfo.class, baiId);
		if (bai == null) {
			// 抛异常
		}
		bai.setIsPassedVerify(isPassed);
		bai.setVerifiedTime(new Date());
		bai.setVerifiedUser(new User(verifiedUserId));
		bai.setVerifiedMessage(msg);
		ht.update(bai);

		grantBorrowerRole(bai.getUserId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esoft.jdp2p.borrower.service.impl.BorrowerService#
	 * initBorrowerAuthentication(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public BorrowerAuthentication initBorrowerAuthentication(String userId)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("userId:" + userId);
		}
		BorrowerAuthentication ba = ht
				.get(BorrowerAuthentication.class, userId);
		if (ba == null) {
			ba = new BorrowerAuthentication();
			ba.setVerified(BorrowerConstant.Verify.unverified);
		}
		if (ba.getBorrowerInfo() == null) {
			ba.setBorrowerInfo(getBorrowerInfo(user));
		}
		return ba;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esoft.jdp2p.borrower.service.impl.BorrowerService#
	 * saveOrUpdateBorrowerAuthentication
	 * (com.esoft.jdp2p.borrower.model.BorrowerAuthentication)
	 */
	/**
	 * 保存BorrowerAuthentication，借款人材料信息
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void saveOrUpdateBorrowerAuthentication(BorrowerAuthentication ba) {
		// TODO：验证，抛异常
		ht.saveOrUpdate(ba);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esoft.jdp2p.borrower.service.impl.BorrowerService#
	 * verifyBorrowerAuthentication(java.lang.String, boolean, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor=Exception.class)
	public void verifyBorrowerAuthentication(String baId, boolean isPassed,
			String msg, String verifiedUserId) {
		// TODO:验证
		BorrowerAuthentication ba = ht.get(BorrowerAuthentication.class, baId);
		if (ba == null) {
			// 抛异常
		}
		ba.setIsPassedVerify(isPassed);
		ba.setVerifiedTime(new Date());
		ba.setVerifiedUser(new User(verifiedUserId));
		ba.setVerifiedMessage(msg);
		ht.update(ba);

		grantBorrowerRole(ba.getUserId());
	}

	@Transactional(readOnly = false, rollbackFor=Exception.class)
	private void grantBorrowerRole(String biId) {
		BorrowerInfo bi = ht.get(BorrowerInfo.class, biId);

		Boolean addition = bi.getBorrowerAdditionalInfo().getIsPassedVerify();
		Boolean authentication = bi.getBorrowerAuthentication()
				.getIsPassedVerify();
		Boolean personal = bi.getBorrowerPersonalInfo().getIsPassedVerify();

		User user = bi.getUser();

		if (addition != null && authentication != null && personal != null
				&& addition && authentication && personal) {
			// 添加投资权限
			userBO.addRole(user, new Role("LOANER"));
		} else {
			// 如果有一个未通过的,删除用户权限
			userBO.removeRole(user, new Role("LOANER"));
		}
		user = ht.merge(user);
		springSecurityService.refreshLoginUserAuthorities(user.getId());
	}

}
