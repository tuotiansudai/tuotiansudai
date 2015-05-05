package com.esoft.archer.statistics.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * UserExt 图形统计用 视图。
 * 创建视图sql:
 * create or replace view user_area_age(id, area, age) as
 * select u.id id, u.area area, year(NOW())-substring(u.id_card,7,4) age from user u where u.id_card is not null and u.id_card != ''
 * create or replace view user_ext (id, province_id, province_name, age) as
 * select us.id id, parent.id province_id, parent.name province_name, if(us.age < 18, '未成年', if(us.age <= 29, '18-29岁' , if(us.age <= 39, '30-39岁', if(us.age <= 49, '40-49岁', if(us.age <= 59, '50-59岁', '60岁以上'))))) age from user_area_age us left join area on us.area = area.id left join area parent on area.pid = parent.id
 */
@Entity
@Table(name = "user_ext")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "entityCache")
public class UserExt implements java.io.Serializable {
	private static final long serialVersionUID = 3158876390030677047L;
	
	//同人员ID
	private String id;
	//省 ID
	private String provinceId;
	//省份名
	private String provinceName;
	//年龄范围（如 18-29岁)
	private String age;
	
	@Id
	@Column(name="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="province_id")
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	@Column(name="province_name")
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	@Column(name="age")
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
}