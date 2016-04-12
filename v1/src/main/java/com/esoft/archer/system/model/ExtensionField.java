package com.esoft.archer.system.model;

// default package

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esoft.archer.banner.model.BannerPicture;
import com.esoft.archer.node.model.NodeAttr;
import com.esoft.archer.user.model.User;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.repay.model.Repay;

/**
 * 扩展字段
 * 
 * @author wangzhi
 */
@Entity
@Table(name = "extension_field")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class ExtensionField implements java.io.Serializable, Cloneable {

	// Fields

	private String id;
	/**
	 * 字段名称
	 */
	private String name;

	/**
	 * 归属于
	 */
	private String belongTo;
	/**
	 * 字段类型
	 */
	private String type;
	/**
	 * 字段说明
	 */
	private String description;

	private Integer seqNum;

	@Column(name = "belong_to", length = 512)
	public String getBelongTo() {
		return belongTo;
	}

	@Column(name = "description", length = 1000)
	public String getDescription() {
		return description;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	@Column(name = "name", length = 1000)
	public String getName() {
		return name;
	}

	@Column(name = "seq_num")
	public Integer getSeqNum() {
		return this.seqNum;
	}

	@Column(name = "type", length = 512)
	public String getType() {
		return type;
	}

	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	public void setType(String type) {
		this.type = type;
	}

}