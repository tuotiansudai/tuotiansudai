package com.esoft.archer.statistics.controller;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.statistics.model.UserExt;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DigitFormatUtil;

@Component
@Scope(ScopeType.VIEW)
public class UserExtList extends EntityQuery<UserExt> {

	private static final String lazyModelCountHql = "select count(distinct userExt.id) from UserExt userExt";
	private static final String lazyModelHql = "select distinct userExt from UserExt userExt";


	public UserExtList() {
		setCountHql(lazyModelCountHql);
		setHql(lazyModelHql);
		final String[] RESTRICTIONS = { };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}
	
	/**
	 * 会员地区统计数据
	 * TODO:台湾省 页面不能选，此处也查不到。
	 * @return 统计数据的json形式
	 */
	public String getAreaStatistics(){
		StringBuilder sb = new StringBuilder("{'investmentSection':[");
		String hql = "select count(user.id), p.id, p.name from Area p, Area a left join a.users user where a.parent = p and p.parent is null group by p.id";
		List ues = getHt().find(hql);
		long count = 0;
		for(int i = 0; i < ues.size(); i++){
			Object[] o = (Object[])ues.get(i);
			count += Long.parseLong(o[0].toString());
		}
		for(int i = 0; i < ues.size(); i++){
			Object[] o = (Object[])ues.get(i);
			double total =  Double.parseDouble((o[0].toString()));
			double per = ArithUtil.round(total / count * 100, 2);
			sb.append("{'cha':'").append(o[1].toString()).append("','city':'")
				.append(o[2].toString()).append("','amount':")
				.append(total).append(",'ratio':'").append(per).append("%'},");
		}
		sb.append("{'cha':'710000','city':'台湾','amount':0.0,'ratio':'0.0%'}");
		sb.append("]}");
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public String getAgeStatistics(){
		StringBuilder sb = new StringBuilder("{'result':[");
		String hql = "select count(ue.id), ue.age from UserExt ue group by ue.age";
		List ues = getHt().find(hql);
		for(int i = 0; i < ues.size(); i++){
			Object[] o = (Object[])ues.get(i);
			double total =  Double.parseDouble((o[0].toString()));
			sb.append("{'age':'").append(o[1].toString()).append("','total':")
				.append(total).append("}");
			if(i != ues.size() - 1){
				sb.append(",");
			}
		}
		sb.append("]}");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Object[] os = new Object[]{1l, "b"};
		System.out.println(Long.parseLong(os[0].toString()));
	}
	
	
}
