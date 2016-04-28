package com.esoft.archer.message.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.common.service.BaseService;
import com.esoft.archer.message.model.InBox;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.SpringBeanUtil;
import com.esoft.jdp2p.message.MessageConstants;

@Component
@Scope(ScopeType.VIEW)
@SuppressWarnings("unchecked")
public class InBoxHome extends EntityHome<InBox> implements java.io.Serializable{

	private String selectItems;
	@Resource
	private HibernateTemplate ht;
	@Resource
	private LoginUserInfo loginUser;
	@Logger
	private static Log log;

	/**
	 * 获取某个用户的未读的信息的数量
	 * 
	 * @author liuchun
	 * @param userId
	 * @return
	 */
	public long getAllInBoxNoReadByUserId(String userId) {
		String hql = "select count(ib) from InBox ib where ib.status = ? and ib.recevier.id=?";
		Object o = ht
				.find(hql,
						new String[] { MessageConstants.InBoxConstants.NOREAD,
								userId }).get(0);
		if (o == null) {
			return 0;
		}
		return (Long) o;
	}

	/**
	 * 通过用户的id获取所有的未读信息
	 * 
	 * @return
	 */
	public List<InBox> getAllInBoxNoReadByLoginUser() {
		if (StringUtils.isEmpty(loginUser.getLoginUserId())) {
			return ht.findByNamedQuery("InBox.finInBoxNoReadByLoginUser",
					loginUser.getLoginUserId());
		}
		return null;
	}

	public List<InBox> getAllInBoxByLoginUser() {
		if (StringUtils.isEmpty(loginUser.getLoginUserId())) {
			return ht.findByNamedQuery("InBox.finInBoxByLoginUser",
					loginUser.getLoginUserId());
		}
		return null;
	}

	/**
	 * 读信
	 * 
	 * @param inBoxId
	 */
	@Transactional
	public void setLetterReaded(String inBoxId) {
		InBox ib = getBaseService().get(InBox.class, inBoxId);
		ib.setStatus(MessageConstants.InBoxConstants.ISREAD);
		getBaseService().update(ib);
		
		RequestContext context = RequestContext.getCurrentInstance();
		// 即时更新前台未读信息数量
		context.addCallbackParam(
				"noread",
				ht.findByNamedQuery("InBox.finInBoxNoReadByLoginUser",
						loginUser.getLoginUserId()).size());

	}

	/**
	 * 标记已读
	 */
	public void setLetterReaded() {
		if (StringUtils.isNotBlank(selectItems)) {
			String[] inboxIds = selectItems.split(",");
			for (String inboxId : inboxIds) {
				ht.bulkUpdate("update InBox set status = ? where id = ?",
						MessageConstants.InBoxConstants.ISREAD, inboxId);
			}
		}
	}

	/**
	 * 批量删除
	 */
	@Transactional(readOnly = false)
	public void deleteSelectInBox() {
		if (StringUtils.isNotBlank(selectItems)) {
			String[] inboxIds = selectItems.split(",");
			for (String inboxId : inboxIds) {
				InBox inbox = ht.get(InBox.class, inboxId);
				if (inbox != null) {
					getBaseService().delete(inbox);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public String delete() {
		String inBoxId = FacesUtil.getParameter("inBoxId");
		InBox inBox = ht.get(InBox.class, inBoxId);
		if (inBox != null) {
			ht.delete(inBox);
			FacesUtil.addInfoMessage("信息删除成功！");
		}
		return null;
	}

	/**
	 * 根据inBox id删除inBox
	 * 
	 * @param inBoxId
	 * @return
	 */
	@Transactional(readOnly = false)
	public String delete(String inBoxId) {
		InBox inBox = ht.get(InBox.class, inBoxId);
		if (inBox != null) {
			ht.delete(inBox);
			FacesUtil.addInfoMessage("信息删除成功！");
		}
		return null;
	}

	public String getSelectItems() {
		return selectItems;
	}

	public void setSelectItems(String selectItems) {
		this.selectItems = selectItems;
	}
}
