package com.esoft.archer.banner.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.esoft.jdp2p.loan.model.Loan;

@Entity
@Table(name = "banner_picture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BannerPicture implements java.io.Serializable {

	// Fields

	private String id;
	private Banner banner;
	private String title;
	private String url;
	private Integer seqNum;
	/**
	 * 是否为外链
	 */
	private Boolean isOutSite;
	private String picture;

	private List<Loan> loanInfoPics = new ArrayList<Loan>(0);
	private List<Loan> loanGuaranteePics = new ArrayList<Loan>(0);

	// Constructors

	/** default constructor */
	public BannerPicture() {
	}

	/** full constructor */
	public BannerPicture(Banner banner, String picture) {
		this.banner = banner;
		this.picture = picture;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "banner_id")
	public Banner getBanner() {
		return this.banner;
	}

	// Property accessors
	// @GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	// @GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@Column(name = "is_out_site", columnDefinition = "BOOLEAN")
	public Boolean getIsOutSite() {
		return isOutSite;
	}

	@Column(name = "picture", length = 300)
	public String getPicture() {
		return this.picture;
	}

	@Column(name = "title", length = 100)
	public String getTitle() {
		return title;
	}

	@Column(name = "url", length = 300)
	public String getUrl() {
		return url;
	}

	@Column(name = "seq_num", nullable = false)
	public Integer getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	public void setBanner(Banner product) {
		this.banner = product;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIsOutSite(Boolean isOutSite) {
		this.isOutSite = isOutSite;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// 如下配置，能搞定 只删除中间表
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "loan_info_pics", joinColumns = { @JoinColumn(name = "pic_id", nullable = false, updatable = true) }, inverseJoinColumns = { @JoinColumn(name = "loan_id", nullable = false, updatable = true) })
	public List<Loan> getLoanInfoPics() {
		return loanInfoPics;
	}

	public void setLoanInfoPics(List<Loan> loanInfoPics) {
		this.loanInfoPics = loanInfoPics;
	}

	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "loan_guarantee_pics", joinColumns = { @JoinColumn(name = "pic_id", nullable = false, updatable = true) }, inverseJoinColumns = { @JoinColumn(name = "loan_id", nullable = false, updatable = true) })
	public List<Loan> getLoanGuaranteePics() {
		return loanGuaranteePics;
	}

	public void setLoanGuaranteePics(List<Loan> loanGuaranteePics) {
		this.loanGuaranteePics = loanGuaranteePics;
	}

}