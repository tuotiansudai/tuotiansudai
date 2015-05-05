package com.esoft.archer.theme.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RegionComponentId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class RegionComponentId implements java.io.Serializable {

	// Fields

	private String componentId;
	private String regionId;

	// Constructors

	/** default constructor */
	public RegionComponentId() {
	}

	/** full constructor */
	public RegionComponentId(String componentId, String regionId) {
		this.componentId = componentId;
		this.regionId = regionId;
	}

	// Property accessors

	@Column(name = "component_id", nullable = false, length = 32)
	public String getComponentId() {
		return this.componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	@Column(name = "region_id", nullable = false, length = 32)
	public String getRegionId() {
		return this.regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RegionComponentId))
			return false;
		RegionComponentId castOther = (RegionComponentId) other;

		return ((this.getComponentId() == castOther.getComponentId()) || (this
				.getComponentId() != null && castOther.getComponentId() != null && this
				.getComponentId().equals(castOther.getComponentId())))
				&& ((this.getRegionId() == castOther.getRegionId()) || (this
						.getRegionId() != null
						&& castOther.getRegionId() != null && this
						.getRegionId().equals(castOther.getRegionId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getComponentId() == null ? 0 : this.getComponentId()
						.hashCode());
		result = 37 * result
				+ (getRegionId() == null ? 0 : this.getRegionId().hashCode());
		return result;
	}

}