package com.esoft.archer.system.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 实体统计
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-5-14 上午9:36:28
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-5-14 wangzhi 1.0
 */
public class EntityGroupBy {
	private Class entityClazz;
	private String groupByField;
	private String groupByValue;
	private int amount;
	private List<Object> tempValues = new ArrayList<Object>(0);
	
	public EntityGroupBy(Class entityClazz, String groupByField,
			String groupByValue, int amount, List<Object> tempValues) {
		super();
		this.entityClazz = entityClazz;
		this.groupByField = groupByField;
		this.groupByValue = groupByValue;
		this.amount = amount;
		this.tempValues = tempValues;
	}
	
	public EntityGroupBy(String entityClazz, String groupByField,
			String groupByValue, int amount, List<Object> tempValues) throws ClassNotFoundException {
		super();
		this.entityClazz = Class.forName(entityClazz);
		this.groupByField = groupByField;
		this.groupByValue = groupByValue;
		this.amount = amount;
		this.tempValues = tempValues;
	}

	public Class getEntityClazz() {
		return entityClazz;
	}

	public void setEntityClazz(Class entityClazz) {
		this.entityClazz = entityClazz;
	}

	public String getGroupByField() {
		return groupByField;
	}

	public void setGroupByField(String groupByField) {
		this.groupByField = groupByField;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public List<Object> getTempValues() {
		return tempValues;
	}

	public void setTempValues(List<Object> tempValues) {
		this.tempValues = tempValues;
	}

	public String getGroupByValue() {
		return groupByValue;
	}

	public void setGroupByValue(String groupByValue) {
		this.groupByValue = groupByValue;
	}
	
	
}
