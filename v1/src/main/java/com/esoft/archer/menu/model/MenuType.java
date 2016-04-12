package com.esoft.archer.menu.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * MenuType entity. 
 * 菜单类型
 */
@Entity
@Table(name = "menu_type")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MenuType implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String enable;
	private String description;
	private Set<Menu> menus = new HashSet<Menu>(0);

	// Constructors

	/** default constructor */
	public MenuType() {
	}

	/** minimal constructor */
	public MenuType(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/** full constructor */
	public MenuType(String id, String name, String description, Set<Menu> menus) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.menus = menus;
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

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}
	
	@Column(name = "enable", nullable = false, length = 1)
	public String getEnable() {
		return enable;
	}

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "menuType")
	public Set<Menu> getMenus() {
		return this.menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

}