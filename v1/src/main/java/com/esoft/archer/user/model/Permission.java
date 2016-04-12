package com.esoft.archer.user.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.esoft.archer.menu.model.Menu;
import com.esoft.archer.urlmapping.model.UrlMapping;

/**
 * Permission entity. 权限
 */
@Entity
@Table(name = "permission")
@NamedQueries({
		@NamedQuery(name = "Permission.findPermissionsByMenuId", query = "select distinct permission from Permission permission left join permission.menus menu where menu.id=:menuId"),
		@NamedQuery(name = "Permission.findPermissionsByUrlMappingId", query = "select distinct permission from Permission permission left join permission.urlMappings um where um.id=:urlMappingId") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Permission implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String description;
	private Set<Role> roles = new HashSet<Role>(0);
	private Set<Menu> menus = new HashSet<Menu>(0);
	private Set<UrlMapping> urlMappings = new HashSet<UrlMapping>(0);

	// Constructors

	/** default constructor */
	public Permission() {
	}

	/** minimal constructor */
	public Permission(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/** full constructor */
	public Permission(String id, String name, String description,
			Set<Role> roles, Set<Menu> menus) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.roles = roles;
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

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob
	@Column(name = "description", columnDefinition = "CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
	public Set<Menu> getMenus() {
		return this.menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
	public Set<UrlMapping> getUrlMappings() {
		return urlMappings;
	}

	public void setUrlMappings(Set<UrlMapping> urlMappings) {
		this.urlMappings = urlMappings;
	}

}