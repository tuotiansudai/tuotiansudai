package com.esoft.archer.config.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.config.ConfigConstants;
import com.esoft.archer.config.model.Config;
import com.esoft.archer.config.model.ConfigType;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class ConfigHome extends EntityHome<Config> implements
		java.io.Serializable {
	@Logger
	static Log log;
	private final static StringManager sm = StringManager
			.getManager(ConfigConstants.Package);

	@Resource
	private LoginUserInfo loginUserInfo;

	public ConfigHome() {
		setUpdateView(FacesUtil.redirect(ConfigConstants.View.CONFIG_LIST));
		setDeleteView(FacesUtil.redirect(ConfigConstants.View.CONFIG_LIST));
	}

	public Config createInstance() {
		Config config = new Config();
		config.setConfigType(new ConfigType());
		return config;
	}

	@Transactional(readOnly = false)
	public String save() {
		if (getInstance().getConfigType() == null
				|| StringUtils.isEmpty(getInstance().getConfigType().getId())) {
			getInstance().setConfigType(null);
		}
		return super.save();
	}

	/**
	 * 通过配置编号得到配置的值
	 * 
	 * @param configId
	 * @return
	 */
	public String getConfigValue(String configId) {
		Config config = getBaseService().get(Config.class, configId);
		if (config != null) {
			return config.getValue();
		}
		return "";
	}

	/**
	 * 通过配置编号得到配置的(int型)值
	 * 
	 * @param configId
	 * @return 返回int型值,不存在则返回0
	 */
	public int getConfigIntValue(String configId) {
		Config config = getBaseService().get(Config.class, configId);
		/** 存在返回当前值 */
		if (config != null) {
			return Integer.parseInt(config.getValue());
		}
		/** 不存在则返回0 */
		return 0;
	}

	@Override
	@Transactional(readOnly = false)
	public String delete() {
		if (log.isInfoEnabled()) {
			log.info(sm.getString("log.info.deleteConfig",
					loginUserInfo.getLoginUserId(), new Date(), getId()));
		}
		return super.delete();
	}
}
