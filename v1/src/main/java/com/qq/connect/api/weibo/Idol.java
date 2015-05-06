/*     */package com.qq.connect.api.weibo;

/*     */
/*     */import com.qq.connect.QQConnect;
/*     */
import com.qq.connect.QQConnectException;
/*     */
import com.qq.connect.javabeans.GeneralResultBean;
/*     */
import com.qq.connect.utils.QQConnectConfig;
/*     */
import com.qq.connect.utils.http.HttpClient;
/*     */
import com.qq.connect.utils.http.PostParameter;
/*     */
import com.qq.connect.utils.http.Response;

/*     */
/*     */public class Idol extends QQConnect
/*     */{
	/*     */private static final long serialVersionUID = -6124397423510235640L;

	/*     */
	/*     */public Idol(String token, String openID)
	/*     */{
		/* 28 */super(token, openID);
		/*     */}

	/*     */
	/*     */private GeneralResultBean sendRequest(String openid, String parameter,
			int flag, int flag2) throws QQConnectException
	/*     */{
		/* 33 */PostParameter[] parameters = null;
		/* 34 */if (flag == 1)
		/*     */{
			/* 36 */parameters = new PostParameter[] {
					new PostParameter("name", parameter),
					new PostParameter("openid", openid),
					new PostParameter("oauth_consumer_key",
							QQConnectConfig.getValue("app_ID")),
					new PostParameter("access_token", this.client.getToken()),
					new PostParameter("format", "json") };
			/*     */}
		/* 46 */else if (flag2 == 1) {
			/* 47 */parameters = new PostParameter[] {
					new PostParameter("fopenids", parameter),
					new PostParameter("openid", openid),
					new PostParameter("oauth_consumer_key",
							QQConnectConfig.getValue("app_ID")),
					new PostParameter("access_token", this.client.getToken()),
					new PostParameter("format", "json") };
			/*     */}
		/*     */else
		/*     */{
			/* 55 */parameters = new PostParameter[] {
					new PostParameter("fopenid", parameter),
					new PostParameter("openid", openid),
					new PostParameter("oauth_consumer_key",
							QQConnectConfig.getValue("app_ID")),
					new PostParameter("access_token", this.client.getToken()),
					new PostParameter("format", "json") };
			/*     */}
		/*     */
		/* 65 */String url = "";
		/* 66 */if (flag2 == 1)
		/*     */{
			/* 68 */url = "addIdolURL";
			/*     */}
		/*     */else {
			/* 71 */url = "delIdolURL";
			/*     */}
		/*     */
		/* 75 */return new GeneralResultBean(this.client.post(
				QQConnectConfig.getValue(url), parameters).asJSONObject());
		/*     */}

	/*     */
	/*     */public GeneralResultBean addIdolByName(String name)
	/*     */throws QQConnectException
	/*     */{
		/* 93 */return sendRequest(this.client.getOpenID(), name, 1, 1);
		/*     */}

	/*     */
	/*     */public GeneralResultBean addIdolByOpenID(String fopenids)
	/*     */throws QQConnectException
	/*     */{
		/* 106 */return sendRequest(this.client.getOpenID(), fopenids, 2, 1);
		/*     */}

	/*     */
	/*     */public GeneralResultBean delIdolByName(String name)
	/*     */throws QQConnectException
	/*     */{
		/* 121 */return sendRequest(this.client.getOpenID(), name, 1, 2);
		/*     */}

	/*     */
	/*     */public GeneralResultBean delIdolByOpenID(String fopenids)
	/*     */throws QQConnectException
	/*     */{
		/* 134 */return sendRequest(this.client.getOpenID(), fopenids, 2, 2);
		/*     */}
	/*     */
}
