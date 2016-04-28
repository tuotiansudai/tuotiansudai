package com.esoft.archer.user.converter;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;

import org.omnifaces.converter.ValueChangeConverter;

import com.esoft.archer.user.model.Area;

public class AreaConverter extends ValueChangeConverter {

	public Object getAsChangedObject(FacesContext facesContext, UIComponent component,
			String submittedValue) {
		String id = submittedValue.trim();
		if (!id.equals("")) {
			ListIterator<UIComponent> kids = component.getChildren()
					.listIterator();
			UISelectItems ui = (UISelectItems) kids.next();
			ArrayList<Area> areas = (ArrayList<Area>) ui.getValue();
			for (Area area : areas) {
				if (area.getId().equals(id)) {
					return area;
				}
			}
		}
		return null;
	}

	
	public String getAsString(FacesContext facesContext, UIComponent component,
			Object value) {
		if (value == null || value.equals("")) {
			return "";
		} else {
			return ((Area) value).getId();
		}
	}
}
