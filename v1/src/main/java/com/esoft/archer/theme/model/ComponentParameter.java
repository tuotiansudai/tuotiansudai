package com.esoft.archer.theme.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * ComponentParameter entity. 
 * 元件参数
 */
@Entity
@Table(name = "component_parameter")
@NamedQueries({
	@NamedQuery(name="ComponentParameter.findByCompanent",
			query="Select c from ComponentParameter c where c.component.id = ?")
})
public class ComponentParameter implements java.io.Serializable {

	// Fields

	private String id;
	private Component component;
	private String name;
	private String value;
	private String description;

	// Constructors

	/** default constructor */
	public ComponentParameter() {
	}

	/** minimal constructor */
	public ComponentParameter(String id, Component component) {
		this.id = id;
		this.component = component;
	}

	/** full constructor */
	public ComponentParameter(String id, Component component, String name,
			String value, String description) {
		this.id = id;
		this.component = component;
		this.name = name;
		this.value = value;
		this.description = description;
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
	@JoinColumn(name = "component_id", nullable = false)
	public Component getComponent() {
		return this.component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	@Column(name = "name", length = 100)
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

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}