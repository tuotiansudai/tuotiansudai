package com.esoft.archer.common.controller;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.CommonConstants;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
public class EntityQuery<E> {

	static Log log = LogFactory.getLog(EntityQuery.class);
	protected static StringManager commonSM = StringManager
			.getManager(CommonConstants.Package);

	private final static String HQL_TEMPLATE_SELECT = "Select {0} from {1} {0} ";
	private final static String HQL_TEMPLATE_SELECT_COUNT = "Select count({0}) from {1} {0} ";

	/**
	 * 自定义每页数量
	 */
	private int pageSize = 10;
	/**
	 * 自定义当前页
	 */
	private int currentPage = 1;

	/**
	 * 手动取LazyModel的值，用自定义当前页和自定义数量
	 * 
	 * @return
	 */
	public List<E> getLazyModelData() {
		return this.getLazyModel().load(pageSize * (currentPage - 1), pageSize, null, null, null);
	}

	private String hql;
	private String countHql;
	private List<String> orderColumn;
	private List<String> orderDirection;
	private Object[] parameterValues;
	private List<String> restrictionExpressionStrings;

	private E example;
	public List<E> allResultList;

	private LazyDataModel<E> lazyModel;

	@Resource
	HibernateTemplate ht;

	public HibernateTemplate getHt() {
		return ht;
	}

	@Resource
	JdbcTemplate jt;

	private String entityAlias;

	String getEntityAlias() {
		if (entityAlias == null) {
			String entityClassName = getEntityClass().getSimpleName();
			entityAlias = StringUtils.uncapitalize(entityClassName);
		}

		return entityAlias;
	}

	private final static String EL_PATTERN = "[#{](.*?)[}]";
	// private static final Pattern SUBJECT_PATTERN =
	// Pattern.compile("^select (\\w+(\\.\\w+)*)\\s+from",
	// Pattern.CASE_INSENSITIVE);
	// private static final Pattern FROM_PATTERN =
	// Pattern.compile("(^|\\s)(from)\\s", Pattern.CASE_INSENSITIVE);
	private static final Pattern WHERE_PATTERN = Pattern.compile(
			"\\s(where)\\s", Pattern.CASE_INSENSITIVE);
	private static final Pattern ORDER_PATTERN = Pattern.compile(
			"\\s(order)(\\s)+by\\s", Pattern.CASE_INSENSITIVE);

	protected String parseHql(final String hql) {
		StringBuilder builder = new StringBuilder(hql);

		Pattern p = Pattern.compile(EL_PATTERN);
		List<String> restrictions = getRestrictionExpressionStrings();
		// parser where
		if (restrictions != null && restrictions.size() >= 1) {

			List<Object> parameterValues = new ArrayList<Object>();
			for (String restriction : restrictions) {
				boolean isFindExpression = false;
				Matcher m = p.matcher(restriction);
				while (m.find()) {
					isFindExpression = true;
					final String el = m.group();
					Object elValue = FacesUtil.getExpressionValue(el);
					// log.debug("el = " + el + ",elValue=" + elValue);
					if (elValue != null && !elValue.toString().equals("")) {
						if (WHERE_PATTERN.matcher(builder).find()) {
							builder.append(" and ");
						} else {
							builder.append(" where ");
						}
						restriction = m.replaceAll("?");
						parameterValues.add(elValue);
						builder.append(restriction);
					}
				}
				if (!isFindExpression) {
					if (WHERE_PATTERN.matcher(builder).find()) {
						builder.append(" and ");
					} else {
						builder.append(" where ");
					}
					builder.append(restriction);
				}
			}
			setParameterValues(parameterValues.toArray());
		}

		return builder.toString();
	}

	public E getEntityById(String id) {
		return getHt().get(getEntityClass(), id);
	}

	public String getOrder() {
		if (getOrderColumn() == null) {
			return null;
		}
		StringBuffer sortStr = new StringBuffer();
		for (int i = 0; i < getOrderColumn().size(); i++) {
			if (i > 0) {
				sortStr.append(",");
			}
			sortStr.append(getOrderColumn().get(i));
			sortStr.append("  ");
			sortStr.append(getOrderDirection().get(i));
		}
		return sortStr.toString();
	}

	/**
	 * 添加一个排序
	 * 
	 * @param orderColumn
	 *            排序字段
	 * @param orderDirection
	 *            排序方向 DIR_DESC DIR_ASC
	 */
	public void addOrder(String orderColumn, String orderDirection) {
		if (this.orderColumn == null || this.orderDirection == null) {
			this.orderColumn = new ArrayList<String>();
			this.orderColumn.add(orderColumn);
			this.orderDirection = new ArrayList<String>();
			this.orderDirection.add(orderDirection);
		} else {
			// 如果排序字段已经包含要添加的字段
			if (this.getOrderColumn().contains(orderColumn)) {
				int index = this.getOrderColumn().indexOf(orderColumn);
				// 判断该字段对应的排序方向是否和需要添加的一样
				if (!this.getOrderDirection().get(index).equals(orderDirection)) {
					// 如果不同，则改变排序方向
					this.getOrderDirection().remove(index);
					this.getOrderDirection().add(index, orderDirection);
				}
			} else {
				this.getOrderColumn().add(orderColumn);
				this.getOrderDirection().add(orderDirection);
			}
		}
	}

