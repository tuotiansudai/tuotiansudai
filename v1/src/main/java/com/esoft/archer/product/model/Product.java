package com.esoft.archer.product.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esoft.archer.node.model.Node;

/**
 * Product entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Product extends Node {

	// Fields

	private static final long serialVersionUID = 2489798649352351423L;
	@Transient
	// private String thumbnail;
	private String characteristics;
	private List<ProductPicture> productPictures = new ArrayList<ProductPicture>(
			0);

	// Constructors

	/** default constructor */
	public Product() {
	}

	/** full constructor */
	public Product(String characteristics, List<ProductPicture> productPictures) {
		// this.thumbnail = thumbnail;
		this.characteristics = characteristics;
		this.productPictures = productPictures;
	}

	/**
	 * 默认返回所有图片中的第一张
	 * 
	 * @return
	 */
	// public String getThumbnail() {
	// if (this.getProductPictures().size()>0) {
	// return getProductPictures().get(0).getPicture();
	// }
	// return this.thumbnail;
	// }

	// public void setThumbnail(String thumbnail) {
	// this.thumbnail = thumbnail;
	// }

	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name = "characteristics", columnDefinition="longtext")
	public String getCharacteristics() {
		return this.characteristics;
	}

	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
	public List<ProductPicture> getProductPictures() {
		return this.productPictures;
	}

	public void setProductPictures(List<ProductPicture> productPictures) {
		this.productPictures = productPictures;
	}

}