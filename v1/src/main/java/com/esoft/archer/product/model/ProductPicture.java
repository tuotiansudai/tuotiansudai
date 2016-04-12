package com.esoft.archer.product.model;

import javax.persistence.CascadeType;
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
 * ProductPicture entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "product_picture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class ProductPicture implements java.io.Serializable {

	// Fields

	private String id;
	private Product product;
	private String picture;

	// Constructors

	/** default constructor */
	public ProductPicture() {
	}

	/** full constructor */
	public ProductPicture(Product product, String picture) {
		this.product = product;
		this.picture = picture;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Column(name = "picture", length = 300)
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}