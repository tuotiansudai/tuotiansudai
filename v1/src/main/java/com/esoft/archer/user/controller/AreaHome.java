package com.esoft.archer.user.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.user.model.Area;
import com.esoft.archer.user.service.AreaService;
import com.esoft.core.annotations.ScopeType;

@Component
@Scope(ScopeType.VIEW)
public class AreaHome extends EntityHome<Area> implements Serializable {
	private static final long serialVersionUID = -9028139079103290282L;

	@Resource
	private AreaService areaService;

	private Area selectedProvince;
	private Area selectedCity;
	private Area selectedCounty;

	private List<Area> allProvinces;
	private List<Area> allCities;
	private List<Area> allCounties;

	public void findAllCities() {
		this.allCounties = null;
		this.selectedCounty = null;
		if (this.selectedProvince == null) {
			this.allCities = null;
		} else {
			this.allCities = areaService.findAllCities(this.selectedProvince
					.getId());
			if (this.allCities.size() > 0) {
				this.selectedCity = allCities.get(0);
				findAllCounties();
			}
		}
	}

	public void findAllCounties() {
		this.selectedCounty = null;
		if (this.selectedCity == null){
			this.allCounties = null;
		} else {
			this.allCounties = areaService.findAllCounties(this.selectedCity
					.getId());
		}
	}

	public List<Area> getAllCities() {
		return allCities;
	}

	public List<Area> getAllCounties() {
		return allCounties;
	}

	public List<Area> getAllProvinces() {
		if (this.allProvinces == null) {
			this.allProvinces = areaService.getAllProvinces();
			if (this.selectedProvince == null) {
				this.selectedProvince = allProvinces.get(0);
			}
			findAllCities();
			this.selectedCity = this.allCities.get(0);
			findAllCounties();
		}
		return this.allProvinces;
	}

	public Area getSelectedCity() {
		return selectedCity;
	}

	public Area getSelectedCounty() {
		return selectedCounty;
	}

	public Area getSelectedProvince() {
		return selectedProvince;
	}

	public void initByCounty(Area area) {
		if (area != null) {
			this.selectedCounty = area;
			this.selectedCity = getBaseService().get(Area.class,
					area.getParent().getId());
			this.allCounties = areaService.findAllCounties(this
					.getSelectedCity().getId());
			this.selectedProvince = getBaseService().get(Area.class,
					selectedCity.getParent().getId());
			this.allCities = areaService.findAllCities(this
					.getSelectedProvince().getId());
		}
	}

	public void initByCity(Area area) {
		if (area != null) {
			this.selectedCity = area;
			this.allCounties = areaService.findAllCounties(this
					.getSelectedCity().getId());
			this.selectedProvince = getBaseService().get(Area.class,
					selectedCity.getParent().getId());
			this.allCities = areaService.findAllCities(this
					.getSelectedProvince().getId());
		}
	}

	/**
	 * 通过城市名字初始化数据
	 * @param area
	 */
	public void initByCityName(String area) {
		if (StringUtils.isNotEmpty(area)) {
			List<Area> areas = getBaseService().find(
					"from Area area where area.name=?", area);
			if (areas.size() > 0) {
				this.selectedCity = areas.get(0);
				this.allCounties = areaService.findAllCounties(this
						.getSelectedCity().getId());
				this.selectedProvince = getBaseService().get(Area.class,
						selectedCity.getParent().getId());
				this.allCities = areaService.findAllCities(this
						.getSelectedProvince().getId());
			}
		}
	}

	public void setAllCities(List<Area> allCities) {
		this.allCities = allCities;
	}

	public void setAllCounties(List<Area> allCounties) {
		this.allCounties = allCounties;
	}

	public void setAllProvinces(List<Area> allProvinces) {
		this.allProvinces = allProvinces;
	}

	public void setSelectedCity(Area selectedCity) {
		this.selectedCity = selectedCity;
	}

	public void setSelectedCounty(Area selectedCounty) {
		this.selectedCounty = selectedCounty;
	}

	public void setSelectedProvince(Area selectedProvince) {
		this.selectedProvince = selectedProvince;
	}

}
