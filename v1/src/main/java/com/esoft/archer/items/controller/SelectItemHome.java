package com.esoft.archer.items.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.items.SelectItemConstant;
import com.esoft.archer.items.model.SelectItem;
import com.esoft.archer.items.model.SelectItemGroup;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;

@Component
@Scope(ScopeType.VIEW)
public class SelectItemHome extends EntityHome<SelectItem> {

	@Resource
	HibernateTemplate ht;
	public SelectItemHome(){
		setUpdateView(FacesUtil.redirect(SelectItemConstant.View.SELECTITEM_LIST));
	}
	
	@SuppressWarnings("unchecked")
	public SelectItem getselectItemsbyId(String selectitemId){
		List<SelectItem> itemList = ht.findByNamedQuery("SelectItem.findItemsById",
				selectitemId);
		if (itemList == null || itemList.size() == 0) {
			return null;
		}
		return (SelectItem) itemList.get(0);
	}
	/**
	 * 根据候选项id获取选项组
	 * @param selectitemId
	 * @return
	 */
	public List<Object> slectItemList(String selectitemId) {
		List<Object> itemlist = new ArrayList<Object>();
		
		SelectItem selectItem = getselectItemsbyId(selectitemId);
		if (selectItem == null) {
			return null;
		}

		String items = selectItem.getItems().replaceAll("，", ",");
		String[] itemArray = items.split(",");

		for (String item : itemArray) {
			itemlist.add(item);
		}
		return itemlist;
	}
}
