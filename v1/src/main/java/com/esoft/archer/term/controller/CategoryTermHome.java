package com.esoft.archer.term.controller;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.term.TermConstants;
import com.esoft.archer.term.model.CategoryTerm;
import com.esoft.archer.term.model.CategoryTermType;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class CategoryTermHome extends EntityHome<CategoryTerm>  implements Serializable{
	
	
	@Logger static Log log ;
	@Resource
	HibernateTemplate ht ;
	private static final StringManager sm = StringManager.getManager(TermConstants.Package);
	public CategoryTermHome(){
		setUpdateView( FacesUtil.redirect(TermConstants.View.TERM_LIST) );
	}
	
	
	public CategoryTerm getTermById(String id){
		CategoryTerm instance = getBaseService().get(getEntityClass(), id);
		return instance ;
	}
	
	@Override
	@Transactional(readOnly=false)
	public String delete() {
		
//		if (log.isInfoEnabled()) {
//			log.info(sm.getString("log.info.deleteTerm",
//					(FacesUtil
//							.getExpressionValue("#{loginUserInfo.loginUserId}").toString()), new Date(), getId()));
//		}
		Set<Node> nodeSets =  getInstance().getNodes();
		List<CategoryTerm> ct =getInstance().getChildren();
		if( (nodeSets != null && nodeSets.size() > 0) || ct.size()>0){
//			log.info(sm.getString("log.info.deleteTermUnccessful",
//					(FacesUtil
//							.getExpressionValue("#{loginUserInfo.loginUserId}").toString()), new Date(), getId()));
			FacesUtil.addErrorMessage("删除失败，请先删除该分类下的所有分类或文章。");
			return null;
		}else{
			return super.delete();
		}
	}
	
	@Transactional(readOnly=false)
	public String save(){
		
		//判断父菜单是否是自己本身
		boolean parentIsOneself = false ;
		CategoryTerm term = getInstance() ;
		while( term.getParent() != null ){
			
			if( StringUtils.equals(getInstance().getId(), term.getParent().getId())){
				parentIsOneself = true ;
				break ;
			}
			
			term = term.getParent();
		}
		
		if(parentIsOneself){
			FacesUtil.addWarnMessage(sm.getString("parentCanNotBeItself"));
			return null ;
		}
		
	
		return super.save();
	}
	
}
