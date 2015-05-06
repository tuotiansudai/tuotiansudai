package com.esoft.archer.theme.service;

import java.io.IOException;

import freemarker.template.TemplateException;

public interface ThemeDesignService {
	
	/**
	 * 添加元件到部位
	 * @param componentId
	 * @param regionId
	 * @param tplPath 模板文件路径
	 * @param regoinPath 部位路径，到目录即可，大多数情况下目录格式应该为 /app/site/themes/xxx/region
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public void add2Region(String componentId,String regionId,String tplPath,String regoinPath) throws IOException, TemplateException;
}
