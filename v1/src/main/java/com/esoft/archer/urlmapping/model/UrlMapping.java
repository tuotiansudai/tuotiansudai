package com.esoft.archer.urlmapping.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.esoft.archer.user.model.Permission;
import com.esoft.core.util.SpringBeanUtil;

/**
 * UrlMapping entity. 访问路径别名
 * 
 * #{pageName} 单页的路径 eg： <!-- 单页 --> <url-mapping id="page"> <pattern
 * value="/m/#{menuId}/p/#{pageName}" /> <view-id
 * value="themepath:#{pageName}.htm" /> </url-mapping>
 */
@Entity
@Table(name = "url_mapping")
@NamedQueries({ @NamedQuery(name = "UrlMapping.findByPattern", query = "Select urlMapping from UrlMapping urlMapping "
		+ " where urlMapping.pattern=?") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UrlMapping implements java.io.Serializable {

	// Fields
	private String id;
	private String pattern;
	private String viewId;
	private String description;
	private List<Permission> permissions = new ArrayList<Permission>(0);
	private String permissionsCommaStr;

	// Constructors

	/** default constructor */
	public UrlMapping() {
	}

	// Property accessors
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "url_mapping_permission", 
			joinColumns = { @JoinColumn(name = "url_mapping_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "permission_id", nullable = false, updatable = false) })
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	@Lob 
	@Column(name = "description", columnDefinition="CLOB")
	public String getDescription() {
		return this.description;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	@Column(name = "pattern", unique = true, nullable = false, length = 500)
	public String getPattern() {
		return pattern;
	}

	@Column(name = "view_id", nullable = false, length = 500)
	public String getViewId() {
		return viewId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	
	/**
	 * 获取权限，返回用逗号分隔的字符串
	 * 
	 * @return
	 */
	@Transient
	public String getPermissionsCommaStr() {
		// FIXME:不太合适。。。总感觉如下处理比较别扭。
		if (StringUtils.isEmpty(this.permissionsCommaStr)) {
			HibernateTemplate ht = (HibernateTemplate) SpringBeanUtil
					.getBeanByName("ht");
			StringBuffer result = new StringBuffer();
			if (!StringUtils.isEmpty(this.getId())) {
				List<Permission> permissions = ht.findByNamedQueryAndNamedParam("Permission.findPermissionsByUrlMappingId", "urlMappingId", this.getId());
				if (permissions.size() > 0) {
					result.append(new StringBuffer(permissions.get(0).getId()));
					for (int i = 1; i < permissions.size(); i++) {
						result.append(",");
						result.append(permissions.get(i).getId());
					}
				}
			}
			this.setPermissionsCommaStr(result.toString());
		}
		return this.permissionsCommaStr;
	}

	public void setPermissionsCommaStr(String permissionsCommaStr) {
		this.permissionsCommaStr = permissionsCommaStr;
	}


}