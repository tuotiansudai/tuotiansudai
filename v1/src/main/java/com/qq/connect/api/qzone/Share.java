/*     */package com.qq.connect.api.qzone;

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
import java.util.ArrayList;

/*     */
/*     */public class Share extends QQConnect
/*     */{
	/*     */private static final long serialVersionUID = -3088533004308446275L;

	/*     */
	/*     */public Share(String token, String openID)
	/*     */{
		/* 31 */super(token, openID);
		/*     */}

	/*     */
	/*     */private GeneralResultBean addShare(PostParameter[] parameters)
	/*     */throws QQConnectException
	/*     */{
		/* 38 */return new GeneralResultBean(this.client.post(
				QQConnectConfig.getValue("addShareURL"), parameters)
				.asJSONObject());
		/*     */}

	/*     */
	/*     */public GeneralResultBean addShare(String title, String url,
			String site, String fromUrl, String[] parameters)
	/*     */throws QQConnectException
	/*     */{
		/* 77 */ArrayList postParameterArray = new ArrayList();
		/*     */
		/* 79 */postParameterArray.add(new PostParameter("title", title));
		/* 80 */postParameterArray.add(new PostParameter("url", url));
		/* 81 */postParameterArray.add(new PostParameter("site", site));
		/* 82 */postParameterArray.add(new PostParameter("fromurl", fromUrl));
		/*     */
		/* 86 */for (String parameter : parameters) {
			/* 87 */if (parameter.indexOf("comment") == 0) {
				/* 88 */postParameterArray.add(new PostParameter("comment",
						parameter.substring(8)));
				/*     */}
			/* 90 */else if (parameter.indexOf("summary") == 0) {
				/* 91 */postParameterArray.add(new PostParameter("summary",
						parameter.substring(8)));
				/*     */}
			/* 93 */else if (parameter.indexOf("images") == 0) {
				/* 94 */postParameterArray.add(new PostParameter("images",
						parameter.substring(7)));
				/*     */}
			/* 96 */else if (parameter.indexOf("type") == 0) {
				/* 97 */postParameterArray.add(new PostParameter("type",
						parameter.substring(5)));
				/*     */}
			/* 99 */else if (parameter.indexOf("playurl") == 0) {
				/* 100 */postParameterArray.add(new PostParameter("playurl",
						parameter.substring(8)));
				/*     */}
			/* 102 */else if (parameter.indexOf("nswb") == 0) {
				/* 103 */postParameterArray.add(new PostParameter("nswb",
						parameter.substring(5)));
				/*     */}
			/*     */else {
				/* 106 */throw new QQConnectException(
						"you pass one illegal parameter");
				/*     */}
			/*     */
			/*     */}
		/*     */
		/* 111 */postParameterArray.add(new PostParameter("format", "json"));
		/* 112 */postParameterArray.add(new PostParameter("access_token",
				this.client.getToken()));
		/* 113 */postParameterArray.add(new PostParameter("oauth_consumer_key",
				QQConnectConfig.getValue("app_ID")));
		/*     */
		/* 115 */postParameterArray.add(new PostParameter("openid", this.client
				.getOpenID()));
		/*     */
		/* 118 */return addShare((PostParameter[]) postParameterArray
				.toArray(new PostParameter[1]));
		/*     */}
	/*     */
}
