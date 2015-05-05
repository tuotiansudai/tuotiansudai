package com.esoft.archer.user.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Role entity. 角色
 */
@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Role implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String description;
	private List<Permission> permissions = new ArrayList<Permission>(0);
	private List<User> users = new ArrayList<User>(0);

	// Constructors

	/** default constructor */
	public Role() {
	}

	/** minimal constructor */
	public Role(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Role(String id) {
		this.id = id;
	}

	/** full constructor */
	public Role(String id, String name, String description,
			List<Permission> permissions, List<User> users) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.permissions = permissions;
		this.users = users;
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

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "role_permission",
		joinColumns = { @JoinColumn(name = "role_id", nullable = false, updatable = false) },
		inverseJoinColumns = { @JoinColumn(name = "permission_id", nullable = false, updatable = false) })
	public List<Permission> getPermissions() {
		return this.permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "roles")
	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}