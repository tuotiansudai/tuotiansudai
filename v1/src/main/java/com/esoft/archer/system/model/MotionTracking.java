package com.esoft.archer.system.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 动作追踪，用于追踪一个动作的来源，过程，去向
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-5-14 上午9:36:28
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-5-14 wangzhi 1.0
 */
@Entity
@Table(name = "motion_tracking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class MotionTracking {
	private String id;

	// 产生动作者
	private String who;
	// 产生动作者类型
	private String whoType;

	// 来自
	private String fromWhere;
	// 来的类型
	private String fromType;
	// 来的时间
	private Date fromTime;
	// 动作
	private String action;
	// 动作类型
	private String actionType;
	// 动作时间
	private Date actionTime;
	// 目的
	private String toWhere;
	// 目的类型
	private String toType;
	// 目的时间
	private Date toTime;

	@Column(name = "action", length = 512)
	public String getAction() {
		return action;
	}

	@Column(name = "action_time")
	public Date getActionTime() {
		return actionTime;
	}

	@Column(name = "action_type", length = 512)
	public String getActionType() {
		return actionType;
	}

	@Column(name = "from_where", length = 512)
	public String getFromWhere() {
		return fromWhere;
	}

	@Column(name = "from_time")
	public Date getFromTime() {
		return fromTime;
	}

	@Column(name = "from_type", length = 512)
	public String getFromType() {
		return fromType;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 64)
	public String getId() {
		return id;
	}

	@Column(name = "to_where", length = 512)
	public String getToWhere() {
		return toWhere;
	}

	@Column(name = "to_time")
	public Date getToTime() {
		return toTime;
	}

	@Column(name = "to_type", length = 512)
	public String getToType() {
		return toType;
	}

	@Column(name = "who", length = 512)
	public String getWho() {
		return who;
	}

	@Column(name = "who_type", length = 512)
	public String getWhoType() {
		return whoType;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public void setFromWhere(String fromWhere) {
		this.fromWhere = fromWhere;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setToWhere(String toWhere) {
		this.toWhere = toWhere;
	}

	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}

	public void setToType(String toType) {
		this.toType = toType;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public void setWhoType(String whoType) {
		this.whoType = whoType;
	}
}
