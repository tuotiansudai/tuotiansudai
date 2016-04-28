package com.esoft.archer.product.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.Iterator;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.node.model.NodeBodyHistory;
import com.esoft.archer.product.model.Product;
import com.esoft.archer.product.model.ProductPicture;
import com.esoft.archer.product.service.ProductService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Resource
	HibernateTemplate ht;
	@Logger
	static Log log;

	@Transactional
	public void save(Product product) {
		ht.merge(product);
	}

	public ProductPicture addProductPicture(String productId, String picture) {
		ProductPicture pp = new ProductPicture();
		pp.setId(IdGenerator.randomUUID());
		pp.setPicture(picture);
		Product p = new Product();
		p.setId(productId);
		pp.setProduct(p);
		// ht.save(pp);
		return pp;
	}

	public void deleteProductPicture(String productPictureId) {

	}

	public void deleteProductPicture(ProductPicture productPicture) {
		File file = new File(FacesUtil.getAppRealPath()
				+ productPicture.getPicture());
		file.delete();
		ProductPicture pp = ht
				.get(ProductPicture.class, productPicture.getId());
		if (pp != null) {
			ht.delete(pp);
		}
	}

	public void saveProductPicture(ProductPicture pp) {
		ht.save(pp);
	}
}
