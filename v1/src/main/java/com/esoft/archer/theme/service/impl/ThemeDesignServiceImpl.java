package com.esoft.archer.theme.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.theme.ThemeConstants;
import com.esoft.archer.theme.model.Component;
import com.esoft.archer.theme.model.RegionComponent;
import com.esoft.archer.theme.model.RegionComponentId;
import com.esoft.archer.theme.service.ThemeDesignService;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.StringManager;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class ThemeDesignServiceImpl implements ThemeDesignService {

	@Resource
	private HibernateTemplate ht ;
	
	static StringManager sm = StringManager.getManager(ThemeConstants.Package);
	@Logger static Log log ;

	public void add2Region(String componentId, String regionId, String tplPath,
			String regionPath) throws IOException, TemplateException {
		RegionComponentId id = new RegionComponentId();
		id.setComponentId(componentId);
		id.setRegionId(regionId);
		RegionComponent regionComponent = ht.get(RegionComponent.class, id);
		if(log.isDebugEnabled()){
			log.debug(sm.getString("log.add2Region", componentId,regionId));
		}
		if( regionComponent != null ){
			return ;
		}else{
			regionComponent = new RegionComponent();
			regionComponent.setId(id);
			ht.save(regionComponent);
			
			//生成模板
			generateRegion(regionId,tplPath,regionPath);
		}
	}

	/**
	 * Region 1-* components 1-* componentParameters
	 * @param tplPath
	 * @param regoinPath
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void generateRegion(String regionId ,String tplPath,
			String regoinPath) throws IOException, TemplateException{
		if(log.isDebugEnabled()){
			log.debug(sm.getString("log.generateRegionTplStart"));
		}
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(tplPath).getParentFile());
		
		Template t = cfg.getTemplate(new File(tplPath).getName());
		//查询components
		Map<String, List<Component>> map = new HashMap<String, List<Component>>();
//		List<Component> results = ht.findByNamedQuery("Component.findComponentByRegionId",regionId);
		@SuppressWarnings("unchecked")
		/*List<Component> results = ht.find("Select c from Component c, RegionComponent rc" +
				" where c in elements(rc.component) and rc.region.id=?" +
				" order by rc.seqNum", regionId);*/
		List<Component> results = ht.find("Select rc.component from RegionComponent rc" +
				" where rc.region.id=?" +
				" order by rc.seqNum", regionId);
		map.put("components",results );
		
		Writer out = new FileWriter(new File(regoinPath + "/"+regionId+".xhtml"));
		t.process(map, out);
	}
	
}
