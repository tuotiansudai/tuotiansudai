package com.esoft.jdp2p.message.controller;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.model.UserMessageWay;

@Component
@Scope(ScopeType.VIEW)
public class UserMessageWayList extends EntityQuery<UserMessageWay>{
	
	@Resource
	private HibernateTemplate ht ;
	
	@Logger 
	private static Log log ;
	
	
	@Transactional(readOnly=false)
	public void changeStatus(UserMessageWay umw){
		if (umw.getStatus().equals(MessageConstants.UserMessageNodeStatus.OPEN)) {
			umw.setStatus(MessageConstants.UserMessageNodeStatus.CLOSED);
		} else {
			umw.setStatus(MessageConstants.UserMessageNodeStatus.OPEN);
		} 
		getHt().update(umw);
	}
}
