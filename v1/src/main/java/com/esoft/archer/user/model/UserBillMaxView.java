package com.esoft.archer.user.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

/**
 * 借款者账户
 */
@Entity
@Subselect("select user_id, max(seq_num) as max_seq_num from user_bill group by user_id")
@Synchronize({ "user_bill" })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserBillMaxView implements java.io.Serializable {

	private String userId;

	private int maxSeqNum;

	public UserBillMaxView() {
	}

	@Id
	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "max_seq_num")
	public int getMaxSeqNum() {
		return maxSeqNum;
	}

	public void setMaxSeqNum(int maxSeqNum) {
		this.maxSeqNum = maxSeqNum;
	}

}