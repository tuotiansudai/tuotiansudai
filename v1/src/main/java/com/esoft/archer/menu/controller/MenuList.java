package com.esoft.archer.menu.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.common.service.BaseService;
import com.esoft.archer.menu.MenuConstants;
import com.esoft.archer.menu.model.Menu;
import com.esoft.archer.menu.model.MenuType;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

/**
 * 菜单查询控制器
 * 
 * @author wanghm
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class MenuList extends EntityQuery<Menu> implements java.io.Serializable {

	@Logger
	static Log log;
	@Resource
	private HibernateTemplate ht;

	private static StringManager sm = StringManager
			.getManager(MenuConstants.Package);

	private TreeNode root;

	private String menuTypeId;

	private List<com.esoft.archer.menu.model.TreeNode> menuTree;
	private static final String[] RESTRICTIONS = {
			"id like #{menuList.example.id}",
			"label like #{menuList.example.label}",
			"url like #{menuList.example.url}",
			"menuType.id = #{menuList.example.menuType.id}" };

	public MenuList() {
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));

	}

	public String getMenuTypeId() {
		return menuTypeId;
	}

	public void setMenuTypeId(String menuTypeId) {
		this.menuTypeId = menuTypeId;
	}

	@Override
	public void initExample() {
		Menu menu = new Menu();
		MenuType menuType = new MenuType();
		// menuType.setId(MenuConstants.MenuType.MAIN_MENU);
		menu.setMenuType(menuType);
		setExample(menu);
	}

	@Resource(name = "baseService")
	BaseService<MenuType> menuTypeService;

	public List<Menu> getMainMenus() {
		List<Menu> mainMenus = getMenusByType(MenuConstants.MenuType.MAIN_MENU);
		return mainMenus;
	}

	public List<Menu> getNavigationMenus() {
		return getMenusByType(MenuConstants.MenuType.NAVIGATION);
	}

	public List<Menu> getManagementMenus() {
		return getMenusByType(MenuConstants.MenuType.MANAGEMENT);
	}

	@SuppressWarnings("unchecked")
	public List<Menu> getMenusByType(String typeId) {
		if (StringUtils.isEmpty(typeId)) {
			return null;
		}
		getHt().setCacheQueries(true);
		return getHt().findByNamedQuery("Menu.findMenusByType", typeId);
	}

	/**
	 * 通过菜单编号，查询该菜单下的所有子菜单（包含 enable为不可用的）
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Menu> getMenusByParentId(final String parentId) {
		getHt().setCacheQueries(true);
		return getHt().findByNamedQuery("Menu.findMenusByParentId", parentId);
	}

	/**
	 * 通过url路径找到相匹配（多个相同URL同样的情况下，只取第一个结果）的菜单的根枝干菜单编号。<br/>
	 * 原则： <li>1.比如 （NULL） - news(/term/news) - setup(/node/news/setup)，传入
	 * “/node/news/setup” 得出 news</li> <li>
	 * 2.如果是以“/node/{分类术语编号}/{文章编号}”的格式，如果为找到结果集，则试图去查找URL“/term/{分类术语编号}”</li>
	 * <li>3.如果为找到URL“/term/{分类术语编号}”，则尝试查找URL“/term/{分类术语编号}%”</li>
	 * 
	 * @param url
	 * @return
	 */
	public String getParentIdByChildUrl(String url) {

		if (StringUtils.isEmpty(url)) {
			return null;
		}
		String parentId = null;
		List<Menu> menus = getHt().findByNamedQuery("Menu.findMenusByUrl", url);
		if (menus.size() > 0) {
			Menu tmp = menus.get(0);
			while (tmp.getParent() != null) {
				tmp = tmp.getParent();
			}
			parentId = (tmp.getId());

		}

		// 如果符合 /node/**/** 模式，并且在菜单表中未找到该路径
		String[] params = url.split("/");
		if (parentId == null && StringUtils.equals("node", params[1]) && !url.contains("%")) {
			String termId = params[2];
			// 如果符合 /node/**/** 模式
			parentId = getParentIdByChildUrl("/term/" + termId);
		} else if (parentId == null && StringUtils.equals("term", params[1])
				&& !url.contains("%")) {
			String termId = params[2];
			parentId = getParentIdByChildUrl("/term/" + termId + "%");
		} else if (parentId == null && StringUtils.equals("term", params[1])
				&& url.contains("%")) {
			String termId = params[2];
			if (!termId.contains("%")) {
				termId = termId.concat("%");
			}
			parentId = getParentIdByChildUrl("/node/" + termId);
		} else if (parentId == null && url.contains("/node/")
				&& url.contains("%")) {
			return null;
		}

		return parentId;
	}

	public TreeNode getRoot() {
		root = new DefaultTreeNode("root", null);
		root.setExpanded(true);
		List<Menu> menuList = new ArrayList<Menu>();
		addOrder("seqNum", DIR_ASC);
		menuList = ht.find(getRenderedHql(), getParameterValues());
		if (menuList != null && menuList.size() > 0) {
			for (Menu m : menuList) {
				if ((m.getParent() == null || StringUtils.isEmpty(m.getParent()
						.getId())) || !menuList.contains(m.getParent())) {
					TreeNode newChild = createNewNode(m, root);
					initTreeNode(menuList, m, newChild);
				}
			}
		}
		return root;
	}

	private TreeNode createNewNode(Menu menu, TreeNode parentNode) {
		TreeNode newNode = new DefaultTreeNode(menu, parentNode);
		newNode.setExpanded(true);
		return newNode;
	}

	private void initTreeNode(List<Menu> menuList, Menu parentMenu,
			TreeNode parentNode) {
		if (menuList != null && menuList.size() > 0) {
			for (Menu menu : menuList) {
				if (menu.getParent() != null
						&& menu.getParent().getId().equals(parentMenu.getId())) {
					TreeNode newNode = createNewNode(menu, parentNode);
					initTreeNode(menuList, menu, newNode);
				}
			}
		}

	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	private com.esoft.archer.menu.model.TreeNode createNewNode(String id,
			String label) {
		com.esoft.archer.menu.model.TreeNode node = new com.esoft.archer.menu.model.TreeNode();
		node.setId(id);
		node.setLabel(label);
		return node;
	}

	public List<com.esoft.archer.menu.model.TreeNode> getMenuTree() {
		menuTree = new ArrayList<com.esoft.archer.menu.model.TreeNode>();
		if (StringUtils.isNotEmpty(menuTypeId)) {
			FacesUtil.getViewMap().put("menuTypeId", menuTypeId);
		} else {
			menuTypeId = (String) FacesUtil.getViewMap().get("menuTypeId");
		}
		if (StringUtils.isEmpty(menuTypeId)) {
			menuTypeId = ((Menu) FacesUtil
					.getExpressionValue("#{menuHome.instance}")).getMenuType()
					.getId();
			FacesUtil.getViewMap().put("menuTypeId", menuTypeId);
		}
		List<Menu> list = ht.findByNamedQuery("Menu.findMenusByTypeId",
				menuTypeId);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Menu menu = list.get(i);
				if (menu.getParent() == null
						&& !menu.getId()
								.equals(((Menu) FacesUtil
										.getExpressionValue("#{menuHome.instance}"))
										.getId())) {
					com.esoft.archer.menu.model.TreeNode node = createNewNode(
							menu.getId(), menu.getLabel());
					menuTree.add(node);
					buildMenuTree(menu, list);
				}
			}
		}
		return menuTree;
	}

	private void buildMenuTree(Menu parent, List<Menu> menuList) {
		for (int i = 0; i < menuList.size(); i++) {
			Menu menu = menuList.get(i);
			if (menu.getParent() != null
					&& StringUtils.isNotEmpty(menu.getParent().getId())
					&& menu.getParent().getId().equals(parent.getId())
					&& !menu.getId()
							.equals(((Menu) FacesUtil
									.getExpressionValue("#{menuHome.instance}"))
									.getId())) {
				int level = 0;// 记录parent是第几级菜单
				Menu newMenu = parent.getParent();
				while (newMenu != null) {
					level++;
					newMenu = newMenu.getParent();
				}
				String prefixStr = "";
				int prod = (int) Math.pow(2, (level + 1));
				for (int j = 0; j < prod; j++) {
					if (j == (prod - 1)) {
						prefixStr = prefixStr + "&nbsp;-";
					} else {
						prefixStr = prefixStr + "&nbsp;";
					}
				}
				com.esoft.archer.menu.model.TreeNode node = createNewNode(
						menu.getId(), prefixStr + menu.getLabel());
				menuTree.add(node);
				buildMenuTree(menu, menuList);
			}
		}
	}

	public void setMenuTree(List<com.esoft.archer.menu.model.TreeNode> menuTree) {
		this.menuTree = menuTree;
	}

	public void handleMenuChange() {
		menuTypeId = ((Menu) FacesUtil
				.getExpressionValue("#{menuHome.instance}")).getMenuType()
				.getId();
		this.menuTree = this.getMenuTree();
	}

}
