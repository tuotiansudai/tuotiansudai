package com.esoft.archer.theme.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.theme.ThemeConstants;
import com.esoft.archer.theme.model.Theme;
import com.esoft.archer.theme.service.ThemeService;

@Service
@Transactional
public class ThemeServiceImpl implements ThemeService {

	@Resource
	private HibernateTemplate ht ;
	
	@Transactional(readOnly=false)
	public boolean setDefaultTheme(String themeId) {
		if(StringUtils.isEmpty(themeId)){
			return false ;
		}
		ht.bulkUpdate("update Theme set status = ? where status = ?",
				ThemeConstants.ThemeStatus.ENABLE,ThemeConstants.ThemeStatus.DEFAULT);
		Theme theme = ht.get(Theme.class, themeId);
		theme.setStatus(ThemeConstants.ThemeStatus.DEFAULT);
		ht.update(theme);
		return true;
	}

}
