package com.esoft.archer.system.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.system.model.Dict;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;

@Component
@Scope(ScopeType.VIEW)
public class DictHome extends EntityHome<Dict> implements java.io.Serializable{
	
	public DictHome() {
		setUpdateView(FacesUtil.redirect("/admin/system/dictList"));
	}
	
	@Override
	@Transactional(readOnly = false)
	public String save() {
		
		if(StringUtils.isEmpty(getInstance().getId())){//新增
			getInstance().setId(IdGenerator.randomUUID());
		}else{//编辑
			//判断父编号不能为自己本身
			Dict parent = getInstance().getParent();
			String parentId = parent == null ? "" : parent.getId();
			if(StringUtils.equals(getInstance().getId(), parentId)){
				FacesUtil.addErrorMessage("父节点编码不能为自己本身！");
				return null ;
			}
			
		}
		
		return super.save();
		
	}
}
