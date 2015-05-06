package com.esoft.archer.config.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.config.ConfigConstants;
import com.esoft.archer.config.model.Config;
import com.esoft.archer.config.model.ConfigType;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class ConfigTypeHome extends EntityHome<ConfigType> implements
		java.io.Serializable {
	@Logger
	static Log log;
	private static final StringManager sm = StringManager
			.getManager(ConfigConstants.Package);

	public ConfigTypeHome() {
		setUpdateView(FacesUtil.redirect(ConfigConstants.View.CONFIG_TYPE_LIST));
		setDeleteView(FacesUtil.redirect(ConfigConstants.View.CONFIG_TYPE_LIST));
	}

	private List<Config> configs = new ArrayList<Config>();

	@PostConstruct
	public void init() {
		if (FacesUtil.getParameter("id") != null) {
			String id = FacesUtil.getParameter("id");
			super.setId(id);
			configs = new ArrayList<Config>();
			configs = getInstance().getConfigs();
		}
	}

	public List<Config> getConfigs() {
		return configs;
	}

	public void setConfigs(List<Config> configs) {
		this.configs = configs;
	}

	@Transactional(readOnly = false)
	public String configDetail() {
		String id = FacesUtil.getParameter("typeId");
		super.setId(id);
		getInstance().setConfigs(configs);
		super.save();
		return null ;
	}

	@Override
	@Transactional(readOnly = false)
	public String delete() {
		if (log.isInfoEnabled()) {
			log.info(sm.getString("log.info.deleteConfigType", FacesUtil
					.getExpressionValue("#{loginUserInfo.loginUserId}"),
					new Date(), getId()));
		}
		return super.delete();
	}

	@Override
	@Transactional(readOnly = false)
	public String save() {
		return super.save();
	}
}
