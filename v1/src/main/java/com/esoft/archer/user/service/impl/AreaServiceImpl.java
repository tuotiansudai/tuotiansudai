package com.esoft.archer.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.common.service.impl.BaseServiceImpl;
import com.esoft.archer.user.model.Area;
import com.esoft.archer.user.service.AreaService;

@Service(value = "areaService")
@SuppressWarnings("unchecked")
public class AreaServiceImpl extends BaseServiceImpl<Area> implements
		AreaService {

	@Resource
	private HibernateTemplate ht;

	public List<Area> getAllProvinces() {
		return ht.find("from Area area where area.parent.id = null");
	}

	public List<Area> findAllCities(String provinceId) {
		//如果是直辖市的话，城市显示区
//		if ("110000".equals(provinceId)) {
//			return ht.find("from Area area where area.parent.id = 110100");
//		} else if ("120000".equals(provinceId)) {
//			return ht.find("from Area area where area.parent.id = 120100");
//		} else if ("500000".equals(provinceId)) {
//			return ht.find("from Area area where area.parent.id = 500100");
//		} else if ("310000".equals(provinceId)){
//			return ht.find("from Area area where area.parent.id = 310100");
//		} else {
			return ht.find("from Area area where area.parent.id = '"
					+ provinceId + "'");
//		}
	}

	public List<Area> findAllCounties(String cityId) {
		return ht.find("from Area area where area.parent.id = '" + cityId + "'");
	}
	/**
	 * 根据城市id去查询省份
	 */
	public Area findProvincebyCity(Area area){
		 Area areaProvince = (Area) ht.find("from Area where id = ?",area.getParent().getId()).get(0);
		 return areaProvince;
	}

	@Override
	public Area getAreaById(String id) {
		return ht.get(Area.class,id);
	}
}
