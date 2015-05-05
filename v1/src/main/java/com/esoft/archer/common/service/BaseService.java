package com.esoft.archer.common.service;

import java.io.Serializable;
import java.util.List;

public interface BaseService<E> {
	
	public List<E> findAll(Class<E> c);
	
	public List<E> findAll(Class<E> c,String orderBy );
	
	public List<E> findByExample(E e);
	
	public void merge(E e);
	
	public void save(E e);
	
	public void update(E e);
	
	public void saveOrUpdate(E e);
	
	public void delete(E e);
	
	public void delete(Class<E> c , Serializable id);
	
	public boolean contains(E e);
	
	public E get(Class<E> c, Serializable id);
	
	public Object find(String hql);
	
	/**
	 * 通过定义 @NamedQuery 查询
	 * @param queryName
	 * @param values
	 * @return 查询结果
	 */
	public List findByNamedQuery(String queryName ,Object ...values );
	
	
}
