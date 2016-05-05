package com.esoft.archer.theme.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.theme.ThemeConstants;
import com.esoft.archer.theme.model.Theme;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.StringManager;

/**
 * 菜单类型查询
 * @author wanghm
 *
 */
@Component
@Scope(ScopeType.REQUEST)
public class ThemeList {
	
//	private static final long serialVersionUID = 1697137990909862041L;
	
	static StringManager sm = StringManager.getManager(ThemeConstants.Package);
	@Logger static Log log ;
	
	@Resource
	HibernateTemplate ht ;
	
	public ThemeList(){
		/*final String[] RESTRICTIONS = {"id like #{themeList.example.id}",
				"name like #{themeList.example.name}",
				"screenshotUri like #{themeList.example.screenshotUri}",
				"description like #{themeList.example.description}",
				"templates like #{themeList.example.templates}",
				};
				
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));*/
		
	}
	
	private List<Theme> allThemes ;
	
	@SuppressWarnings("unchecked")
	public List<Theme> getAllThemes(){
		if(allThemes == null){
			allThemes = ht.findByNamedQuery("Theme.findAllOrderByStatus");
		}
		return allThemes;
	}
	
}
