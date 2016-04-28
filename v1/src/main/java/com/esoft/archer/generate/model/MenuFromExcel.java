package com.esoft.archer.generate.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 从excel里面读取数据，生成对象
 * 
 * @author Administrator
 * 
 */
public class MenuFromExcel {
	private String lineNumber;
	private String id;
	private String label;
	private String urlType;
	private String pid;
	private String termType;
	private String type;
	private String url;
	private String enable;
	private String permission;
	private String seqNum;
	private String description;
	private String expanded;
	private String language;
	private String target;
	private MenuFromExcel parent;
	private int level;
	private List<MenuFromExcel> children = new ArrayList<MenuFromExcel>();

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isParent() {
		if (this.getChildren().size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public MenuFromExcel getParent() {
		return parent;
	}

	public void setParent(MenuFromExcel parent) {
		this.parent = parent;
	}

	public List<MenuFromExcel> getChildren() {
		return children;
	}

	public void setChildren(List<MenuFromExcel> children) {
		this.children = children;
	}

	public String getDescription() {
		return description;
	}

	public String getEnable() {
		return enable;
	}

	public String getExpanded() {
		return expanded;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getLanguage() {
		return language;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public String getPermission() {
		return permission;
	}

	public String getPid() {
		return pid;
	}

	public String getSeqNum() {
		return seqNum;
	}

	public String getTarget() {
		return target;
	}

	public String getTermType() {
		return termType;
	}

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public void setExpanded(String expanded) {
		this.expanded = expanded;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setTermType(String termType) {
		this.termType = termType;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

}
