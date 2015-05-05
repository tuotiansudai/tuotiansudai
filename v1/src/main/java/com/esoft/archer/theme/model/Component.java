package com.esoft.archer.theme.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * Component entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "component")
@NamedQueries({
//	@NamedQuery(name="Component.findComponentByRegionId",
//			query="Select c from Component c, RegionComponent rc" +
//				" where c in elements(rc.component) and rc.region.id=?" +
//				" order by rc.seqNum")
	@NamedQuery(name="Component.findByUrl",
			query="Select distinct(c) from Component c left join fetch c.componentParameters where c.scriptUrl = ?"),
	@NamedQuery(name="Component.findAll",
			query="Select c from Component c")
})
public class Component implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String scriptUrl;
	private String enable;
	private String description;
	private List<ComponentParameter> componentParameters = new ArrayList<ComponentParameter>(
			0);
	private Set<RegionComponent> regionComponents = new HashSet<RegionComponent>(
			0);

	// Constructors

	/** default constructor */
	public Component() {
	}

	/** minimal constructor */
	public Component(String name, String enable) {
		this.name = name;
		this.enable = enable;
	}

	/** full constructor */
	public Component(String name, String scriptUrl, String enable,
			String description, List<ComponentParameter> componentParameters,
			Set<RegionComponent> regionComponents) {
		this.name = name;
		this.scriptUrl = scriptUrl;
		this.enable = enable;
		this.description = description;
		this.componentParameters = componentParameters;
		this.regionComponents = regionComponents;
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

	@Column(name = "script_url", length = 500)
	public String getScriptUrl() {
		return this.scriptUrl;
	}

	public void setScriptUrl(String scriptUrl) {
		this.scriptUrl = scriptUrl;
	}

	@Column(name = "enable", nullable = false, length = 1)
	public String getEnable() {
		return this.enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "component")
	public List<ComponentParameter> getComponentParameters() {
		return this.componentParameters;
	}

	public void setComponentParameters(
			List<ComponentParameter> componentParameters) {
		this.componentParameters = componentParameters;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "component")
	public Set<RegionComponent> getRegionComponents() {
		return this.regionComponents;
	}

	public void setRegionComponents(Set<RegionComponent> regionComponents) {
		this.regionComponents = regionComponents;
	}

}