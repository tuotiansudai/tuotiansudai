package com.esoft.archer.node.model;

import java.util.Date;

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
 * NodeBodyHistory entity. 
 * 节点主体的历史版本。
 */
@Entity
@Table(name = "node_body_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class NodeBodyHistory implements java.io.Serializable {

	// Fields

	private String id;
	private Node node;
	private Date createTime;
	private Double version;
	private String body;

	// Constructors

	/** default constructor */
	public NodeBodyHistory() {
	}

	/** minimal constructor */
	public NodeBodyHistory(String id, Node node, Date createTime, Double version) {
		this.id = id;
		this.node = node;
		this.createTime = createTime;
		this.version = version;
	}

	/** full constructor */
	public NodeBodyHistory(String id, Node node, Date createTime,
			Double version, String body) {
		this.id = id;
		this.node = node;
		this.createTime = createTime;
		this.version = version;
		this.body = body;
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
	@JoinColumn(name = "node_id", nullable = false)
	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	@Column(name = "create_time", nullable = false, length = 8)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "version", nullable = false, precision = 22, scale = 0)
	public Double getVersion() {
		return this.version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name = "body", columnDefinition="longtext")
	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}