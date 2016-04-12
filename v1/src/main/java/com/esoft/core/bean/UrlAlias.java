package com.esoft.core.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * UrlAlias entity. 
 * 访问路径别名
 */
@Entity
@Table(name = "url_alias")
public class UrlAlias implements java.io.Serializable {

	// Fields

	private String alias;
	private String original;
	/**
	 * 别名不同，语言不同，原路径相同。
	 */
	private String language;
	private String description;

	// Constructors

	/** default constructor */
	public UrlAlias() {
	}

	/** minimal constructor */
	public UrlAlias(String alias, String original) {
		this.alias = alias;
		this.original = original;
	}

	/** full constructor */
	public UrlAlias(String alias, String original, String language,
			String description) {
		this.alias = alias;
		this.original = original;
		this.language = language;
		this.description = description;
	}

	// Property accessors
	@Id
	@Column(name = "alias", unique = true, nullable = false, length = 100)
	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "original", nullable = false, length = 500)
	public String getOriginal() {
		return this.original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	@Column(name = "language", length = 20)
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}