package com.esoft.archer.items.model;
// default package

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 候选项组
 */
@Entity
@Table(name = "select_item_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
@NamedQueries({
	@NamedQuery(name="SelectItemGroup.findSelectItemGroupByname",query = "from SelectItemGroup where name = ?")
})
public class SelectItemGroup implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 6306170412454475862L;
	private String id;
	private String name;
	private String description;
	private Set<SelectItem> selectItems = new HashSet<SelectItem>(0);

	// Constructors

	/** default constructor */
	public SelectItemGroup() {
	}

	/** minimal constructor */
	public SelectItemGroup(String id) {
		this.id = id;
	}

	/** full constructor */
	public SelectItemGroup(String id, String name, String description,
			Set<SelectItem> selectItems) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.selectItems = selectItems;
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

	@Column(name = "name", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "selectItemGroup")
	public Set<SelectItem> getSelectItems() {
		return this.selectItems;
	}

	public void setSelectItems(Set<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}

}