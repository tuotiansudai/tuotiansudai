package com.esoft.jdp2p.message.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.message.model.UserMessage;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.esoft.jdp2p.message.service.MessageService;

@Component
@Scope(ScopeType.VIEW)
public class UserMessageHome extends EntityHome<UserMessage> implements
		java.io.Serializable {

	@Logger
	private static Log log;

	@Resource
	private LoginUserInfo loginUser;
	
	@Resource
	private MessageService messageService;

	private List<UserMessageTemplate> umts;

	public String saveUserMessages() {
		messageService.saveUserMessages(loginUser.getLoginUserId(), umts);
		FacesUtil.addInfoMessage("设置成功！");
		return "pretty:userMessageTypeSet";
	}

	public void initUmts(String userId) {
		this.umts = messageService.getUserMessagesTemplatesByUserId(userId);
	}

	public List<UserMessageTemplate> getUmts() {
		return umts;
	}

	public void setUmts(List<UserMessageTemplate> umts) {
		this.umts = umts;
	}

}
