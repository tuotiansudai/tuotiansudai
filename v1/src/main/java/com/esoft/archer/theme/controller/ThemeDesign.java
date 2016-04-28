package com.esoft.archer.theme.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.theme.ThemeConstants;
import com.esoft.archer.theme.model.Region;
import com.esoft.archer.theme.service.ThemeDesignService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

import freemarker.template.TemplateException;

/**
 * 1；查询系统所以主题，默认选中当前使用<br/>
 * 2；查询默认主题下的所有模板，前台以下拉框的方式展示，或者以按钮的方式展示<br/>
 * 3；展示当前模板下所有部位，以子表格（subTable）的形式展现模板下的元件<br/>
 * 4；在部位表格下方，展示所有元件，并在元件后方以下拉框的形式放置 当前模板下的所有部位，通过出发onchange事件
 * 添加到部位中<br/>
 * 5；允许部位中对元件的展示顺序进行排序
 */
@Component
@Scope(ScopeType.REQUEST)
public class ThemeDesign {
	
	private final static String REGION_TEMPLATE_PATH = 
		FacesUtil.getRealPath("/site/templates/region.xhtml.ftl");
	
	static StringManager sm = StringManager.getManager(ThemeConstants.Package);
	
	@Resource
	HibernateTemplate ht ;
	@Resource
	ThemeDesignService themeDesignService;
	
	@Logger
	Log log ;
	
	private String regionId ;
	private String componentId;
	
	private Region selectedRegion ;
	
	private List<Region> regions ;
	
	public List<Region> getRegions(){
		
		return ht.findByNamedQuery("Region.findRegionByTemplateId", "1");
	}
	
	public void add2Region(ValueChangeEvent e){
		final String regionId = (String) e.getNewValue();
		log.debug("regionId=" + regionId);
		log.debug("regionId=" + componentId);
	}
	
	/**
	 * 添加组件到部位，regionId 通过注入获取，即，getRegionId 方法，componentId通过参数获取
	 */
	public String add2Region(){
		if(log.isDebugEnabled()){
			log.debug("add 2 region .regionId=" + regionId);
			log.debug("componentId=" + FacesUtil.getParameter("componentId"));
		}
		try {
			themeDesignService.add2Region(
					FacesUtil.getParameter("componentId"), regionId,REGION_TEMPLATE_PATH,
					FacesUtil.getRealPath("/site/themes/shandun/region/"));
			//FIXME : 模板路径应该由系统自动获取
		} catch (IOException e) {
			FacesUtil.addErrorMessage(sm.getString("log.generateTplIOExceptoin"));
			log.error(e.getLocalizedMessage(), e);
			e.printStackTrace();
		} catch (TemplateException e) {
			FacesUtil.addErrorMessage(sm.getString("log.generateTplError"));
			log.error(e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
		
		return FacesUtil.redirect(ThemeConstants.View.THEME_DESIGN);
	}
	
	public void deleteFromRegion(){
		
		if(log.isDebugEnabled())
			log.debug("remove...");
		
		
	}
	
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setSelectedRegion(Region selectedRegion) {
		this.selectedRegion = selectedRegion;
	}

	public Region getSelectedRegion() {
		return selectedRegion;
	}
	
}