	/**
	 * 获取一个排序
	 * 
	 * @param orderColumn
	 *            排序字段
	 * @param orderDirection
	 *            排序方向 DIR_DESC DIR_ASC
	 */
	public String getOrderByOrderColumn(String orderColumn) {
		if (this.orderColumn != null && this.orderDirection != null
				&& this.getOrderColumn().contains(orderColumn)) {
			int index = this.getOrderColumn().indexOf(orderColumn);
			return this.getOrderDirection().get(index);
		}
		return null;
	}

	/**
	 * 移除一个排序
	 * 
	 * @param orderColumn
	 */
	public void removeOrder(String orderColumn) {
		int index = this.getOrderColumn().indexOf(orderColumn);
		if (index != -1) {
			this.getOrderColumn().remove(index);
			this.getOrderDirection().remove(index);
		}
	}

	public String getRenderedHql() {
		StringBuilder renderedHql = new StringBuilder(parseHql(getHql()));
		// parser order .
		if (!ORDER_PATTERN.matcher(renderedHql).find()
				&& StringUtils.isNotEmpty(getOrder())) {
			renderedHql.append(" order by ").append(getOrder());
		}
		return renderedHql.toString();
	}

	protected String getRenderedCountHql() {
		return parseHql(getCountHql());
	}

	protected static final String DIR_ASC = "asc";
	protected static final String DIR_DESC = "desc";

