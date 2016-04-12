package com.esoft.archer.product.controller;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.node.NodeConstants;
import com.esoft.archer.node.controller.NodeHome;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.node.model.NodeBody;
import com.esoft.archer.node.model.NodeType;
import com.esoft.archer.product.ProductConstants;
import com.esoft.archer.product.model.Product;
import com.esoft.archer.product.model.ProductPicture;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class ProductHome extends NodeHome {
	private static final StringManager sm = StringManager
			.getManager(ProductConstants.Package);

	@Override
	public Node createInstance() {
		Product product = new Product();
		product.setNodeType(new NodeType());
		product.setNodeBody(new NodeBody());
		product.setProductPictures(new ArrayList<ProductPicture>());
		return product;
	}

	@Override
	public Class getEntityClass() {
		return Product.class;
	}

	public ProductHome() {
		setUpdateView(FacesUtil.redirect(ProductConstants.View.PRODUCT_LIST));
	}

	@Override
	public String save() {
		Product p = (Product) this.getInstance();
		if (p.getProductPictures() == null
				|| p.getProductPictures().size() == 0) {
			FacesUtil.addErrorMessage(sm.getString("picutureNullError"));
			return null;
		}
		for (ProductPicture pp : p.getProductPictures()) {
			pp.setProduct(p);
		}
		return super.save();
	}
}
