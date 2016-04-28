package com.esoft.archer.common.controller;

import com.esoft.archer.common.CommonConstants;
import com.esoft.core.annotations.ConverterId;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.Id;
import java.lang.reflect.*;
import java.util.List;

@Component
public class EntityHome<E> {

	protected static StringManager commonSM = StringManager
			.getManager(CommonConstants.Package);

	private String id;

	private String saveView;
	private String updateView;
	private String deleteView;

	private String saveSuccessMessage;
	private String updateSuccessMessage;
	private String deleteSuccessMessage;

	private String saveFailMessage;
	private String updateFailMessage;
	private String deleteFailMessage;

	private final static String saveSuccessMessageKey = "saveSuccessMessage",
			updateSuccessMessageKey = "updateSuccessMessage",
			deleteSuccessMessageKey = "deleteSuccessMessage",
			saveFailMessageKey = "saveFailMessage",
			updateFailMessageKey = "updateFailMessage",
			deleteFailMessageKey = "deleteFailMessage";

	protected E instance;

	// @Resource(name="baseService")
	// private BaseService<E> baseService;

	// public BaseService<E> getBaseService(){
	// return baseService;
	// }

	@Resource(name = "ht")
	HibernateTemplate baseService;

	public HibernateTemplate getBaseService() {
		return baseService;
	}

	public List<E> findAll() {
		return baseService.find("from " + getEntityClass().getSimpleName());
	}

	final static Log log = LogFactory.getLog(EntityHome.class);

	private Class<E> entityClass;

	public E getInstance() {
		if (instance == null) {
			initInstance();
		}
		return instance;

	}

	public E getInstance(final String id) {
		setId(id);
		return getInstance();
	}

	@Transactional(readOnly = false)
	public String save() {
		return this.save(true);
	}

