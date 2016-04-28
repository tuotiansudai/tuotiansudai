package com.esoft.archer.theme.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.esoft.archer.theme.ThemeConstants;
import com.esoft.archer.theme.model.Component;
import com.esoft.archer.theme.model.ComponentParameter;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@org.springframework.stereotype.Component
@Scope(ScopeType.VIEW)
public class TemplatesMgr implements java.io.Serializable {
	@Logger
	private static Log log ;
	@Resource
	private HibernateTemplate ht;
	
	private StringManager sm = StringManager.getManager(ThemeConstants.Package);
	
	private final static String TEMPLATES_PATH = "/templates/";
	
	private List<String> templates ;
	
	private String selectTpl ;
	
	private String themeName ;
	
	private String content ;
	
	private String templateSrc;
	
	private Component component;
	
	private String insertContent;
	
	private String editContent;
	
	private List<Component> components = new ArrayList<Component>();
	
	private List<ComponentParameter> componentParametersEdit = new ArrayList<ComponentParameter>();
	
	private List<ComponentParameter> componentParametersInsert = new ArrayList<ComponentParameter>();
	
	public TemplatesMgr(){
		if(component == null){
			component = new Component();
		}
	}
	
	public String saveTpl(){
		//save file .
//		System.out.println(getThemePath() + getSelectTpl() + "\n" + content);
		try {
			FileUtils.writeStringToFile(getFile(getThemePath(), selectTpl), content, "utf-8");
			FacesUtil.addInfoMessage(sm.getString("saveComponentSuccess"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//add message.
		return null ;
	}

	public void setTemplates(List<String> templates) {
		this.templates = templates;
	}

	public List<String> getTemplates() {
		if(templates == null){
			//templates = 
			//get really path . 
			//System.out.println(FacesUtil.getAppRealPath()+ThemeConstants.THEME_PATH+"blog/");
			
			File tplsFile = getFile(getThemePath());
			if(tplsFile.exists()){
				templates = Arrays.asList(tplsFile.list());
			}
			
			//filter .xhtml file .
		}
		return templates;
	}

	public void setSelectTpl(String selectTpl) {
		this.selectTpl = selectTpl;
	}

	public String getSelectTpl() {
		if(selectTpl == null && getTemplates().size() > 0){
			selectTpl = (getTemplates().get(0));
		}
		return selectTpl;
	}


	public String getThemeName() {
		return (String)FacesUtil.getSessionAttribute(ThemeConstants.SESSION_KEY_USER_THEME);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		try {
			 
			content = FileUtils.readFileToString(getFile(getThemePath(),getSelectTpl()),"utf-8");
		} catch (IOException e) {
			content = "Read File error.";
			e.printStackTrace();
		}
		return content;
	}
	
	private String getThemePath(){
		return (FacesUtil.getAppRealPath()+ThemeConstants.THEME_PATH + getThemeName() + TEMPLATES_PATH);
	}
	
	private File getFile(String path,String file){
		if(file == null){
			return new File(path);
		}
		return new File(path + file);
	}
	
	private File getFile(String path){
		return getFile(path ,null);
	}
	
	
	public String getTemplateSrc() {
		return templateSrc;
	}

	public void setTemplateSrc(String templateSrc) {
		this.templateSrc = templateSrc;
	}
	
	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public HibernateTemplate getHt() {
		return ht;
	}

	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}

	public List<Component> getComponents() {
		components = ht.findByNamedQuery("Component.findAll");
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}
	
	public List<ComponentParameter> getComponentParametersEdit() {
		return componentParametersEdit;
	}

	public void setComponentParametersEdit(
			List<ComponentParameter> componentParametersEdit) {
		this.componentParametersEdit = componentParametersEdit;
	}

	public List<ComponentParameter> getComponentParametersInsert() {
		return componentParametersInsert;
	}

	public void setComponentParametersInsert(
			List<ComponentParameter> componentParametersInsert) {
		this.componentParametersInsert = componentParametersInsert;
	}

	public String getInsertContent() {
		return insertContent;
	}

	public void setInsertContent(String insertContent) {
		this.insertContent = insertContent;
	}
	
	public String getEditContent() {
		return editContent;
	}

	public void setEditContent(String editContent) {
		this.editContent = editContent;
	}

	public void findTemplate(){
		setComponent(new Component());
		String scriptSrc = this.getTemplateSrc();
		if(StringUtils.isEmpty(scriptSrc)){
			return ;
		}else{
			List<Component> componentList = ht.findByNamedQuery("Component.findByUrl", scriptSrc);
			if(componentList != null && componentList.size() > 1){
//				FacesUtil.addErrorMessage(sm.getString("componentUrlConflict"));
				return ;
			}else if(componentList != null && componentList.size() == 1){
				setComponent(componentList.get(0));
				setComponentParametersEdit(ht.findByNamedQuery("ComponentParameter.findByCompanent", componentList.get(0).getId()));
			}else if(componentList == null || componentList.size() <1){
//				FacesUtil.addErrorMessage(sm.getString("componentUrlError"));
				return ;
			}
		}
	}
	
	public void handleComponentChange(){
		String componentId = getComponent().getId();
		if(StringUtils.isEmpty(componentId)){
			return ;
		}
		setComponent(ht.get(Component.class, componentId));
		setComponentParametersInsert(ht.findByNamedQuery("ComponentParameter.findByCompanent", componentId));
	}
	
	private String initInclude(List<ComponentParameter> componentParameters){
		String str ="";
		if(StringUtils.isEmpty(getComponent().getScriptUrl())){
			return str;
		}
		List<Component> componentList = ht.findByNamedQuery("Component.findByUrl", getComponent().getScriptUrl());
		if(componentList != null && componentList.size() > 1){
//			FacesUtil.addErrorMessage(sm.getString("componentUrlConflict"));
			return str;
		}else if(componentList != null && componentList.size() == 1){
			setComponent(componentList.get(0));
		}else if(componentList == null || componentList.size() <1){
//			FacesUtil.addErrorMessage(sm.getString("componentUrlError"));
			return str;
		}
		str = "\n<ui:include src=\""+getComponent().getScriptUrl()+"\">\n";
		if(componentParameters != null && componentParameters.size() > 0){
			for(ComponentParameter c : componentParameters){
				String cId = c.getId();
				ComponentParameter comNew = ht.get(ComponentParameter.class, cId);
				if(StringUtils.isNotEmpty(c.getValue()) && !c.getValue().equals(comNew.getValue())){
					str = str + "    <ui:param name=\""+c.getName()+"\" value=\""+c.getValue()+"\"></ui:param>\n";
				}
			}
		}
		str = str + "</ui:include>\n";
		return str;
	}
	
	public void editInsertContent(){
		String str = initInclude(getComponentParametersInsert());
		setInsertContent(str);
	}
	
	public void editContent(){
		String str = initInclude(getComponentParametersEdit());
		setEditContent(str);
	}
	
}
