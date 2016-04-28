package com.esoft.archer.common.service;

import java.util.List;

import com.esoft.archer.common.exception.InputRuleMatchingException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;

/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description: 验证service，验证字符串规则，验证是否已存在等等。
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-1-7 下午3:49:56  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-7      wangzhi      1.0          
 */
public interface ValidationService {

	/**
	 * 该实体的该字段，在数据库记录中，是否已经存在某值。
	 * @param entityClass
	 * @param fieldName
	 * @param value
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public boolean isAlreadExist(String entityClass, String fieldName, Object value) throws SecurityException, ClassNotFoundException, NoSuchMethodException;
	
	/**
	 * 查找所有已存在的值
	 * @param entityClass
	 * @param fieldName
	 * @param value
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public List findAlreadExists(String entityClass, String fieldName, Object value) throws ClassNotFoundException, SecurityException, NoSuchMethodException;

	/**
	 * 该实体的该字段，在数据库记录中，是否已经存在某值。
	 * @param entityClass
	 * @param fieldName
	 * @param value
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	boolean isAlreadyExist(Class entityClass, String fieldName, Object value)
			throws SecurityException, NoSuchMethodException;
	
	/**
	 * 该实体的该字段的值，是否与数据库中相同。
	 * @param entityClass
	 * @param fieldName
	 * @param id 实体对象的id
	 * @param value
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public boolean equalsPersistenceValue(String entityClass, String fieldName,
			String id, String value) throws ClassNotFoundException, SecurityException, NoSuchMethodException;

	/**
	 * 根据输入匹配规则，验证value
	 * @param ruleId 匹配规则id
	 * @param value 输入值
	 * @return
	 * @throws NoMatchingObjectsException 根据ruleId 未找到
	 * @throws InputRuleMatchingException 验证失败，里面包含验证消息
	 */
	public boolean inputRuleValidation(String ruleId, String value)
			throws NoMatchingObjectsException, InputRuleMatchingException;
}
