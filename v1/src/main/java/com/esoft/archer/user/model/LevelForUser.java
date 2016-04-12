package com.esoft.archer.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 等级，给用户使用
 */
@Entity
@Table(name = "level_for_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class LevelForUser implements java.io.Serializable {

	// Fields

	private String id;
	private String name;

	/**
	 * 序号（越大，级别越高）
	 */
	private int seqNum;
	/**
	 * 积分下线（达到此下线，才能获得该等级） 0或者null，视为此项无限制
	 */
	private Integer minPointLimit;
	/**
	 * 每个等级对应多少积分值
	 */
	private int pointValue;
	/**
	 * 有效期（秒）
	 */
	private int validityPeriod;
	private String description;
	private List<User> users = new ArrayList<User>(0);

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "min_point_limit")
	public Integer getMinPointLimit() {
		return minPointLimit;
	}

	public void setMinPointLimit(Integer minPointLimit) {
		this.minPointLimit = minPointLimit;
	}

	@Lob
	@Column(name = "description", columnDefinition = "CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "level")
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Column(name = "point_value")
	public int getPointValue() {
		return pointValue;
	}

	public void setPointValue(int pointValue) {
		this.pointValue = pointValue;
	}

	@Column(name = "validity_period")
	public int getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	@Column(name = "seq_num")
	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

}