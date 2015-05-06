package com.esoft.archer.common.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;
import org.hibernate.ObjectNotFoundException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.common.exception.InputRuleMatchingException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.common.service.ValidationService;
import com.esoft.archer.config.model.Config;
import com.esoft.core.annotations.ConverterId;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-9 上午11:47:38
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-9 wangzhi 1.0
 */
@Service("validationService")
public class ValidationServiceImpl implements ValidationService {

	@Resource
	HibernateTemplate ht;

	private static final String FIND_EXISTS_HQL = "select {0} from {1} {0} where {0}.{2}=?";
	private static final String EQUALS_PERSISTENCE_VALUE_HQL = "select {0} from {1} {0} where {0}.{2}=? and {0}.{3}=?";

	@Override
	public boolean isAlreadExist(String entityClass, String fieldName,
			Object value) throws SecurityException, ClassNotFoundException,
			NoSuchMethodException {
		List objs = findAlreadExists(entityClass, fieldName, value);
		if (objs.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isAlreadyExist(Class entityClass, String fieldName,
			Object value) throws SecurityException, NoSuchMethodException {
		// 判断field是否正确
		entityClass.getMethod("get" + StringUtils.capitalize(fieldName),
				new Class[0]);

		String entityClassName = entityClass.getSimpleName();
		String entityAlias = StringUtils.uncapitalize(entityClassName);
		String hql = MessageFormat.format(FIND_EXISTS_HQL, entityAlias,
				entityClassName, fieldName);

		List objs = ht.find(hql, value);
		if (objs.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List findAlreadExists(String entityClass, String fieldName,
			Object value) throws ClassNotFoundException, SecurityException,
			NoSuchMethodException {
		Class clazz = Class.forName(entityClass);
		// 判断field是否正确
		clazz.getMethod("get" + StringUtils.capitalize(fieldName), new Class[0]);

		String entityClassName = clazz.getSimpleName();
		String entityAlias = StringUtils.uncapitalize(entityClassName);
		String hql = MessageFormat.format(FIND_EXISTS_HQL, entityAlias,
				entityClassName, fieldName);

		return ht.find(hql, value);
	}

	@Override
	public boolean equalsPersistenceValue(String entityClass, String fieldName,
			String id, String value) throws ClassNotFoundException,
			SecurityException, NoSuchMethodException {
		Class clazz = Class.forName(entityClass);
		// 判断field是否正确
		clazz.getMethod("get" + StringUtils.capitalize(fieldName), new Class[0]);

		String entityClassName = clazz.getSimpleName();
		String entityAlias = StringUtils.uncapitalize(entityClassName);
		String idFieldName = getAnnotadedWithId(clazz);
		String hql = MessageFormat.format(EQUALS_PERSISTENCE_VALUE_HQL,
				entityAlias, entityClassName, idFieldName, fieldName);

		List objs = ht.find(hql, new String[] { id, value });
		if (objs.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean inputRuleValidation(String ruleId, String value)
			throws NoMatchingObjectsException, InputRuleMatchingException {
//		ruleId = "input_"+ruleId;
		if (StringUtils.isEmpty(value)) {
			//输入框是否为空，有requried="true"来判断，所以此处一律返回true
			return true;
		}
		Config rule = ht.get(Config.class, ruleId);
		if (rule == null) {
			// ruleId未找到
			throw new NoMatchingObjectsException(Config.class, "ruleId:"
					+ ruleId);
		}
		String regex = rule.getValue();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		if (!matcher.matches()) {
			// 验证失败
			throw new InputRuleMatchingException(rule.getDescription());
		}
		return true;
	}

	private String getAnnotadedWithId(Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();
		try {
			for (Field field : fields) {
				if (field.isAnnotationPresent(Id.class)
						|| field.isAnnotationPresent(ConverterId.class)) {
					field.setAccessible(true);
					return field.getName();
				}
			}
			for (Method method : methods) {
				if (method.isAnnotationPresent(Id.class)
						|| method.isAnnotationPresent(ConverterId.class)) {
					String name = method.getName();
					name = StringUtils.uncapitalize(name.substring(3,
							name.length()));
					return name;
				}
			}
			if (clazz.getSuperclass() != null
					&& !clazz.getSuperclass().equals(Object.class)) {
				return getAnnotadedWithId(clazz.getSuperclass());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
