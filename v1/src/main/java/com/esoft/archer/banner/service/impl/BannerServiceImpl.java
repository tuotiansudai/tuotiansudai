package com.esoft.archer.banner.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.Iterator;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.banner.model.BannerPicture;
import com.esoft.archer.banner.service.BannerService;
import com.esoft.archer.node.model.NodeBodyHistory;
import com.esoft.archer.product.model.Product;
import com.esoft.archer.product.model.ProductPicture;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;

@Service
@Transactional
public class BannerServiceImpl implements BannerService {

	@Resource
	HibernateTemplate ht;
	@Logger
	static Log log;

	public void deleteBannerPicture(BannerPicture bannerPicture) {
		File file = new File(FacesUtil.getAppRealPath()
				+ bannerPicture.getPicture());
		file.delete();
		BannerPicture pp = ht.get(BannerPicture.class, bannerPicture.getId());
		if (pp != null) {
			ht.delete(pp);
		}
	}
}
