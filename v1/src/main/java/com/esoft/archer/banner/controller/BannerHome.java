package com.esoft.archer.banner.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.banner.BannerConstants;
import com.esoft.archer.banner.model.Banner;
import com.esoft.archer.banner.model.BannerPicture;
import com.esoft.archer.common.controller.EntityHome;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class BannerHome extends EntityHome<Banner> {
	private static final long serialVersionUID = 2373410201504708160L;
	@Logger
	static Log log;
	private static final StringManager sm = StringManager
			.getManager(BannerConstants.Package);

	public BannerHome() {
		this.setUpdateView(FacesUtil.redirect(BannerConstants.View.BANNER_LIST));
	}

	@Override
	public Banner createInstance() {
		Banner banner = new Banner();
		banner.setPictures(new ArrayList<BannerPicture>());
		return banner;
	}

	@Override
	@Transactional(readOnly=false)
	public String save() {
		if (this.getInstance().getId() == null) {
			this.getInstance().setId(IdGenerator.randomUUID());
		}
		Banner banner = this.getInstance();
		List<BannerPicture> bps = banner.getPictures();
		if (bps == null || bps.size() == 0) {
			FacesUtil.addErrorMessage(sm.getString("pictureNullError"));
			return null;
		}
		for (int i = 0; i < bps.size(); i++) {
			bps.get(i).setBanner(banner);
			bps.get(i).setSeqNum(i + 1);
		}
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("Banner保存成功！");
		return getSaveView();
	}
}
