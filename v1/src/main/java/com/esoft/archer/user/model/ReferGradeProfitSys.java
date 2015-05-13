package com.esoft.archer.user.model;

// default package

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * ReferGradeProfitUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ref_grade_pft_sys")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class ReferGradeProfitSys implements java.io.Serializable {

	// Fields
	private String id;

	private Integer grade;

	private Double profitrate;

	private Date   inputdate ;

	private Date  updatetime;

	private User user;

	/** default constructor */
	public ReferGradeProfitSys() {
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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