package com.esoft.archer.items.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.items.SelectItemConstant;
import com.esoft.archer.items.model.SelectItemGroup;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.PublicUtils;

@Component
@Scope(ScopeType.REQUEST)
public class SelectItemGroupHome extends EntityHome<SelectItemGroup> {

	@Resource
	HibernateTemplate ht;
	@Logger
	static Log log;
	public SelectItemGroupHome(){
		setUpdateView(FacesUtil.redirect(SelectItemConstant.View.SELECTITEMGROUP_LIST));
	}
	
    @Override
    @Transactional(readOnly = false)
    public String save() {
    	if(PublicUtils.isEmpty(getId())){
    		if(PublicUtils.isEmpty(getInstance().getId())){
    			getInstance().setId(IdGenerator.randomUUID());
    		}
    		if(isExistName()){
    			log.error("name has exist! please input again");
    			FacesUtil.addErrorMessage(SelectItemConstant.ErrorMsg.NAMEHASEXIST);
    			return null;
    		}
    	}
    	return super.save();
    }
    
    @SuppressWarnings("unchecked")
	public boolean isExistName(){
    	List<SelectItemGroup> itemgroup = ht.findByNamedQuery("SelectItemGroup.findSelectItemGroupByname",
				getInstance().getName());
		if (itemgroup != null && itemgroup.size() > 1) {
			return true;
		}else if(itemgroup != null && itemgroup.size() == 1){
			String group = itemgroup.get(0).getId();
			if(!group.equals(getInstance().getId())){
				return true;
			}
		}
		return false;
    }
}
