package com.esoft.archer.node.controller;

import java.util.Date;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.node.NodeConstants;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.node.model.NodeAttr;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class NodeAttrHome extends EntityHome<NodeAttr> {

	@Logger
	static Log log;
	private static final StringManager sm = StringManager
			.getManager(NodeConstants.Package);

	public NodeAttrHome() {
		setUpdateView(FacesUtil.redirect(NodeConstants.View.NODE_ATTR_LIST));
	}

	@Override
	@Transactional(readOnly=false)
	public String delete() {
		if (log.isInfoEnabled()) {
			log.info(sm.getString("log.info.deleteNodeAttr",
					FacesUtil
					.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
		}
		Set<Node> nodeSets = getInstance().getNodes();
		if (nodeSets != null && nodeSets.size() > 0) {
			FacesUtil.addWarnMessage(sm.getString("canNotDeleteNodeAttr"));
			if (log.isInfoEnabled()) {
				log.info(sm.getString("log.info.deleteNodeAttrUnsuccessful",
						FacesUtil
						.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
			}
			return null;
		} else {
			return super.delete();
		}
	}
}
