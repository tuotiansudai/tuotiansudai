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
/*     */public class Topic extends QQConnect
/*     */{
	/*     */private static final long serialVersionUID = -3088533004308446275L;

	/*     */
	/*     */public Topic(String token, String openID)
	/*     */{
		/* 31 */super(token, openID);
		/*     */}

	/*     */
	/*     */private GeneralResultBean addTopic(PostParameter[] parameters)
	/*     */throws QQConnectException
	/*     */{
		/* 53 */return new GeneralResultBean(this.client.post(
				QQConnectConfig.getValue("addTopicURL"), parameters)
				.asJSONObject());
		/*     */}

	/*     */
	/*     */public GeneralResultBean addTopic(String con, String[] parameters)
	/*     */throws QQConnectException
	/*     */{
		/* 86 */ArrayList postParameterArray = new ArrayList();
		/*     */
		/* 88 */postParameterArray.add(new PostParameter("con", con));
		/*     */
		/* 90 */for (String parameter : parameters) {
			/* 91 */if (parameter.indexOf("richtype=") == 0)
				/* 92 */postParameterArray.add(new PostParameter("richtype",
						parameter.substring(9)));
			/* 93 */else if (parameter.indexOf("richval=") == 0)
				/* 94 */postParameterArray.add(new PostParameter("richval",
						parameter.substring(8)));
			/* 95 */else if (parameter.indexOf("lbs_nm=") == 0)
				/* 96 */postParameterArray.add(new PostParameter("lbs_nm",
						parameter.substring(7)));
			/* 97 */else if (parameter.indexOf("lbs_x=") == 0)
				/* 98 */postParameterArray.add(new PostParameter("lbs_x",
						parameter.substring(6)));
			/* 99 */else if (parameter.indexOf("lbs_y=") == 0)
				/* 100 */postParameterArray.add(new PostParameter("lbs_y",
						parameter.substring(6)));
			/* 101 */else if (parameter.indexOf("third_source=") == 0)
				/* 102 */postParameterArray.add(new PostParameter(
						"third_source", parameter.substring(13)));
			/*     */else {
				/* 104 */throw new QQConnectException(
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
		/*     */
		/* 116 */return addTopic((PostParameter[]) postParameterArray
				.toArray(new PostParameter[1]));
		/*     */}
	/*     */
}
