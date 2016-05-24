/*     */package com.qq.connect.api.weibo;

/*     */
/*     */import com.qq.connect.QQConnect;
/*     */
import com.qq.connect.QQConnectException;
/*     */
import com.qq.connect.javabeans.weibo.RepostInfoBean;
/*     */
import com.qq.connect.javabeans.weibo.WeiboBean;
/*     */
import com.qq.connect.utils.QQConnectConfig;
/*     */
import com.qq.connect.utils.http.HttpClient;
/*     */
import com.qq.connect.utils.http.ImageItem;
/*     */
import com.qq.connect.utils.http.PostParameter;
/*     */
import com.qq.connect.utils.http.Response;
/*     */
import java.util.ArrayList;

/*     */
/*     */public class Weibo extends QQConnect
/*     */{
	/*     */private static final long serialVersionUID = -3088533004308446275L;

	/*     */
	/*     */public Weibo(String token, String openID)
	/*     */{
		/* 31 */super(token, openID);
		/*     */}

	/*     */
	/*     */private WeiboBean send(PostParameter[] parameters, String targetUrl)
			throws QQConnectException {
		/* 35 */return new WeiboBean(this.client.post(
				QQConnectConfig.getValue(targetUrl), parameters).asJSONObject());
		/*     */}

	/*     */
	/*     */public WeiboBean delWeibo(String weiboID)
	/*     */throws QQConnectException
	/*     */{
		/* 53 */return send(
				new PostParameter[] {
						new PostParameter("id", weiboID),
						new PostParameter("format", "json"),
						new PostParameter("access_token",
								this.client.getToken()),
						new PostParameter("oauth_consumer_key",
								QQConnectConfig.getValue("app_ID")),
						new PostParameter("openid", this.client.getOpenID()) },
				"delTURL");
		/*     */}

	/*     */
	/*     */public WeiboBean addWeibo(String content, String[] parameters)
	/*     */throws QQConnectException
	/*     */{
		/* 81 */ArrayList postParameterArray = new ArrayList();
		/*     */
		/* 83 */postParameterArray.add(new PostParameter("content", content));
		/*     */
		/* 85 */for (String parameter : parameters) {
			/* 86 */if (parameter.indexOf("clientip") == 0)
				/* 87 */postParameterArray.add(new PostParameter("clientip",
						parameter.substring(9)));
			/* 88 */else if (parameter.indexOf("longitude") == 0)
				/* 89 */postParameterArray.add(new PostParameter("longitude",
						parameter.substring(10)));
			/* 90 */else if (parameter.indexOf("latitude") == 0)
				/* 91 */postParameterArray.add(new PostParameter("latitude",
						parameter.substring(9)));
			/* 92 */else if (parameter.indexOf("syncflag") == 0)
				/* 93 */postParameterArray.add(new PostParameter("syncflag",
						parameter.substring(9)));
			/* 94 */else if (parameter.indexOf("compatibleflag") == 0)
				/* 95 */postParameterArray.add(new PostParameter(
						"compatibleflag", parameter.substring(6)));
			/*     */else {
				/* 97 */throw new QQConnectException(
						"you pass one illegal parameter");
				/*     */}
			/*     */
			/*     */}
		/*     */
		/* 102 */postParameterArray.add(new PostParameter("format", "json"));
		/* 103 */postParameterArray.add(new PostParameter("access_token",
				this.client.getToken()));
		/* 104 */postParameterArray.add(new PostParameter("oauth_consumer_key",
				QQConnectConfig.getValue("app_ID")));
		/* 105 */postParameterArray.add(new PostParameter("openid", this.client
				.getOpenID()));
		/*     */
		/* 108 */return send(
				(PostParameter[]) postParameterArray
						.toArray(new PostParameter[1]),
				"addTURL");
		/*     */}

	/*     */
	/*     */public RepostInfoBean getRepostList(int flag, String rootID,
			int pageFlag, String pageTime, int reqNum, String twitterID)
	/*     */throws QQConnectException
	/*     */{
		/* 129 */PostParameter[] parameters = null;
		/*     */
		/* 131 */parameters = new PostParameter[] {
				new PostParameter("flag", flag),
				new PostParameter("rootid", rootID),
				new PostParameter("pageflag", pageFlag),
				new PostParameter("pagetime", pageTime),
				new PostParameter("reqnum", reqNum),
				new PostParameter("twitterid", twitterID),
				new PostParameter("format", "json"),
				new PostParameter("access_token", this.client.getToken()),
				new PostParameter("oauth_consumer_key",
						QQConnectConfig.getValue("app_ID")),
				new PostParameter("openid", this.client.getOpenID()) };
		/*     */
		/* 144 */return new RepostInfoBean(this.client.get(
				QQConnectConfig.getValue("getRepostListURL"), parameters)
				.asJSONObject());
		/*     */}

	/*     */
	/*     */public WeiboBean addPicWeibo(String content, byte[] pic,
			String[] parameters)
	/*     */throws QQConnectException
	/*     */{
		/* 207 */ArrayList postParameterArray = new ArrayList();
		/*     */
		/* 209 */postParameterArray.add(new PostParameter("content", content));
		/* 210 */for (String parameter : parameters) {
			/* 211 */if (parameter.indexOf("clientip=") == 0)
				/* 212 */postParameterArray.add(new PostParameter("clientip",
						parameter.substring(9)));
			/* 213 */else if (parameter.indexOf("longitude=") == 0)
				/* 214 */postParameterArray.add(new PostParameter("longitude",
						parameter.substring(10)));
			/* 215 */else if (parameter.indexOf("latitude=") == 0)
				/* 216 */postParameterArray.add(new PostParameter("latitude",
						parameter.substring(9)));
			/* 217 */else if (parameter.indexOf("syncflag=") == 0)
				/* 218 */postParameterArray.add(new PostParameter("syncflag",
						parameter.substring(9)));
			/* 219 */else if (parameter.indexOf("compatibleflag=") == 0)
				/* 220 */postParameterArray.add(new PostParameter(
						"compatibleflag", parameter.substring(6)));
			/*     */else {
				/* 222 */throw new QQConnectException(
						"you pass one illegal parameter");
				/*     */}
			/*     */
			/*     */}
		/*     */
		/* 228 */ImageItem image = new ImageItem("pic", pic);
		/* 229 */postParameterArray.add(new PostParameter("format", "json"));
		/* 230 */postParameterArray.add(new PostParameter("access_token",
				this.client.getToken()));
		/* 231 */postParameterArray.add(new PostParameter("oauth_consumer_key",
				QQConnectConfig.getValue("app_ID")));
		/* 232 */postParameterArray.add(new PostParameter("openid", this.client
				.getOpenID()));
		/*     */
		/* 235 */return new WeiboBean(this.client.multPartURL(
				QQConnectConfig.getValue("addPicTURL"),
				(PostParameter[]) postParameterArray
						.toArray(new PostParameter[1]), image).asJSONObject());
		/*     */}
	/*     */
}