	protected void initLazyModel() {
		lazyModel = new LazyDataModel<E>() {

			private static final long serialVersionUID = -4605725783065773722L;

			public void setRowIndex(int rowIndex) {
				if (getPageSize() == 0) {
					setPageSize(15);
				}
				super.setRowIndex(rowIndex);
			};

			@Override
			public Object getRowKey(E object) {
				return getLazyModelRowKey(object);
			}

			@Override
			public E getRowData(String rowKey) {
				return getLazyModelRowData(rowKey);
			}

			/**
			 * 多行排序
			 */
			@Override
			public List<E> load(int first, int pageSize,
					List<SortMeta> multiSortMeta, Map<String, String> filters) {
				List<String> orderColumns = null;
				List<String> orderDirections = null;
				if (multiSortMeta != null) {
					orderColumns = new ArrayList<String>();
					orderDirections = new ArrayList<String>();
					for (SortMeta sortMeta : multiSortMeta) {
						orderColumns.add(sortMeta.getSortField());
						orderDirections
								.add(sortMeta.getSortOrder().compareTo(
										SortOrder.DESCENDING) == 0 ? DIR_DESC
										: DIR_ASC);
					}
				}
				return load(first, pageSize, orderColumns, orderDirections);
			};

			@Override
			public List<E> load(final int first, final int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {

				List<String> orderColumns = null;
				List<String> orderDirections = null;
				if (StringUtils.isNotEmpty(sortField)) {
					orderColumns = new ArrayList<String>();
					orderDirections = new ArrayList<String>();
					orderColumns.add(sortField);
					orderDirections.add(sortOrder
							.compareTo(SortOrder.DESCENDING) == 0 ? DIR_DESC
							: DIR_ASC);
				}

				return load(first, pageSize, orderColumns, orderDirections);
			}

			private List<E> load(final int first, final int pageSize,
					List<String> orderColumns, List<String> orderDirections) {
				if (getOrderColumn() != null && orderColumns != null) {
					for (String oc : orderColumns) {
						removeOrder(oc);
					}
					// 确保用户在表格中进行排序的，一定要首先生效。
					getOrderColumn().addAll(0, orderColumns);
				} else if (orderColumns != null) {
					setOrderColumn(orderColumns);
				}
				if (getOrderDirection() != null && orderDirections != null) {
					getOrderDirection().addAll(0, orderDirections);
				} else if (orderDirections != null) {
					setOrderDirection(orderDirections);
				}
				if (getParameterValues() == null) {
					setParameterValues(new Object[] {});
				}

				// if (log.isDebugEnabled()) {
				// log.debug("Rendered Hql = " + getRenderedHql());
				// log.debug("Rendered Count Hql = " + getRenderedCountHql());
				// }

				List<Object> objs = ht.find(getRenderedCountHql(),
						getParameterValues());
				Long count = 0L;
				if (objs.size() != 0) {
					count = (Long) objs.get(0);
				}
				setRowCount(count.intValue());

				@SuppressWarnings("unchecked")
				List<E> resultList = ht
						.execute(new HibernateCallback<List<E>>() {

							public List<E> doInHibernate(Session session)

							throws HibernateException, SQLException {
								Query query = session
										.createQuery(getRenderedHql());
								// 从第0行开始
								query.setFirstResult(first);
								query.setMaxResults(pageSize);
								for (int i = 0; i < getParameterValues().length; i++) {
									query.setParameter(i,
											getParameterValues()[i]);
								}
								return query.list();
							}

						});

				dealResultList(resultList);
				return resultList;
			}
		};

	}

	public Object getLazyModelRowKey(E object) {
		// 如果dataTable要多选，则必须重新该方法。
		throw new UnsupportedOperationException(
				"getLazyModelRowKey(T object) must be implemented when basic rowKey algorithm is not used.");
	}

	public E getLazyModelRowData(String rowKey) {
		// 如果dataTable要多选，则必须重新该方法。
		throw new UnsupportedOperationException(
				"getLazyModelRowData(String rowKey) must be implemented when basic rowKey algorithm is not used.");
	}

	public void dealResultList(List<E> resultList) {

	}

	public LazyDataModel<E> getLazyModel() {
		if (lazyModel == null) {
			initLazyModel();
		}

		return lazyModel;
	}

	public void setLazyModel(LazyDataModel<E> lazyModel) {
		this.lazyModel = lazyModel;
	}

	public void initAllResultList() {
		try {
			allResultList = ht.find("from " + getEntityClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getLocalizedMessage(), e);
		}
	}

	public void setAllResultList(List<E> allResultList) {
		this.allResultList = allResultList;
	}

	public List<E> getAllResultList() {
		if (allResultList == null) {
			initAllResultList();
		}
		return allResultList;
	}

	private Class<E> entityClass;

	public Class<E> getEntityClass() {
		if (entityClass != null) {
			return entityClass;
		}
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

	public void setHql(String hql) {
		this.hql = hql;
	}

	public String getHql() {
		if (hql == null || hql.length() == 0) {
			hql = parserHql(HQL_TEMPLATE_SELECT);
		}
		return hql;
	}

	public void setCountHql(String countHql) {
		this.countHql = countHql;
	}

	public String getCountHql() {
		if (countHql == null || countHql.equals("")) {
			countHql = parserHql(HQL_TEMPLATE_SELECT_COUNT);
		}
		return countHql;
	}

	private String parserHql(String hql) {
		String entityAlias = getEntityAlias();
		String entityClassName = getEntityClass().getSimpleName();
		return MessageFormat.format(hql, entityAlias, entityClassName);
	}

	public void setRestrictionExpressionStrings(
			List<String> restrictionExpressionStrings) {
		this.restrictionExpressionStrings = restrictionExpressionStrings;
	}

	public List<String> getRestrictionExpressionStrings() {
		return restrictionExpressionStrings;
	}

	/**
	 * 添加lazyModel的一个查询条件。
	 * 
	 * @param restirction
	 */
	public void addRestriction(String restirction) {
		if (this.restrictionExpressionStrings == null) {
			this.restrictionExpressionStrings = new ArrayList<String>();
			this.restrictionExpressionStrings.add(restirction);
		} else if (!this.restrictionExpressionStrings.contains(restirction)) {
			if (this.restrictionExpressionStrings instanceof AbstractList) {
				this.restrictionExpressionStrings = new ArrayList<String>(
						this.restrictionExpressionStrings);
			}
			this.restrictionExpressionStrings.add(restirction);
		}
	}

	/**
	 * 移除lazyModel的一个查询条件。
	 * 
	 * @param restirction
	 */
	public void removeRestriction(String restirction) {
		if (this.restrictionExpressionStrings != null
				&& this.restrictionExpressionStrings.contains(restirction)) {
			if (this.restrictionExpressionStrings instanceof AbstractList) {
				this.restrictionExpressionStrings = new ArrayList<String>(
						this.restrictionExpressionStrings);
			}
			this.restrictionExpressionStrings.remove(restirction);
		}
	}

	public void setOrderColumn(List<String> orderColumn) {
		this.orderColumn = orderColumn;
	}

	public List<String> getOrderColumn() {
		return orderColumn;
	}

	public void setOrderDirection(List<String> orderDirection) {
		this.orderDirection = orderDirection;
	}

	public List<String> getOrderDirection() {
		return orderDirection;
	}

	public void setParameterValues(Object[] parameterValues) {
		this.parameterValues = parameterValues;
	}

	public Object[] getParameterValues() {
		return parameterValues;
	}

	public void setExample(E example) {
		this.example = example;
	}

	public E getExample() {
		if (example == null) {
			initExample();
		}
		return example;
	}

	protected void initExample() {
		try {
			setExample(getEntityClass().newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
