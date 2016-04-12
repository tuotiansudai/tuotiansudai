package com.esoft.archer.link.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.language.model.Language;
import com.esoft.archer.link.LinkConstants;
import com.esoft.archer.link.model.Link;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
@Component
@Scope(ScopeType.VIEW)
public class LinkList extends EntityQuery<Link> implements Serializable{
	@Logger
	static Log log;
	public LinkList() {
		final String[] RESTRICTIONS = {
				"id like #{linkList.example.id}",
				"name like #{linkList.example.name}",
				"url like #{linkList.example.url}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));

	}
	
	public List<Link> getAllLinks(){
		return getHt().find("from Link order by seqNum");
	}
	
	/**
	 * 获取首页面所有的链接
	 * @return
	 */
	public List<Link> getFrontLinks(){
		return getLinks(LinkConstants.LinkPosition.FRONT);
	}
	
	/**
	 * 获取内页所有链接
	 * @return
	 */
	public List<Link> getInnerLinks(){
		return getLinks(LinkConstants.LinkPosition.INNER);
		
	}
	
	
	public List<Link> getLinks(final String type){
		return getHt().findByNamedQuery("Link.findLinkByPositionOrderBySeqNumAndName", type);
	}
	
	
}
