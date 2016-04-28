package com.esoft.archer.product.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.model.LazyDataModel;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.common.model.PageModel;
import com.esoft.archer.node.NodeConstants;
import com.esoft.archer.node.controller.NodeList;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.node.model.NodeType;
import com.esoft.archer.product.ProductConstants;
import com.esoft.archer.product.model.Product;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

/**
 * 产品查询
 * 
 */
@Component
@Scope(ScopeType.REQUEST)
public class ProductList extends NodeList {
	private static final long serialVersionUID = -1350682013319140386L;

	public void init() {
		setCountHql("SELECT count(product) FROM Product product join product.categoryTerms term ");
		setHql("SELECT product FROM Product product join product.categoryTerms term");
		final String[] RESTRICTIONS = {
				"product.id like #{productList.example.id}",
				"product.title like #{productList.example.title}",
				"product.nodeType.id = #{productList.example.nodeType.id}",
				"product in elements(term.nodes) and term.id = #{productList.example.categoryTerms[0].id}" };

		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		addOrder("product.updateTime", DIR_DESC);
	}

	@Override
	public String getNodeTypeId() {
		return ProductConstants.NodeType;
	}

	@Override
	public Class getEntityClass() {
		return Product.class;
	}

}
