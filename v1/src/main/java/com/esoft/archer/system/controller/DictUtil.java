package com.esoft.archer.system.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.system.model.Dict;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.SpringBeanUtil;


@Component
@Scope(ScopeType.REQUEST)
public class DictUtil implements java.io.Serializable{
	
	@Logger
	private static Log log ;
	
//	@Resource
	private static HibernateTemplate ht ;
	
	
	
	/**
	 * 通过编号获取该编号对应的名称，此处是父key为空的情况下
	 * @param id
	 * @return
	 */
	public static String getValue(String key){
		return getValue(null,key);
	}
	
	/**
	 * 通过数据字典的 父编码和子编码获取对应的名称
	 * @param parentKey
	 * @param key
	 * @return
	 */
	public static String getValue(String parentKey ,String key){
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		if(parentKey == null){
			parentKey = "COMMON";
		}
		
		String hql = "from Dict where parent.key = ? and key = ?";
		List<Dict> dicts = getHt().find(hql,parentKey,key);
		if(dicts.size() > 1){
			log.error("有多个相同的key存在数据库中");
			return "ERROR[多个相同的KEY]";
		}else if(dicts.size() < 1){
			return parentKey +"."+key + " not found";
		}else{
			return dicts.get(0).getValue();
		}
	}
	
	public static Dict getDict(String id){
		return getHt().get(Dict.class, id);
	}
	
	public static List<Dict> getDictByParentKey(String parentKey){
		return getHt().find("from Dict where parent.key = ? order by seqNum",parentKey);
	}
	
	private static HibernateTemplate getHt(){
		if(ht == null){
			ht = (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");
		}
		return ht ;
	}
	
	
}
