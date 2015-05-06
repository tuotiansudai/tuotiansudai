package com.esoft.archer.user.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用户积分历史
 */
@Entity
@Table(name = "user_point_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserPointHistory implements java.io.Serializable {

	// Fields

	private String id;

	private User user;
	/**
	 * 使用属性time，尽量使用setTimeDate/getTimeDate这两个方法
	 */
	private Date time;
	// 增加还是扣除积分
	private String operateType;

	private String type;
	private String typeInfo;
	private String remark;
	private int point;

	public UserPointHistory() {
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return 为了使time属性在系统中是方便添加的方法
	 */
	@Transient
	// 禁止hibernate
	public Date getTimeDate() {
		return this.time;
	}

	public void setTimeDate(Date time) {
		this.time = time;
	}

	// 跟数据库的类型一致,主要为了解决精确到毫秒问题
	@Column(name = "time", nullable = false)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "operate_type", nullable = false, length = 50)
	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	@Column(name = "type", nullable = false, length = 100)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "type_info", columnDefinition = "CLOB")
	public String getTypeInfo() {
		return typeInfo;
	}

	public void setTypeInfo(String typeInfo) {
		this.typeInfo = typeInfo;
	}

	@Column(name = "point", nullable = false)
	public int getPoint() {
		return point;
	}

	@Lob
	@Column(name = "remark", columnDefinition = "CLOB")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setPoint(int point) {
		this.point = point;
	}

}