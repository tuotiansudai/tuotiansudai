package com.esoft.archer.message.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esoft.archer.user.model.User;

/**
 * 发信箱
 * @author yinjl
 *
 */
@Entity
@Table(name = "sendbox")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
@NamedQueries({
	@NamedQuery(name = "sendBox.finsendBoxByLoginUser", 
	        query = "from SendBox where sender.id = ? order by sendTime desc") })
public class SendBox {

	private String id;
	/**
	 * 收信人
	 */
	private User receiver;
	/**
	 * 发信人
	 */
	private User sender;
	/**
	 * 写信标题
	 */
	private String title;
	/**
	 * 写信内容
	 */
	private String content;
	/**
	 * 写信状态
	 */
	private String status;
	/**
	 * 写信时间
	 */
	private Date sendTime;
	/**
	 * 回复收信id
	 */
	private String inBoxId;
	/**
	 * 是否删除
	 */
	private String isdelete;
	
	

	public SendBox() {
	}

	public SendBox(String id, User receiver, User sender, String title,
			String content, String status, Date sendTime, String inBoxId,
			String isdelete) {
		this.id = id;
		this.receiver = receiver;
		this.sender = sender;
		this.title = title;
		this.content = content;
		this.status = status;
		this.sendTime = sendTime;
		this.inBoxId = inBoxId;
		this.isdelete = isdelete;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver")
	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender")
	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "send_time")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Column(name = "inbox_id", length = 32)
	public String getInBoxId() {
		return inBoxId;
	}

	public void setInBoxId(String inBoxId) {
		this.inBoxId = inBoxId;
	}

	@Column(name = "isdelete")
	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}
	
	
}
