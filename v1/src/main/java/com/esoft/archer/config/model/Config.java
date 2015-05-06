package com.esoft.archer.config.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Config entity. 系统配置
 */
@Entity
@Table(name = "config")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region="entityCache")
public class Config implements java.io.Serializable {

	// Fields

	private String id;
	private ConfigType configType;
	private String name;
	private String value;
	private String description;

	// Constructors

	/** default constructor */
	public Config() {
	}

	/** minimal constructor */
	public Config(String id) {
		this.id = id;
	}

	/** full constructor */
	public Config(String id, ConfigType configType, String name, String value,
			String description) {
		this.id = id;
		this.configType = configType;
		this.name = name;
		this.value = value;
		this.description = description;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 128)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	public ConfigType getConfigType() {
		return this.configType;
	}

	public void setConfigType(ConfigType configType) {
		this.configType = configType;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob 
	@Column(name = "value", columnDefinition="CLOB")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}