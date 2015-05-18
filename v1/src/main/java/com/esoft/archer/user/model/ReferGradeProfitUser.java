package com.esoft.archer.user.model;

// default package
import java.util.Date;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * ReferGradeProfitUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user_refer_grade_profitrate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class ReferGradeProfitUser implements java.io.Serializable {

	// Fields
	private String id;

	private User referrer;

	private String referrerName;

	private Integer grade;

	private Double profitRate;

	private Date   inputDate ;

	private Date  updateTime;



	/** default constructor */
	public ReferGradeProfitUser() {
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(name = "referrer_name",nullable = false)

	public String getReferrerName() {
		return referrerName;
	}

	public void setReferrerName(String referrerName) {
		this.referrerName = referrerName;
	}

	@Column(name = "grade")
	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@Column(name = "profitrate", precision = 22, scale = 0)
	public Double getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(Double profitRate) {
		this.profitRate = profitRate;
	}
	@Column(name = "inputdate",length = 10)
	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	@Column(name = "updatetime",length = 19)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "referrer_id", nullable = false)
	public User getReferrer() {
		return referrer;
	}

	public void setReferrer(User referrer) {
		this.referrer = referrer;
	}
}