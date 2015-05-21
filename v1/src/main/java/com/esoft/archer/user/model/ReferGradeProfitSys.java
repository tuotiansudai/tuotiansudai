package com.esoft.archer.user.model;

// default package

import com.esoft.archer.user.service.ReferGradePtSysService;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * ReferGradeProfitUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "globle_refer_grade_profitrate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class ReferGradeProfitSys implements java.io.Serializable {

	// Fields
	private String id;

	private Integer grade;

	private Double profitRate;

	private Date   inputDate ;

	private Date  updateTime;

	private String gradeRole;

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
//
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
	@Column(name = "grade_role",length = 20)
	public String getGradeRole() {
		return gradeRole;
	}

	public void setGradeRole(String gradeRole) {
		this.gradeRole = gradeRole;
	}

	@Column(name = "updatetime",length = 19)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}