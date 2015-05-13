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
@Table(name = "ref_grade_pft_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class ReferGradeProfitUser implements java.io.Serializable {

	// Fields
	private String id;

	private String referrerid;

	private String referrername;

	private Integer grade;

	private Double profitrate;

	private Date   inputdate ;

	private Date  updatetime;

	private User user;

	/** default constructor */
	public ReferGradeProfitUser() {
	}

	public ReferGradeProfitUser(String referrerid) {
		this.referrerid = referrerid;

	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "referrer_id" ,nullable = false)
	public String getReferrerid() {
		return referrerid;
	}

	public void setReferrerid(String referrerid) {
		this.referrerid = referrerid;
	}
	@Column(name = "referrer_name",nullable = false)
	public String getReferrername() {
		return referrername;
	}

	public void setReferrername(String referrername) {
		this.referrername = referrername;
	}

	@Column(name = "grade")
	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@Column(name = "profitrate", precision = 22, scale = 0)
	public Double getProfitrate() {
		return profitrate;
	}

	public void setProfitrate(Double profitrate) {
		this.profitrate = profitrate;
	}
	@Column(name = "inputdate",length = 10)
	public Date getInputdate() {
		return inputdate;
	}

	public void setInputdate(Date inputdate) {
		this.inputdate = inputdate;
	}
	@Column(name = "updatetime",length = 19)
	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
}