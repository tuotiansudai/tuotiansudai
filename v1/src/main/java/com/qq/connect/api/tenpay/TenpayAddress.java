/*    */package com.qq.connect.api.tenpay;

/*    */
/*    */import com.qq.connect.QQConnect;
/*    */
import com.qq.connect.QQConnectException;
/*    */
import com.qq.connect.javabeans.tenpay.TenpayAddressBean;
/*    */
import com.qq.connect.utils.QQConnectConfig;
/*    */
import com.qq.connect.utils.http.HttpClient;
/*    */
import com.qq.connect.utils.http.PostParameter;
/*    */
import com.qq.connect.utils.http.Response;

/*    */
/*    */public class TenpayAddress extends QQConnect
/*    */{
	/*    */private static final long serialVersionUID = -6124397423510235640L;

	/*    */
	/*    */public TenpayAddress(String token, String openID)
	/*    */{
		/* 22 */super(token, openID);
		/*    */}

	/*    */
	/*    */private TenpayAddressBean getAddress(String openid, int offset,
			int limit, String ver) throws QQConnectException
	/*    */{
		/* 27 */return new TenpayAddressBean(this.client.post(
				QQConnectConfig.getValue("getTenpayAddrURL"),
				new PostParameter[] {
						new PostParameter("openid", openid),
						new PostParameter("offset", offset),
						new PostParameter("limit", limit),
						new PostParameter("ver", ver),
						new PostParameter("oauth_consumer_key", QQConnectConfig
								.getValue("app_ID")),
						new PostParameter("access_token", this.client
								.getToken()),
						new PostParameter("format", "json") }).asJSONObject());
		/*    */}

	/*    */
	/*    */public TenpayAddressBean getAddress(int offset, int limit, String ver)
	/*    */throws QQConnectException
	/*    */{
		/* 52 */return getAddress(this.client.getOpenID(), offset, limit, ver);
		/*    */}

	/*    */
	/*    */public TenpayAddressBean getAddress(int offset, int limit)
	/*    */throws QQConnectException
	/*    */{
		/* 66 */return getAddress(offset, limit, "1");
		/*    */}

	/*    */
	/*    */public TenpayAddressBean getAddress()
	/*    */throws QQConnectException
	/*    */{
		/* 76 */return getAddress(0, 5);
		/*    */}

	/*    */
	/*    */public TenpayAddressBean getAddress(int offset)
	/*    */throws QQConnectException
	/*    */{
		/* 88 */return getAddress(offset, 5);
		/*    */}
	/*    */
}
