package com.esoft.core.jsf.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.Id;

import org.hibernate.proxy.HibernateProxy;
import org.omnifaces.converter.ValueChangeConverter;

import com.esoft.core.annotations.ConverterId;

@FacesConverter("archer.EntityConverter")
public class EntityConverter implements Converter {

	private static final String ID_PREFIX = "entityConverter_";


	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value != null) {
			return getFromViewMap(context, component, value);
		}
		return null;
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object object) {
		if (object != null && !"".equals(object)) {
			String id;

			id = this.getId(object);
			if (id == null) {
				id = "";
			}
			id = id.trim();
			putInViewMap(id, context, component, object);
			return id;
		}
		return null;
	}

	public void putInViewMap(String id, FacesContext context,
			UIComponent component, Object object) {
		if (object != null) {
			Map objectsFromClass = (Map) context.getViewRoot().getViewMap()
					.get(ID_PREFIX + component.getId());
			if (objectsFromClass == null) {
				objectsFromClass = new HashMap();
				context.getViewRoot().getViewMap()
						.put(ID_PREFIX + component.getId(), objectsFromClass);
			}
			objectsFromClass.put(id, object);
		}
	}

	public Object getFromViewMap(FacesContext context, UIComponent component,
			String value) {
		if (value != null && !value.trim().isEmpty()) {
			Map objectsFromClass = (Map) context.getViewRoot().getViewMap()
					.get(ID_PREFIX + component.getId());
			if (objectsFromClass != null) {
				return objectsFromClass.get(value);
			}
		}
		return null;
	}

	/**
	 * Get object ID
	 * 
	 * @param Object
	 *            object
	 * 
	 * @return String
	 */
	public String getId(Object object) {
		try {
			if (object instanceof HibernateProxy) {
				return ((HibernateProxy) object).getHibernateLazyInitializer()
						.getIdentifier().toString();
			}
			Object id = getAnnotadedWithId(object);
			if (id != null) {
				return id.toString();
			} else {
				return "";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Object getAnnotadedWithId(Object object) {
		return getAnnotadedWithId(object, object.getClass());
	}

	public Object getAnnotadedWithId(Object object, Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();
		try {
			for (Field field : fields) {
				if (field.isAnnotationPresent(Id.class)
						|| field.isAnnotationPresent(ConverterId.class)) {
					field.setAccessible(true);
					return field.get(object);
				}
			}
			for (Method method : methods) {
				if (method.isAnnotationPresent(Id.class)
						|| method.isAnnotationPresent(ConverterId.class)) {
					return method.invoke(object);
				}
			}
			if (clazz.getSuperclass() != null
					&& !clazz.getSuperclass().equals(Object.class)) {
				return getAnnotadedWithId(object, clazz.getSuperclass());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
