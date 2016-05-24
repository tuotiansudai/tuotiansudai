package com.esoft.jdp2p.message.controller;

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
import com.esoft.jdp2p.message.model.UserMessageWay;

@Component
@Scope(ScopeType.VIEW)
public class UserMessageNodeList extends EntityQuery<UserMessageNode>{
	
	@Resource
	private HibernateTemplate ht ;
	
	@Logger 
	private static Log log ;
	
}
