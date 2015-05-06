package com.esoft.archer.menu.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.esoft.archer.user.model.Permission;
import com.esoft.core.util.SpringBeanUtil;

/**
 * Menu entity. 菜单
 */
@Entity
@Table(name = "menu")
@NamedQueries({
		@NamedQuery(name = "Menu.findMenusByType", query = "Select distinct m from Menu m left join fetch m.children "
				+ "where m.enable = 1 and m.parent.id is null and m.menuType.id=? and m.menuType.enable = 1 order by m.seqNum"),
		@NamedQuery(name = "Menu.findMenusByTypeId", query = "Select m from Menu m where m.menuType.id like ?"),
		@NamedQuery(name = "Menu.getMenuByUrl", query = "select menu from Menu menu where menu.url=:url"),
		@NamedQuery(name = "Menu.findMenusByUrl", query = "Select menu from Menu menu where menu.url like ? and menu.parent is not null"),
		@NamedQuery(name = "Menu.findMenusByParentId", query = "Select distinct m from Menu m left join fetch m.children where m.parent.id = ?  order by m.seqNum") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class Menu implements java.io.Serializable {

	// Fields

	private String id;
	private MenuType menuType;
	private Menu parent;
	/**
	 * 此菜单可用的权限
	 */
	private List<Permission> permissions = new ArrayList<Permission>(0);
	private String label;
	private String url;
	private String enable;
	private String icon;
	/**
	 * 菜单是否以展开的方式使用，如果不展开查询菜单则不查询该节点下的子菜单（可选值：1/0）
	 */
	private String expanded;
	private String description;

	private Integer seqNum;
	private String language;
	private String target;
	private String permissionsCommaStr;
	private List<Menu> children = new ArrayList<Menu>(0);

	// Constructors

	/** default constructor */
	public Menu() {
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type", nullable = false)
	public MenuType getMenuType() {
		return this.menuType;
	}

	public void setMenuType(MenuType menuType) {
		this.menuType = menuType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	public Menu getParent() {
		return this.parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "menu_permission", joinColumns = { @JoinColumn(name = "menu_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "permission_id", nullable = false, updatable = false) })
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	@Column(name = "label", nullable = false, length = 100)
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Column(name = "url", length = 1000)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "enable", nullable = false, length = 1)
	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "icon", length = 80)
	public String getIcon() {
		return icon;
	}

	public void setExpanded(String expanded) {
		this.expanded = expanded;
	}

	@Column(name = "expanded", nullable = false, length = 1)
	public String getExpanded() {
		return expanded;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "description", length = 1000)
	public String getDescription() {
		return description;
	}

	@Column(name = "seq_num")
	public Integer getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "parent")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@OrderBy(value = "seqNum")
	public List<Menu> getChildren() {
		return this.children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	@Column(name = "language", length = 10, nullable = false)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(name = "target", length = 20)
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * 获取权限，返回用逗号分隔的字符串
	 * 
	 * @return
	 */
	@Transient
	public String getPermissionsCommaStr() {
		// FIXME:不太合适。。。总感觉如下处理比较别扭。
		if (StringUtils.isEmpty(this.permissionsCommaStr)) {
			HibernateTemplate ht = (HibernateTemplate) SpringBeanUtil
					.getBeanByName("ht");
			StringBuffer result = new StringBuffer();
			if (!StringUtils.isEmpty(this.getId())) {
				List<Permission> permissions = ht
						.findByNamedQueryAndNamedParam(
								"Permission.findPermissionsByMenuId", "menuId",
								this.getId());
				if (permissions.size() > 0) {
					result.append(new StringBuffer(permissions.get(0).getId()));
					for (int i = 1; i < permissions.size(); i++) {
						result.append(",");
						result.append(permissions.get(i).getId());
					}
				}
			}
			this.setPermissionsCommaStr(result.toString());
		}
		return this.permissionsCommaStr;
	}

	public void setPermissionsCommaStr(String permissionsCommaStr) {
		this.permissionsCommaStr = permissionsCommaStr;
	}

}
