package com.esoft.archer.config.model;

import java.util.ArrayList;
import java.util.List;
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
 * ConfigType entity. 系统配置的分类
 */
@Entity
@Table(name = "config_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class ConfigType implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String description;
	private List<Config> configs = new ArrayList<Config>(0);

	// Constructors

	/** default constructor */
	public ConfigType() {
	}

	/** minimal constructor */
	public ConfigType(String id) {
		this.id = id;
	}

	/** full constructor */
	public ConfigType(String id, String name, String description,
			List<Config> configs) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.configs = configs;
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

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "configType")
	public List<Config> getConfigs() {
		return this.configs;
	}

	public void setConfigs(List<Config> configs) {
		this.configs = configs;
	}

}