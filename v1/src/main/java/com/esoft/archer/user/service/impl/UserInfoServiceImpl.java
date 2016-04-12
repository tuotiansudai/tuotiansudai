package com.esoft.archer.user.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.common.service.ValidationService;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserInfoService;

/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-1-9 下午7:13:18  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-9      wangzhi      1.0          
 */
@Service
public class UserInfoServiceImpl implements UserInfoService{

	@Resource
	HibernateTemplate ht;
	
	@Resource
	ValidationService validationSrv;
	
	@Override
	public boolean isUsernameExist(String username) {
		List<User> users = ht.find("from User user where user.username=?",username);
		if (users.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isEmailExist(String email) {
		List<User> users = ht.find("from User user where user.email=?",email);
		if (users.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isMobileNumberExist(String mobileNumber) {
		List<User> users = ht.find("from User user where user.mobileNumber=?",mobileNumber);
		if (users.size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public void resetCashPassword(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modifyCashPassWord(String userId, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean verifyOldPassword(String userId, String oldPassword) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVip(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void modifyEmail(String userId, String newEmail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List getUserMessage(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveMessageSet(String uid, List list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSendMessageToUser(String uid, String nodeId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String userRiskRankCalculation(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

}
