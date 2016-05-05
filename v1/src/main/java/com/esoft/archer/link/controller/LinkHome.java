package com.esoft.archer.link.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.link.LinkConstants;
import com.esoft.archer.link.model.Link;
import com.esoft.archer.link.model.LinkType;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.ImageUploadUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class LinkHome extends EntityHome<Link> implements Serializable {
	private static final long serialVersionUID = -5661050223988953680L;
	@Logger
	static Log log;
	@Resource
	LoginUserInfo loginUserInfo;
	private final static StringManager sm = StringManager
			.getManager(LinkConstants.Package);

	public LinkHome() {
		setUpdateView(FacesUtil.redirect(LinkConstants.View.LINK_LIST));
		setDeleteView(FacesUtil.redirect(LinkConstants.View.LINK_LIST));
	}
	
	@Override
	@Transactional(readOnly=false)
	public String delete() {
		if (log.isInfoEnabled()) {
			log.info(sm.getString("log.info.deleteLink",
					loginUserInfo.getLoginUserId(), new Date(), getId()));
		}
		return super.delete();
	}

	@Override
	protected Link createInstance() {
		Link link = new Link();
		link.setType(new LinkType());
		link.setUrl("http://");
		link.setSeqNum(0);
		return link;
	}

}
