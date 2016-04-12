package com.esoft.archer.node.model;

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
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * NodeAttr entity. 
 * 节点属性
 */
@Entity
@Table(name = "node_attr")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class NodeAttr implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String description;
	private Set<Node> nodes = new HashSet<Node>(0);

	// Constructors

	/** default constructor */
	public NodeAttr() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/** minimal constructor */
	public NodeAttr(String id) {
		this.id = id;
	}

	/** full constructor */
	public NodeAttr(String id, String name, String description, Set<Node> nodes) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.nodes = nodes;
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

	//@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	//@JoinTable(name = "node_node_attr", joinColumns = { @JoinColumn(name = "node_attr_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "node_id", nullable = false, updatable = false) })
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "nodeAttrs")
	public Set<Node> getNodes() {
		return this.nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

}