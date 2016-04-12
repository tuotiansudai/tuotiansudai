package com.esoft.umpay.user.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.esoft.archer.user.controller.UserHome;
import com.esoft.archer.user.model.User;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.HashCrypt;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.user.service.impl.UmPayUserOperation;
import org.springframework.orm.hibernate3.HibernateTemplate;

@SuppressWarnings("serial")
public class UmPayUserHome extends UserHome {
	@Resource
	HibernateTemplate ht;

	@Resource
	UmPayUserOperation umPayUserOperation;

	@Override
	public String getInvestorPermission() {
		String idCard = getInstance().getIdCard();
		List<User> users = ht.find(" from User user where user.idCard = ? ",idCard);
		if(CollectionUtils.isNotEmpty(users)){
			FacesUtil.addErrorMessage("身份证号：" + idCard + "已经存在");
			return  null;
		}

		if (StringUtils.equals(
				HashCrypt.getDigestHash(getInstance().getCashPassword()),
				getInstance().getPassword())) {
			FacesUtil.addErrorMessage("交易密码不能与登录密码相同");
			return null;
		}
		try {
			umPayUserOperation.createOperation(this.getInstance(),
					FacesContext.getCurrentInstance());
		} catch (IOException e) {
			FacesUtil.addErrorMessage("开户失败!");
			e.printStackTrace();
			return null;
		} catch (UmPayOperationException e) {
			FacesUtil.addErrorMessage("开户失败:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		FacesUtil.addInfoMessage("开户成功!");
		return "pretty:userCenter";
	}

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}
}
