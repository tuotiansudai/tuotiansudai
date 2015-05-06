package com.esoft.jdp2p.loan.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esoft.archer.user.model.Area;
import com.esoft.archer.user.model.User;

/**
 * 申请企业借款
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "apply_enterprise_loan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class ApplyEnterpriseLoan {

	private String id;
	private User user;
	private String type;
	private String status;
	/** 公司名称 */
	private String company;
	/** 行业 */
	private String industry;
	/** 区域 */
	private Area area;
	/** 联系人 */
	private String contact;
	/** 注册号 */
	private String companyNo;
	/** 是否有担保 */
	private Boolean hasGuarantee;
	/** 担保方名称 */
	private String guaranteeName;
	/** 联系人电话 */
	private String contactMobile;
	/** 金额（万元） */
	private Double money;
	/** 借款期限 */
	private String deadline;
	/** 借款说明 */
	private String description;
	/** 还款来源 */
	private String paymentFrom;
	/** 申请时间 */
	private Date applyTime;
	/**视频的id */
	private String videoId;

	private List<ApplyEnterpriseLoanExtension> extensions;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "area")
	public Area getArea() {
		return area;
	}

	@Column(name = "company", length = 200)
	public String getCompany() {
		return company;
	}

	@Column(name = "company_no", length = 200)
	public String getCompanyNo() {
		return companyNo;
	}

	@Column(name = "contact", length = 200)
	public String getContact() {
		return contact;
	}

	@Column(name = "contact_mobile", length = 200)
	public String getContactMobile() {
		return contactMobile;
	}

	@Column(name = "deadline", length = 20)
	public String getDeadline() {
		return deadline;
	}

	@Column(name = "description", length = 1000)
	public String getDescription() {
		return description;
	}

	@Column(name = "guarantee_name", length = 50)
	public String getGuaranteeName() {
		return guaranteeName;
	}

	@Column(name = "has_guarantee", columnDefinition = "BOOLEAN")
	public Boolean getHasGuarantee() {
		return hasGuarantee;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	@Column(name = "industry", length = 200)
	public String getIndustry() {
		return industry;
	}

	@Column(name = "money")
	public Double getMoney() {
		return money;
	}

	@Column(name = "status", nullable = false, length = 50)
	public String getStatus() {
		return status;
	}

	@Column(name = "apply_time")
	public Date getApplyTime() {
		return applyTime;
	}

	@Column(name = "type", length = 50)
	public String getType() {
		return type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	public User getUser() {
		return user;
	}

	@Column(name = "paymentFrom", length = 1000)
	public String getPaymentFrom() {
		return paymentFrom;
	}

	@Column(name = "video_id")
	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGuaranteeName(String guaranteeName) {
		this.guaranteeName = guaranteeName;
	}

	public void setHasGuarantee(Boolean hasGuarantee) {
		this.hasGuarantee = hasGuarantee;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setPaymentFrom(String paymentFrom) {
		this.paymentFrom = paymentFrom;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "applyEnterpriseLoan")
	public List<ApplyEnterpriseLoanExtension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<ApplyEnterpriseLoanExtension> extensions) {
		this.extensions = extensions;
	}

}
