package com.esoft.core.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * UploadCategory entity. 
 * 上传文件的分类
 */
@Entity
@Table(name = "upload_category")
public class UploadCategory implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String description;
	private Set<Upload> uploads = new HashSet<Upload>(0);

	// Constructors

	/** default constructor */
	public UploadCategory() {
	}

	/** minimal constructor */
	public UploadCategory(String id) {
		this.id = id;
	}

	/** full constructor */
	public UploadCategory(String id, String name, String description,
			Set<Upload> uploads) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.uploads = uploads;
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

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "uploadCategory")
	public Set<Upload> getUploads() {
		return this.uploads;
	}

	public void setUploads(Set<Upload> uploads) {
		this.uploads = uploads;
	}

}