	/**
	 * 
	 * @param showFacesMessage
	 *            是否显示默认facesMessage
	 * @return
	 */
	@Transactional(readOnly = false)
	public String save(boolean showFacesMessage) {
		if (getEntityClass() == null) {
			throw new IllegalStateException("entityClass is null");
		}
		// message ~
		final String message;
		try {
			// log.debug("id=" + getId());
			if (!isIdDefined()) {// create instance
				if (StringUtils.isEmpty(id)) {
					// id =
					// (String)getInstance().getClass().getMethod("getId").invoke(getInstance());
					id = (String) getAnnotadedWithId(getInstance(),
							getInstance().getClass());
				}
				if (!id.matches("^[a-zA-Z0-9_[-]]+$")) { // 编号只能为数字或者字母、下划线、中划线
					FacesUtil.addErrorMessage(commonSM
							.getString("IDRuleMessage"));
					return null;
				}
				if (null != baseService.get(getEntityClass(), getId())) {
					message = commonSM.getString("entityIdHasExist", getId());
					log.debug(message);
					if (showFacesMessage) {
						FacesUtil.addErrorMessage(message);
					}
					setId(null);
					getInstance().getClass().getMethod("setId", String.class)
							.invoke(getInstance(), getId());
					return null;
				}
			}

			baseService.merge(getInstance());
			// baseService.saveOrUpdate(getInstance());

			message = getSaveSuccessMessage();

			if (showFacesMessage) {
				FacesUtil.addInfoMessage(message);
			}
			if (log.isInfoEnabled()) {
				log.info(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage(), e);
			FacesUtil.addErrorMessage(commonSM.getString(saveFailMessageKey));
		}

		return getSaveView();
	}

	@Transactional(readOnly = false)
	public String update() {
		baseService.update(getInstance());
		return getUpdateView();
	}

	@Transactional(readOnly = false)
	public String delete() {
		baseService.delete(getInstance());
		FacesUtil.addInfoMessage(commonSM.getString(deleteSuccessMessageKey));
		return getDeleteView();
	}

	@Transactional(readOnly = false)
	public String delete(String id) {
		baseService.delete(baseService.get(getEntityName(), id));
		FacesUtil.addInfoMessage(commonSM.getString(deleteSuccessMessageKey));
		return getDeleteView();
	}

	public boolean isManaged() {

		return getInstance() != null && baseService.contains(getInstance());
	}

	public boolean isIdDefined() {
		return getId() != null && !"".equals(getId());
	}

	/**
	 * Load the instance if the id is defined otherwise create a new instance <br />
	 * Utility method called by {@link #getInstance()} to load the instance from
	 * the Persistence Context if the id is defined. Otherwise a new instance is
	 * created.
	 * 
	 * @see #createInstance()
	 */
	protected void initInstance() {
		if (isIdDefined()) {

			setInstance(baseService.get(getEntityClass(), getId()));

		} else {
			setInstance(createInstance());
		}
	}

	/**
	 * Set/change the entity being managed.
	 */
	public void setInstance(E instance) {
		this.instance = instance;
	}

	public void clearInstance() {
		setInstance(null);
		setId(null);
	}

	/**
	 * Create a new instance of the entity. <br />
	 * Utility method called by {@link #initInstance()} to create a new instance
	 * of the entity.
	 */
	protected E createInstance() {
		if (getEntityClass() != null) {
			try {
				return getEntityClass().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}

	/**
	 * The simple name of the managed entity
	 */
	protected String getSimpleEntityName() {
		String name = getEntityName();
		if (name != null) {
			return name.lastIndexOf(".") > 0
					&& name.lastIndexOf(".") < name.length() ? name.substring(
					name.lastIndexOf(".") + 1, name.length()) : name;
		} else {
			return null;
		}
	}

	private String getEntityName() {
		return getEntityClass().getName();
	}

	// ~ getter && setter

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getSaveView() {
		if (saveView == null || "".equals(saveView)) {
			saveView = getUpdateView();
		}
		return saveView;
	}

	public void setSaveView(String saveView) {
		this.saveView = saveView;
	}

	public String getUpdateView() {
		return updateView;
	}

	public void setUpdateView(String updateView) {
		this.updateView = updateView;
	}

	public String getDeleteView() {
		return deleteView;
	}

	public void setDeleteView(String deleteView) {
		this.deleteView = deleteView;
	}

	public Class<E> getEntityClass() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) type;
			if (paramType.getActualTypeArguments().length == 2) {
				// likely dealing with -> new
				// EntityHome<Person>().getEntityClass()
				if (paramType.getActualTypeArguments()[1] instanceof TypeVariable) {
					throw new IllegalArgumentException(
							"Could not guess entity class by reflection");
				}
				// likely dealing with -> new Home<EntityManager, Person>() {
				// ... }.getEntityClass()
				else {
					entityClass = (Class<E>) paramType.getActualTypeArguments()[1];
				}
			} else {
				// likely dealing with -> new PersonHome().getEntityClass()
				// where PersonHome extends EntityHome<Person>
				entityClass = (Class<E>) paramType.getActualTypeArguments()[0];
			}
		} else {
			throw new IllegalArgumentException(
					"Could not guess entity class by reflection");
		}

		return entityClass;
	}

	public String getSaveSuccessMessage() {
		if (StringUtils.isEmpty(saveSuccessMessage)) {
			saveSuccessMessage = commonSM.getString(saveSuccessMessageKey, id);
		}
		return saveSuccessMessage;
	}

	public void setSaveSuccessMessage(String saveSuccessMessage) {
		this.saveSuccessMessage = saveSuccessMessage;
	}

	public String getUpdateSuccessMessage() {
		return updateSuccessMessage;
	}

	public void setUpdateSuccessMessage(String updateSuccessMessage) {
		this.updateSuccessMessage = updateSuccessMessage;
	}

	public String getDeleteSuccessMessage() {
		return deleteSuccessMessage;
	}

	public void setDeleteSuccessMessage(String deleteSuccessMessage) {
		this.deleteSuccessMessage = deleteSuccessMessage;
	}

	public String getSaveFailMessage() {
		return saveFailMessage;
	}

	public void setSaveFailMessage(String saveFailMessage) {
		this.saveFailMessage = saveFailMessage;
	}

	public String getUpdateFailMessage() {
		return updateFailMessage;
	}

	public void setUpdateFailMessage(String updateFailMessage) {
		this.updateFailMessage = updateFailMessage;
	}

	public String getDeleteFailMessage() {
		return deleteFailMessage;
	}

	public void setDeleteFailMessage(String deleteFailMessage) {
		this.deleteFailMessage = deleteFailMessage;
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
