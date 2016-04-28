/*     */package com.qq.connect.api.weibo;

/*     */
/*     */import com.qq.connect.QQConnect;
/*     */
import com.qq.connect.QQConnectException;
/*     */
import com.qq.connect.javabeans.weibo.FansIdolsBean;
/*     */
import com.qq.connect.javabeans.weibo.UserInfoBean;
/*     */
import com.qq.connect.utils.QQConnectConfig;
/*     */
import com.qq.connect.utils.http.HttpClient;
/*     */
import com.qq.connect.utils.http.PostParameter;
/*     */
import com.qq.connect.utils.http.Response;
/*     */
import java.util.ArrayList;

/*     */
/*     */public class UserInfo extends QQConnect
/*     */{
	/*     */private static final long serialVersionUID = -6124397423510235640L;

	/*     */
	/*     */public UserInfo(String token, String openID)
	/*     */{
		/* 31 */super(token, openID);
		/*     */}

	/*     */
	/*     */private UserInfoBean getUserInfo(String openid)
	/*     */throws QQConnectException
	/*     */{
		/* 46 */return new UserInfoBean(this.client.get(
				QQConnectConfig.getValue("getWeiboUserInfoURL"),
				new PostParameter[] {
						new PostParameter("openid", openid),
						new PostParameter("oauth_consumer_key", QQConnectConfig
								.getValue("app_ID")),
						new PostParameter("access_token", this.client
								.getToken()),
						new PostParameter("format", "json") }).asJSONObject());
		/*     */}

	/*     */
	/*     */public UserInfoBean getUserInfo()
	/*     */throws QQConnectException
	/*     */{
		/* 65 */return getUserInfo(this.client.getOpenID());
		/*     */}

	/*     */
	/*     */public FansIdolsBean getFansList(int reqnum, int startIndex,
			String[] parameters)
	/*     */throws QQConnectException
	/*     */{
		/* 92 */ArrayList postParameterArray = new ArrayList();
		/*     */
		/* 95 */postParameterArray.add(new PostParameter("reqnum", reqnum));
		/* 96 */postParameterArray.add(new PostParameter("startindex",
				startIndex));
		/* 97 */for (String parameter : parameters) {
			/* 98 */if (parameter.indexOf("mode=") == 0)
				/* 99 */postParameterArray.add(new PostParameter("mode",
						parameter.substring(5)));
			/* 100 */else if (parameter.indexOf("install=") == 0)
				/* 101 */postParameterArray.add(new PostParameter("install",
						parameter.substring(8)));
			/* 102 */else if (parameter.indexOf("sex=") == 0)
				/* 103 */postParameterArray.add(new PostParameter("sex",
						parameter.substring(4)));
			/*     */else {
				/* 105 */throw new QQConnectException(
						"you pass one illegal parameter");
				/*     */}
			/*     */
			/*     */}
		/*     */
		/* 110 */postParameterArray.add(new PostParameter("format", "json"));
		/* 111 */postParameterArray.add(new PostParameter("access_token",
				this.client.getToken()));
		/* 112 */postParameterArray.add(new PostParameter("oauth_consumer_key",
				QQConnectConfig.getValue("app_ID")));
		/* 113 */postParameterArray.add(new PostParameter("openid", this.client
				.getOpenID()));
		/* 114 */PostParameter[] parameters1 = (PostParameter[]) postParameterArray
				.toArray(new PostParameter[1]);
		/*     */
		/* 116 */return new FansIdolsBean(this.client.get(
				QQConnectConfig.getValue("getFansListURL"), parameters1)
				.asJSONObject());
		/*     */}

	/*     */
	/*     */public FansIdolsBean getIdolsList(int reqnum, int startIndex,
			String[] parameters)
	/*     */throws QQConnectException
	/*     */{
		/* 142 */ArrayList postParameterArray = new ArrayList();
		/*     */
		/* 144 */postParameterArray.add(new PostParameter("reqnum", reqnum));
		/* 145 */postParameterArray.add(new PostParameter("startindex",
				startIndex));
		/*     */
		/* 147 */for (String parameter : parameters) {
			/* 148 */if (parameter.indexOf("mode=") == 0)
				/* 149 */postParameterArray.add(new PostParameter("mode",
						parameter.substring(5)));
			/* 150 */else if (parameter.indexOf("install=") == 0)
				/* 151 */postParameterArray.add(new PostParameter("install",
						parameter.substring(8)));
			/*     */else {
				/* 153 */throw new QQConnectException(
						"you pass one illegal parameter");
				/*     */}
			/*     */
			/*     */}
		/*     */
		/* 158 */postParameterArray.add(new PostParameter("format", "json"));
		/* 159 */postParameterArray.add(new PostParameter("access_token",
				this.client.getToken()));
		/* 160 */postParameterArray.add(new PostParameter("oauth_consumer_key",
				QQConnectConfig.getValue("app_ID")));
		/* 161 */postParameterArray.add(new PostParameter("openid", this.client
				.getOpenID()));
		/* 162 */PostParameter[] parameters1 = (PostParameter[]) postParameterArray
				.toArray(new PostParameter[1]);
		/*     */
		/* 164 */return new FansIdolsBean(this.client.get(
				QQConnectConfig.getValue("getIdolsListURL"), parameters1)
				.asJSONObject());
		/*     */}
	/*     */
}
