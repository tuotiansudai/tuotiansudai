package com.esoft.archer.user.service;

import java.util.List;

import javax.faces.model.SelectItem;

import com.esoft.archer.user.model.Area;

public interface AreaService {

	List<Area> getAllProvinces();

	List<Area> findAllCities(String provinceId);

	List<Area> findAllCounties(String cityId);
	
    Area findProvincebyCity(Area area);
    
    public Area getAreaById(String id);

}
