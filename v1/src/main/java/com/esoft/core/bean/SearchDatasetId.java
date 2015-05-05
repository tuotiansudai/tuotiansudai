package com.esoft.core.bean;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SearchDatasetId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class SearchDatasetId implements java.io.Serializable {

	// Fields

	private String id;
	private String type;
	private String data;

	// Constructors

	/** default constructor */
	public SearchDatasetId() {
	}

	/** full constructor */
	public SearchDatasetId(String id, String type, String data) {
		this.id = id;
		this.type = type;
		this.data = data;
	}

	// Property accessors

	@Column(name = "ID", length = 10)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TYPE", length = 10)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "DATA", length = 10)
	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SearchDatasetId))
			return false;
		SearchDatasetId castOther = (SearchDatasetId) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null
				&& castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getType() == castOther.getType()) || (this.getType() != null
						&& castOther.getType() != null && this.getType()
						.equals(castOther.getType())))
				&& ((this.getData() == castOther.getData()) || (this.getData() != null
						&& castOther.getData() != null && this.getData()
						.equals(castOther.getData())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result
				+ (getType() == null ? 0 : this.getType().hashCode());
		result = 37 * result
				+ (getData() == null ? 0 : this.getData().hashCode());
		return result;
	}

}