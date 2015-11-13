package com.esoft.archer.user.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.esoft.archer.user.service.UserService;
import com.esoft.jdp2p.message.service.SendCloudMailService;
import org.apache.commons.logging.Log;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.message.MessageConstants;
import com.esoft.archer.message.model.InBox;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.StringManager;
import com.esoft.jdp2p.message.exception.MailSendErrorException;
import com.esoft.jdp2p.message.service.MailService;

@Component
@Scope(ScopeType.VIEW)
public class UserList extends EntityQuery<User> {

	private static StringManager sm = StringManager
			.getManager(UserConstants.Package);

	@Logger
	private static Log log;

	@Resource
	MailService mailService;

	@Resource
	UserService userService;

	@Resource
	SendCloudMailService sendCloudMailService;

	private List<User> hasLoanRoleUsers;

	private Date registerTimeStart;
	private Date registerTimeEnd;

	public UserList() {
		final String[] RESTRICTIONS = { "user.id like #{userList.example.id}",
				"user.username like #{userList.example.username}",
				"user.mobileNumber like #{userList.example.mobileNumber}",
				"user.status like #{userList.example.status}",
				"user.email like #{userList.example.email}",
				"user.source = #{userList.example.source}",
				"user.referrer like #{userList.example.referrer}",
				"user.registerTime >= #{userList.registerTimeStart}",
				"user.registerTime <= #{userList.registerTimeEnd}",
				"#{userList.example.roles[0].id} in elements(user.roles)",
				"user.channel = #{userList.example.channel}"
		};
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	private List<User> selectedUsers;
	private List<String> allChannelList;

	private String title;
	private String message;
	private String emailTitle;
	private String emailContent;

	@Override
	protected void initExample() {
		User user = new User();
		List<Role> roles = new ArrayList<Role>();
		roles.add(new Role());
		user.setRoles(roles);
		setExample(user);
		allChannelList = userService.getAllChannelName();
	}

	@Override
	public User getLazyModelRowData(String rowKey) {
		List<User> users = (List<User>) getLazyModel().getWrappedData();
		for (User user : users) {
			if (user.getId().equals(rowKey)) {
				return user;
			}
		}
		return null;
	}

	@Override
	public Object getLazyModelRowKey(User user) {
		return user.getId();
	}

	/**
	 * 给被选中的用户发站内信。
	 */
	@Transactional(readOnly = false)
	public void sendMessageToSelectedUsers() {
		if (getSelectedUsers().size() == 0) {
			FacesUtil.addErrorMessage("请选择用户！");
		} else {
			for (User user : getSelectedUsers()) {
				InBox inbox = new InBox();
				inbox.setId(IdGenerator.randomUUID());
				inbox.setRecevier(user);
				inbox.setSender(new User("admin"));
				inbox.setReceiveTime(new Date());
				inbox.setContent(message);
				inbox.setStatus(MessageConstants.InBoxConstants.NOREAD);
				inbox.setTitle(title);
				getHt().save(inbox);
			}
			RequestContext.getCurrentInstance().addCallbackParam("sendSuccess",
					true);
			FacesUtil.addInfoMessage("发送成功！");
			getSelectedUsers().clear();
			this.title = null;
			this.message = null;
		}
	}
	
	/**
	 * 给被选中的用户发邮件。
	 */
	@Transactional(readOnly = false)
	public void sendEmailToSelectedUsers() {
		log.debug("sendEmailToSelectedUsers start !");
		//给图片路径添加域名
		emailContent=emailContent.replace("/ps2/upload/", FacesUtil.getCurrentAppUrl()+"/upload/");
		if (getSelectedUsers().size() == 0) {
			FacesUtil.addErrorMessage("请选择用户！");
		} else {
			boolean flag  =  false;
			//如果用户邮箱为空，略过
			for (User user : getSelectedUsers()) {
				if (user.getEmail() != null && !"".equals(user.getEmail())) {
					try {
						sendCloudMailService.sendMail(user.getEmail(), emailTitle, emailContent);
						flag = true;
					} catch (MailSendErrorException e) {
						log.debug(user.getUsername()+"的邮箱"+user.getEmail()+"不存在！");
						FacesUtil.addInfoMessage(user.getUsername()+"的邮箱"+user.getEmail()+"不存在！");
					}	
				}else {
					log.debug(user.getUsername()+"用户邮箱为空！");
					FacesUtil.addInfoMessage(user.getUsername()+"用户邮箱为空！");
				}
			}
			if (flag) {
				RequestContext.getCurrentInstance().addCallbackParam("sendSuccess", true);
				FacesUtil.addInfoMessage("邮件发送成功！");
				getSelectedUsers().clear();
				this.emailTitle = null;
				this.emailContent = null;
			}
			
		}
	}

	/**
	 * 获取新注册的用户，按照注册时间倒序排列
	 * 
	 * @param count
	 *            新注册用户个数
	 * @return
	 */
	public List<User> getNewUsers(int count) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.addOrder(Order.desc("registerTime"));
		getHt().setCacheQueries(true);
		return getHt().findByCriteria(criteria, 0, count);
	}

	/**
	 * 获取有借款权限的用户
	 */
	@SuppressWarnings({ "unchecked" })
	public List<User> allHasLoanRoleUser() {
		List<User> users = getHt().find("from User");
		List<User> hasLoanRoleUser = new ArrayList<User>();
		for (User user : users) {
			for (Role role : user.getRoles()) {
				if (role.getId().equals("LOANER")
						|| role.getId().equals("ADMINISTRATOR")) {
					hasLoanRoleUser.add(user);
				}
			}
		}
		return hasLoanRoleUser;
	}

	/**
	 * 获取网站有效注册人数
	 * 
	 * @return
	 */
	// FIXME :放到用户统计类中
	public long getValidUserNumber() {
		return (Long) getHt().find(
				"select count(user) from User user where user.status='1'").get(
				0);
	}

	public List<User> getHasLoanRoleUsers() {
		if (this.hasLoanRoleUsers == null) {
			this.hasLoanRoleUsers = allHasLoanRoleUser();
		}
		return this.hasLoanRoleUsers;
	}

	/**
	 * @author wanghm 根据用户名模糊查询符合条件的用户，最多返回50条记录
	 */
	@SuppressWarnings("unchecked")
	public List<User> queryUsersByUserName(String username) {
		if (log.isDebugEnabled())
			log.debug("模糊查询用户名，传入的值为：" + username);
		User example = new User();
		example.setUsername("%" + username + "%");
		DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass());
		criteria.add(Restrictions.like("username", "%" + username + "%"));
		return getHt().findByCriteria(criteria, 0, 50);
	}

	public void setHasLoanRoleUsers(List<User> hasLoanRoleUsers) {
		this.hasLoanRoleUsers = hasLoanRoleUsers;
	}

	public List<User> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getRegisterTimeStart() {
		return registerTimeStart;
	}

	public void setRegisterTimeStart(Date registerTimeStart) {
		this.registerTimeStart = registerTimeStart;
	}

	public Date getRegisterTimeEnd() {
		return registerTimeEnd;
	}

	public void setRegisterTimeEnd(Date registerTimeEnd) {
		this.registerTimeEnd = registerTimeEnd;
	}

	public String getEmailTitle() {
		return emailTitle;
	}

	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public List<String> getAllChannelList() {
		return allChannelList;
	}

	public void setAllChannelList(List<String> allChannelList) {
		this.allChannelList = allChannelList;
	}
}
