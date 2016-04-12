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
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;


/**
 * Region entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "region")
@NamedQueries({
	@NamedQuery(name="Region.findRegionByTemplateId",
			query="Select r from Region r, Template t " +
				" where r in elements(t.regions) and t.id=?" +
				" order by r.title")
})
public class Region implements java.io.Serializable {

	// Fields

	private String id;
	private String title;
	private String description;
	private Set<Template> templates = new HashSet<Template>(0);
	private List<RegionComponent> regionComponents = new ArrayList<RegionComponent>(
			0);

	// Constructors

	/** default constructor */
	public Region() {
	}

	/** minimal constructor */
	public Region(String title) {
		this.title = title;
	}

	/** full constructor */
	public Region(String title, String description, Set<Template> templates,
			List<RegionComponent> regionComponents) {
		this.title = title;
		this.description = description;
		this.templates = templates;
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

	@Column(name = "title", nullable = false, length = 50)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "regions")
	public Set<Template> getTemplates() {
		return this.templates;
	}

	public void setTemplates(Set<Template> templates) {
		this.templates = templates;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "region")
	public List<RegionComponent> getRegionComponents() {
		return this.regionComponents;
	}

	public void setRegionComponents(List<RegionComponent> regionComponents) {
		this.regionComponents = regionComponents;
	}

}