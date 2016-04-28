package com.esoft.archer.menu.controller;

import org.apache.commons.logging.Log;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.TabChangeEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;

@Component
@Scope(ScopeType.SESSION)
public class ManagementMenuBean implements java.io.Serializable{
	
	@Logger
	static Log log ;
	
	private String activeIndex = "0" ;

	public void onTabChange(TabChangeEvent event) { 
		int index = event.getTab().getParent().getChildren().indexOf(event.getTab());
		
		log.debug("Tab changeed,active index is " + index );
//		if( activeIndex.equals("0") ){
			activeIndex = String.valueOf(index) ;
//		}

	}  
	
	public void setActiveIndex(String activeIndex) {
		this.activeIndex = activeIndex;
	}

	public String getActiveIndex() {
		return activeIndex;
	}
	
	
	
}
