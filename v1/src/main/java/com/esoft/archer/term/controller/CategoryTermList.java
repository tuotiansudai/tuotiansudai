package com.esoft.archer.term.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.common.model.PageModel;
import com.esoft.archer.menu.model.TreeNode;
import com.esoft.archer.term.TermConstants;
import com.esoft.archer.term.model.CategoryTerm;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

/**
 * 菜单类型查询
 * 
 * @author wanghm
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class CategoryTermList extends EntityQuery<CategoryTerm> implements
		java.io.Serializable {

	private static final long serialVersionUID = 8682798674042066949L;

	static StringManager sm = StringManager.getManager(TermConstants.Package);
	@Logger
	static Log log;
	@Resource
	HibernateTemplate ht;
	public String categoryTermId;
	private PageModel<CategoryTerm> pageModel;

	private List<TreeNode> termTree;

	private String termTypeId;

	public CategoryTermList() {
		final String[] RESTRICTIONS = {
				"id like #{categoryTermList.example.id}",
				"name like #{categoryTermList.example.name}",
				"description like #{categoryTermList.example.description}" };

		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));

	}

	private void buildTermTree(CategoryTerm parent, List<CategoryTerm> list) {
		if (list != null && list.size() > 0) {
			for (CategoryTerm c : list) {
				if (c.getParent() != null
						&& StringUtils.isNotEmpty(c.getParent().getId())
						&& c.getParent().getId().equals(parent.getId())
						&& !c.getId()
								.equals(((CategoryTerm) FacesUtil
										.getExpressionValue("#{categoryTermHome.instance}"))
										.getId())) {
					int level = 0;
					CategoryTerm parentTerm = c.getParent();
					while (parentTerm != null) {
						level++;
						parentTerm = parentTerm.getParent();
					}
					int prod = (int) Math.pow(2, (level + 1));
					String prefixStr = "";
					for (int i = 0; i < prod; i++) {
						if (i == (prod - 1)) {
							prefixStr = prefixStr + "&nbsp;-";
						} else {
							prefixStr = prefixStr + "&nbsp;";
						}
					}
					TreeNode node = createNewNode(c.getId(),
							prefixStr + c.getName());
					termTree.add(node);
					buildTermTree(c, list);
				}
			}
		}
	}

	private TreeNode createNewNode(String id, String label) {
		TreeNode node = new TreeNode();
		node.setId(id);
		node.setLabel(label);
		return node;
	}

	/**
	 * 获取所有的categoryTerm，除了自身
	 */
	public List<CategoryTerm> getAllResultExceptSelf() {
		List<CategoryTerm> results = getAllResultList();
		if (categoryTermId != null) {
			results.remove(new CategoryTerm(categoryTermId));
		}
		return getAllResultList();
	}

	public String getCategoryTermId() {
		return categoryTermId;
	}

	public PageModel<CategoryTerm> getPageModel() {
		return pageModel;
	}

	@SuppressWarnings("unchecked")
	public PageModel<CategoryTerm> getTerms(final String parentId,
			final int firstResult, final int maxResults) {
		// 父分类ID不能为空
		if (StringUtils.isNotEmpty(parentId)) {
			int page = 0;
			if (StringUtils.isNotEmpty(FacesUtil.getParameter("page"))) {
				page = Integer.valueOf(FacesUtil.getParameter("page"));
			}
			final PageModel<CategoryTerm> pageModel = new PageModel<CategoryTerm>();
			pageModel.setPage(page);

			// data
			List<CategoryTerm> data = getHt().executeFind(
					new HibernateCallback<List<CategoryTerm>>() {
						public List<CategoryTerm> doInHibernate(Session session)
								throws HibernateException, SQLException {

							Query query = session
									.getNamedQuery("CategoryTerm.findByParentIdOrderBySeqNum");
							query.setParameter("pId", parentId);

							Query query2 = session
									.getNamedQuery("CategoryTerm.getTermCountByParentId");
							query2.setParameter("pId", parentId);
							query.setCacheable(true);
							query.setFirstResult(firstResult);
							query.setMaxResults(maxResults);

							query2.setCacheable(true);
							pageModel.setCount(Integer.valueOf(query2
									.uniqueResult().toString()));
							return query.list();
						}
					});
			pageModel.setData(data);
			this.pageModel = pageModel;
		}
		return this.pageModel;
	}

	//FIXME:分类术语  树形展示，查询，选择，还有menu  提供分页遍历的方法。
	public List<TreeNode> getTermTree() {

		termTree = new ArrayList<TreeNode>();
		if (StringUtils.isNotEmpty(termTypeId)) {
			FacesUtil.getViewMap().put("termTypeId", termTypeId);
		} else {
			termTypeId = (String) FacesUtil.getViewMap().get("termTypeId");
		}
		if (StringUtils.isEmpty(termTypeId)) {
			termTypeId = ((CategoryTerm) FacesUtil
					.getExpressionValue("#{categoryTermHome.instance}"))
					.getCategoryTermType().getId();
			FacesUtil.getViewMap().put("termTypeId", termTypeId);
		}
		List<CategoryTerm> termList = ht.findByNamedQuery(
				"CategoryTerm.findByType", termTypeId);
		if (termList != null && termList.size() > 0) {
			for (CategoryTerm c : termList) {
				if (c.getParent() == null
						&& !c.getId()
								.equals(((CategoryTerm) FacesUtil
										.getExpressionValue("#{categoryTermHome.instance}"))
										.getId())) {
					TreeNode node = createNewNode(c.getId(), c.getName());
					termTree.add(node);
					buildTermTree(c, termList);
				}
			}
		}
		return termTree;
	}

	public String getTermTypeId() {
		return termTypeId;
	}

	public void handleTypeChange() {
		this.termTypeId = ((CategoryTerm) FacesUtil
				.getExpressionValue("#{categoryTermHome.instance}"))
				.getCategoryTermType().getId();
		this.termTree = getTermTree();
	}

	public void setCategoryTermId(String categoryTermId) {
		this.categoryTermId = categoryTermId;
	}

	public void setPageModel(PageModel<CategoryTerm> pageModel) {
		this.pageModel = pageModel;
	}

	public void setTermTree(List<TreeNode> termTree) {
		this.termTree = termTree;
	}

	public void setTermTypeId(String termTypeId) {
		this.termTypeId = termTypeId;
	}

}
