package com.esoft.archer.user.service.impl;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-13 下午3:08:49
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-13 wangzhi 1.0
 */
@Service("userBO")
public class UserBO {

	@Resource
	HibernateTemplate ht;

	public void save(User user) {
		validateField(user);
		user.setId(user.getUsername());
		ht.save(user);
	}

	/**
	 * 检查某些域值输入格式和是否已存在，例如：username、email、mobile
	 * 
	 * @param user
	 */
	private void validateField(User user) {
		if (StringUtils.isEmpty(user.getUsername())) {
			throw new IllegalArgumentException(
					"user.username can not be empty!");
		}
		if (!user
				.getUsername()
				.matches(
						"^[a-zA-Z0-9_]{5,25}$")) {
			throw new IllegalArgumentException(
					"user.username cannot be a mobile number and must contains by number or word!");
		}
		User userExist = getUserByUsername(user.getUsername());
		ht.evict(userExist);
		if (userExist != null && !userExist.getId().equals(user.getId())) {
			throw new DuplicateKeyException("user.username '"
					+ user.getUsername() + "' already exists!");
		}
		if (StringUtils.isNotEmpty(user.getEmail())) {
			User emailUser = getUserByEmail(user.getEmail());
			ht.evict(emailUser);
			if (emailUser != null && !emailUser.getId().equals(user.getId())) {
				throw new DuplicateKeyException("user.email '"
						+ user.getEmail() + "' already exists!");
			}
		}
		if (StringUtils.isNotEmpty(user.getMobileNumber())) {
			User mobileUser = getUserByMobileNumber(user.getMobileNumber());
			ht.evict(mobileUser);
			if (mobileUser != null && !mobileUser.getId().equals(user.getId())) {
				throw new DuplicateKeyException("user.mobileNumber '"
						+ user.getMobileNumber() + "' already exists!");
			}
		}
	}

	public void update(User user) {
		validateField(user);
		ht.update(user);
	}

	/**
	 * 使用新session从数据库获取用户的当前信息
	 * @param username
	 * @return
	 */
	public User getUserByUsernameFromDb(String username){
		Session session=null;
		try {
			session = ht.getSessionFactory().openSession();
			Query query = session.createQuery("from User user where user.username=:userName");
			query.setParameter("userName", username);
			List<User> users = query.list();
			if (users != null && users.size() > 0) {
				User u = users.get(0);
				// 因为 role 是懒加载，如果 session 关闭的话，此字段将查询不出来，为了后续还能够使用此字段，特此对该数据进行一次使用。
				List<Role> roles = u.getRoles();
				for(Role r : roles){
					r.setId(r.getId());
				}
				return u;
			} else {
				return null;
			}
		}finally {
			if(session!=null){
				session.close();
			}
		}
	}

	public User getUserByUsername(String username) {
		List<User> users = ht.find("from User user where user.username=?",
				username);
		if (users.size() == 0) {
			return null;
		} else if (users.size() > 1) {
			throw new DuplicateKeyException("duplicate user.username:"
					+ username);
		}
		return users.get(0);
	}
	public User getUserByUserNameOrMobileNumber(String userName){
		List<User> users = ht.find("from User user where user.username=? or user.mobileNumber=?", userName,userName);
		if (users.size() == 0) {
			return null;
		}
		return users.get(0);
	}
	public User getUserByEmail(String email) {
		List<User> users = ht.find("from User user where user.email=?", email);
		if (users.size() == 0) {
			return null;
		} else if (users.size() > 1) {
			throw new DuplicateKeyException("duplicate user.email:" + email);
		}
		return users.get(0);
	}

	/**
	 * 根据手机号，获得某个用户
	 * 
	 * @param mobileNumber
	 * @return
	 */

	public User getUserByMobileNumber(String mobileNumber) {
		List<User> users = ht.find("from User user where user.mobileNumber=?",
				mobileNumber);
		if (users.size() == 0) {
			return null;
		} else if (users.size() > 1) {
			throw new DuplicateKeyException("duplicate user.mobileNumber:"
					+ mobileNumber);
		}
		return users.get(0);
	}

	public void addRole(User user, Role role) {
		for (Role roleT : user.getRoles()) {
			if (roleT.getId().equals(role.getId())) {
				return;
			}
		}
		user.getRoles().add(role);
		ht.update(user);
	}

	public void removeRole(User user, Role role) {
		List<Role> roles = user.getRoles();
		for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
			Role role2 = (Role) iterator.next();
			if (role2.getId().equals(role.getId())) {
				iterator.remove();
			}
		}
		ht.update(user);
	}

	public void enableUser(User user) {
		user.setStatus(UserConstants.UserStatus.ENABLE);
		ht.update(user);
	}

}
