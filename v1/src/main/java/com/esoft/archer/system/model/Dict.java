package com.esoft.archer.system.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 数据字典，中英文的对应<br/>
 * desc: Dict和parent为联合主键，parent为类型
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "dict")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Dict {
	private String id;
	private String key;
	private String value;
	private String status;
	private Dict parent;
	private int seqNum;
	private String description;
	private List<Dict> children = new ArrayList<Dict>(0);

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
	@OrderBy(value="seqNum")
	public List<Dict> getChildren() {
		return children;
	}

	/**
	 * 码
	 * 
	 * @return
	 */
	@Column(name = "`key`", length = 200)
	public String getKey() {
		return key;
	}

	/**
	 * 描述
	 * 
	 * @return
	 */
	@Lob
	@Column(name = "description", columnDefinition = "CLOB")
	public String getDescription() {
		return description;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	/**
	 * 中文名称
	 * 
	 * @return
	 */
	@Column(name = "value", length = 500)
	public String getValue() {
		return value;
	}
	
	/**
	 * 父
	 * 
	 * @return
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "p_id")
	public Dict getParent() {
		return parent;
	}

	/**
	 * 序号，asc
	 * 
	 * @return
	 */
	@Column(name = "seq_num")
	public int getSeqNum() {
		return seqNum;
	}

	/**
	 * 状态
	 * 
	 * @return
	 */
	@Column(name = "status", length = 100)
	public String getStatus() {
		return status;
	}

	public void setChildren(List<Dict> children) {
		this.children = children;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setParent(Dict parent) {
		this.parent = parent;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
