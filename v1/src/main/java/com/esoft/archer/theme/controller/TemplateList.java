package com.esoft.archer.theme.controller;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.theme.ThemeConstants;
import com.esoft.archer.theme.model.Template;
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
@Scope(ScopeType.VIEW)
public class TemplateList extends EntityQuery<Template> implements java.io.Serializable{
	
	private static final long serialVersionUID = -3158336515691959685L;
	static StringManager sm = StringManager.getManager(ThemeConstants.Package);
	@Logger static Log log ;
	
	public TemplateList(){
		final String[] RESTRICTIONS = {"id like #{templateList.example.id}",
				"name like #{templateList.example.name}",
				"template.id = #{templateList.example.theme.id}",};
				
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		
	}
	
	@Override
	protected void initExample() {
		Template template = new Template();
		template.setTheme(new Theme());
		setExample(template);
	}
	
}
