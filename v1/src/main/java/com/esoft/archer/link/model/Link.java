package com.esoft.archer.link.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * 友情链接
 */
@Entity
@Table(name = "link")
@NamedQueries({
		@NamedQuery(name = "Link.findLinkByPositionOrderBySeqNumAndName", query = "from Link where position = ? order by seqNum,name ")
})				
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Link implements java.io.Serializable {
	private static final long serialVersionUID = -7152680684769357391L;
	private String id;
	private String name;
	private LinkType type;
	private String url;
	private String logo;
	private String description;
	private String masterEmail;
	private String position;
	private String status;
	private Integer seqNum;

	/** default constructor */
	public Link() {
	}

	/** minimal constructor */
	public Link(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@Column(name = "logo", length = 500)
	public String getLogo() {
		return logo;
	}

	@Column(name = "master_email", length = 500)
	public String getMasterEmail() {
		return masterEmail;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	@Column(name = "position", nullable=false, length = 50)
	public String getPosition() {
		return position;
	}

	@Column(name = "status", nullable=false)
	public String getStatus() {
		return status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	public LinkType getType() {
		return type;
	}

	@Column(name = "url", length = 500)
	public String getUrl() {
		return url;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setMasterEmail(String masterEmail) {
		this.masterEmail = masterEmail;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setType(LinkType type) {
		this.type = type;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}
	@Column(name = "seq_num")
	public Integer getSeqNum() {
		return seqNum;
	}

}