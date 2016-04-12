package com.esoft.archer.picture.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 认证材料图片
 * @author Administrator
 *
 */
@Entity
@Table(name = "autc_mtr_picture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AutcMtrPicture implements java.io.Serializable {

	// Fields

	private String id;
	private AuthenticationMaterials autcMtr;
	private String title;
	private String url;
	private Integer seqNum;
	private String picture;

	/** default constructor */
	public AutcMtrPicture() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auth_mtr_id")
	public AuthenticationMaterials getAutcMtr() {
		return autcMtr;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@Column(name = "picture", length = 300)
	public String getPicture() {
		return this.picture;
	}

	@Column(name = "seq_num", nullable=false)
	public Integer getSeqNum() {
		return this.seqNum;
	}

	@Column(name = "title", length = 100)
	public String getTitle() {
		return title;
	}

	@Column(name = "url", length = 300)
	public String getUrl() {
		return url;
	}

	public void setAutcMtr(AuthenticationMaterials autcMtr) {
		this.autcMtr = autcMtr;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}