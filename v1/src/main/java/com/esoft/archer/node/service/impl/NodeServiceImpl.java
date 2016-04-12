package com.esoft.archer.node.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.openqa.selenium.internal.seleniumemulation.GetHtmlSource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.service.impl.BaseServiceImpl;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.node.model.NodeBodyHistory;
import com.esoft.archer.node.service.NodeService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.IdGenerator;

@Service
public class NodeServiceImpl extends BaseServiceImpl<Node> implements
		NodeService {

	@Logger
	static Log log;
	
	@Resource
	HibernateTemplate ht;

	@Transactional(readOnly=false)
	public void save(Node node) {

		if (StringUtils.isEmpty(node.getNodeBody().getId())) {
			// 第一次创建
			node.getNodeBody().setId(IdGenerator.randomUUID());
		}
		if (node.getCreateTime() == null) {
			node.setCreateTime(new Date());
		}
		node.setUpdateTime(new Date());

		
		Double version = 0.1d ;
		Iterator<Double> it = ht.iterate("select max(version) from NodeBodyHistory where node.id = ?",node.getId());
		if(it.hasNext()){
			Double result = it.next();
			if(result != null){
				version = new BigDecimal(version).add(new BigDecimal(result)).doubleValue();
			}
		}
		
//		NodeBodyHistory history = new NodeBodyHistory();
//		history.setId(IdGenerator.randomUUID());
//		history.setNode(node);
//		history.setCreateTime(node.getUpdateTime());
//		history.setVersion(version);
//		history.setBody(node.getNodeBody().getBody());
		// node.getNodeBodyHistories().add(history);
		ht.saveOrUpdate(node);
//		ht.save(history);
		// ht.save(history);
	}
}