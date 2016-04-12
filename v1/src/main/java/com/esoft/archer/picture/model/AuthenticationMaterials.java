package com.esoft.archer.picture.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *  认证材料
 * @author Administrator
 *
 */
@Entity
@Table(name = "authentication_materials")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AuthenticationMaterials {

	// Fields

	private static final long serialVersionUID = 2489788649352321423L;
	private String id;
	/**
	 * 材料类型
	 */
	private AutcMtrType type;
	private String description;
	private List<AutcMtrPicture> pictures = new ArrayList<AutcMtrPicture>(0);

	/** default constructor */
	public AuthenticationMaterials() {
	}

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}
	

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "autcMtr")
	@OrderBy(value="seqNum")
	public List<AutcMtrPicture> getPictures() {
		return this.pictures;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "type")
	public AutcMtrType getType() {
		return type;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPictures(List<AutcMtrPicture> bannerPictures) {
		this.pictures = bannerPictures;
	}

	public void setType(AutcMtrType type) {
		this.type = type;
	}

}