package com.esoft.jdp2p.loan.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esoft.archer.user.model.User;

@Entity
@Table(name = "attention_loan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AttentionLoan {

	private String id;
	private User user;
	private Loan loan;
	private Date attentionTime;

	public AttentionLoan() {

	}

	public AttentionLoan(String id, User user, Loan loan, Date attentionTime) {
		this.id = id;
		this.user = user;
		this.loan = loan;
		this.attentionTime = attentionTime;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_id")
	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	@Column(name = "attentiontime")
	public Date getAttentionTime() {
		return attentionTime;
	}

	public void setAttentionTime(Date attentionTime) {
		this.attentionTime = attentionTime;
	}

}
