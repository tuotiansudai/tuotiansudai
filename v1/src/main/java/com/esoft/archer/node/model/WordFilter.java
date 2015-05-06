
package com.esoft.archer.node.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * WordFilter entity. 敏感词过滤
 */
@Entity
@Table(name = "word_filter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
@NamedQueries(@NamedQuery(name = "WordFilter.findAllWordFilters", query = "from WordFilter"))
public class WordFilter implements java.io.Serializable {

	// Fields

	private String id;
	private String word;
	private String description;

	// Constructors

	/** default constructor */
	public WordFilter() {
	}

	/** minimal constructor */
	public WordFilter(String id) {
		this.id = id;
	}

	/** full constructor */
	public WordFilter(String id, String word, String description) {
		this.id = id;
		this.word = word;
		this.description = description;
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

	@Column(name = "word", length = 50)
	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
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