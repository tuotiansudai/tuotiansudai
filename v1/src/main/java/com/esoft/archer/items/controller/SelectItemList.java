package com.esoft.archer.items.controller;
// default package

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.items.model.SelectItem;
import com.esoft.archer.items.model.SelectItemGroup;
import com.esoft.core.annotations.ScopeType;

@Component
@Scope(ScopeType.REQUEST)
public class SelectItemList extends EntityQuery<SelectItem> {

	private static final String[] RESTRICTIONS = {
		"selectItem.id like #{selectItemList.example.id}",
		"selectItem.name like #{selectItemList.example.name}",
		"selectItem.selectItemGroup.id like #{selectItemList.example.selectItemGroup.id}"
	};
	
	public SelectItemList(){
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}
	
	@Override
	protected void initExample() {
		SelectItem selectItem = new SelectItem();
		selectItem.setSelectItemGroup(new SelectItemGroup());
		setExample(selectItem);
	}
}