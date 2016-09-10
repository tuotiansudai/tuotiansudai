package com.tuotiansudai.spring.session;

import org.springframework.session.Session;

import java.util.Map;


public interface MyFindByIndexNameSessionRepository<S extends Session> extends MySessionRepository<S> {

	String PRINCIPAL_NAME_INDEX_NAME = MyFindByIndexNameSessionRepository.class.getName().concat(".PRINCIPAL_NAME_INDEX_NAME");

	Map<String, S> findByIndexNameAndIndexValue(String indexName, String indexValue);
}
