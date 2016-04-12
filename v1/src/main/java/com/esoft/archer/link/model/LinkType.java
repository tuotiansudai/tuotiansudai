package com.esoft.archer.link.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * 友情链接类型
 */
@Entity
@Table(name = "link_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class LinkType implements java.io.Serializable {
	private static final long serialVersionUID = -7152610684769357391L;
	private String id;
	private String name;
	private String description;

	/** default constructor */
	public LinkType() {
	}

	/** minimal constructor */
	public LinkType(String id) {
		this.id = id;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}