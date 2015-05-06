package com.esoft.core.bean;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SearchDataset entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "search_dataset")
public class SearchDataset implements java.io.Serializable {

	// Fields

	private SearchDatasetId id;

	// Constructors

	/** default constructor */
	public SearchDataset() {
	}

	/** full constructor */
	public SearchDataset(SearchDatasetId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "id", column = @Column(name = "ID", length = 10)),
			@AttributeOverride(name = "type", column = @Column(name = "TYPE", length = 10)),
			@AttributeOverride(name = "data", column = @Column(name = "DATA", length = 10)) })
	public SearchDatasetId getId() {
		return this.id;
	}

	public void setId(SearchDatasetId id) {
		this.id = id;
	}

}