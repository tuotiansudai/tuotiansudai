package com.esoft.jdp2p.message.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.model.UserMessageNode;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.esoft.jdp2p.message.model.UserMessageWay;

@Component
@Scope(ScopeType.VIEW)
public class UserMessageTemplateList extends EntityQuery<UserMessageTemplate> {

	public UserMessageTemplateList() {
		final String[] RESTRICTIONS = {
				"id like #{userMessageTemplateList.example.id}",
				"userMessageWay.name like #{userMessageTemplateList.example.userMessageWay.name}",
				"userMessageNode.name like #{userMessageTemplateList.example.userMessageNode.name}",
				"name like #{userMessageTemplateList.example.name}",
				"description like #{userMessageTemplateList.example.description}" };
		ArrayList<String> a = new ArrayList(Arrays.asList(RESTRICTIONS));
		setRestrictionExpressionStrings(a);
	}

	/**
	 * 所有用户可设置的userMessageTempalte
	 */
	private List<UserMessageTemplate> allSetableUmts;

	@Resource
	private HibernateTemplate ht;

	@Logger
	private static Log log;

	@Override
	protected void initExample() {
		UserMessageTemplate umt = new UserMessageTemplate();
		umt.setUserMessageNode(new UserMessageNode());
		umt.setUserMessageWay(new UserMessageWay());
		this.setExample(umt);
	}

	public List<UserMessageTemplate> getAllSetableUmts() {
		if (this.allSetableUmts == null) {
			this.allSetableUmts = getHt().find(
					"from UserMessageTemplate umt where umt.status='"
							+ MessageConstants.UserMessageTemplateStatus.OPEN
							+ "'");
		}
		return allSetableUmts;
	}

	public void setAllSetableUmts(List<UserMessageTemplate> allSetableUmts) {
		this.allSetableUmts = allSetableUmts;
	}

}
