package com.esoft.archer.common.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.ScopeType;

/**
 * 一些字符串工具，在页面上用
 * 
 * @author Administrator
 * 
 */
@Component
@Scope(ScopeType.REQUEST)
public class StringHome {
	/**
	 * 字符串切割，加上“...”
	 */
	public String ellipsis(String str, int length) {
		char[] strs = str.toCharArray();
		String ellipsisStr = "...";
		if (length > strs.length) {
			length = strs.length;
			ellipsisStr = "";
		}
		char[] aimStrs = new char[length];
		System.arraycopy(strs, 0, aimStrs, 0, length);
		return String.valueOf(aimStrs) + ellipsisStr;
	}
	/**
	 * @author hch
	 * @param str
	 * @return 去除标签后的字符串
	 */
	public static String removeLabel(String str){
		if(str!=null&&str.length()>0){
			str=str.replaceAll("<br/?>", "\r\n");
			str=str.replaceAll("</?[a-zA-Z]+[^><]*>", "");
			return str.replaceAll("&nbsp;", " "); 	
		}else{
			return "";
		}
	}
	/**
	 * @author hch
	 * @param url 路径
	 * @return 根据路径获取node编号
	 */
	public String getNodeId(String url){
		if(url!=null&&url.length()>0){
		String[] values=url.split("/");
			return values[values.length-1];
		}else{
			return "";
		}
	}
	/**
	 * @author hch
	 * @param str
	 * @param length 截取位数
	 * @return 根据截取位置返回末尾的字符串
	 */
	public String getLastStr(String str,int length){
		if(str!=null&&str.length()>0){
			if(str.length()>length){
				return str.substring(str.length()-length,str.length());
			}else{
				return str;
			}
		}else{
			return "";
		}
	}
	/**
	 * @author hch
	 * @param str 字符串
	 * @param endNumber 截止位置
	 * @param repalceStr 替换内容
	 * @return 返回替换的内容
	 */
	public String getReplaceStr(String str,int endNumber,String replaceStr){
		StringBuilder result=new StringBuilder();
		char[] c=str.toCharArray();
		for(int i=0;i<c.length;i++){
			if(i<endNumber){
				result.append(c[i]);
			}else{
				result.append(replaceStr);
			}
		}
		return result.toString();
	}
	public static void main(String[] args){
		System.out.println(new StringHome().removeLabel("<p><span style=\"line-height: 0px;\">﻿</span>担保信息详述<br/>2222222222222222222222222222222222</p>"));
	}
}
