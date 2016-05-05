package com.esoft.archer.common.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.service.BaseService;

@Service("baseService")
@SuppressWarnings("unchecked")
public class BaseServiceImpl<E> implements BaseService<E> {

	@Resource
	private HibernateTemplate ht;

	public List<E> findAll(Class<E> c) {
		List<E> result = ht.find("from " + c.getName());
		if (result == null) {
			return new ArrayList<E>();
		}
		return result;
	}

	public List<E> findByExample(E e) {

		return ht.findByExample(e);
	}

	@Transactional(readOnly=false)
	public void merge(E e) {
		ht.merge(e);

	}

	@Transactional(readOnly=false)
	public void delete(E e) {
		ht.delete(e);

	}

	public E get(Class<E> c, Serializable id) {

		return ht.get(c, id);
	}

	public List<E> findAll(Class<E> c, String orderBy) {

		return ht.find("from " + c.getName() + " order by "
				+ orderBy);

	}

	@Transactional(readOnly=false)
	public void save(E e) {
		ht.save(e);
		
	}
	@Transactional(readOnly=false)
	public void update(E e) { 
		ht.update(e);

	}
	@Transactional(readOnly=false)
	public void saveOrUpdate(E e){
		ht.saveOrUpdate(e);
	}
	
	public boolean contains(E e) {
		return ht.contains(e);
	}
	@Transactional(readOnly=false)
	public void delete(Class<E> c, Serializable id) {
		ht.delete(get(c, id));

	}

	public Object find(String hql) {
		return ht.find(hql);
	}
	
	public List findByNamedQuery(String queryName, Object... values) {
		return ht.findByNamedQuery(queryName, values);
	}

}
