package com.esoft.umpay.sign.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.esoft.umpay.trusteeship.UmPayConstants;
import com.umpay.api.exception.VerifyException;
import com.umpay.api.paygate.v40.Plat2Mer_v40;

/**
 * Description : 加密解密工具类
 * 
 * @author zt
 * @data 2015-3-9下午7:43:38
 */
public class UmPaySignUtil {

	/**
	 * 获取即将发送的map
	 * 
	 * @return 参数列表信息map
	 */
	public static Map<String, String> getSendMapDate(String operationType) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("service", operationType);
		map.put("sign_type", "RSA");
		map.put("charset", "UTF-8");
		map.put("res_format", "HTML");
		map.put("mer_id", UmPayConstants.Config.MER_CODE);
		map.put("version", UmPayConstants.Config.VERSION);
		return map;

	}

	/**
	 * 解密同时,获取参数
	 * 
	 * @param request
	 * @return 参数集合
	 * @throws VerifyException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getMapDataByRequest(ServletRequest request)
			throws VerifyException {
		HttpServletRequest htRequest = (HttpServletRequest) request;
		Map fieldMap = new HashMap();
		Enumeration names = htRequest.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String values = htRequest.getParameter(name);
			if (values != null) {
				// TODO 如果是本地环境需要将这个开启关闭下面的,获取'中文有问题'
				try {
					values = new String(htRequest.getParameter(name).getBytes(
							"iso-8859-1"), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				/* values = htRequest.getParameter(name); */
			}
			fieldMap.put(name, values);
		}
		// request里面多了一个参数
		fieldMap.remove("com.ocpsoft.vP_0");
		System.out.println("UMPAY通知P2P====" + fieldMap.get("service")
				+ "=====>信息:" + fieldMap.toString());
		// 验签
		Map platNotifyData = Plat2Mer_v40.getPlatNotifyData(fieldMap);
		return platNotifyData;
	}

}
