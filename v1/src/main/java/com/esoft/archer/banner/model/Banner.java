package com.esoft.archer.banner.model;

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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.esoft.archer.node.model.Node;

@Entity
@Table(name = "banner")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Banner {

	// Fields

	private static final long serialVersionUID = 2489798649352321423L;
	private String id;
	private String description;
	private List<BannerPicture> pictures = new ArrayList<BannerPicture>(0);

	/** default constructor */
	public Banner() {
	}

	/** full constructor */
	public Banner(String description, List<BannerPicture> bannerPictures) {
		this.description = description;
		this.pictures = bannerPictures;
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
	
//	@NotEmpty
//	@Email(message="#{bannerMsg.pictureNullError}")
//	@Size(min=5,max=10)
	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "banner")
	@OrderBy(value="seqNum")
	public List<BannerPicture> getPictures() {
		return this.pictures;
	}

	public void setPictures(List<BannerPicture> bannerPictures) {
		this.pictures = bannerPictures;
	}

}