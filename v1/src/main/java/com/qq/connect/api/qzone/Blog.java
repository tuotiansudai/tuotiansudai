/*    */package com.qq.connect.api.qzone;

/*    */
/*    */import com.qq.connect.QQConnect;
/*    */
import com.qq.connect.QQConnectException;
/*    */
import com.qq.connect.javabeans.GeneralResultBean;
/*    */
import com.qq.connect.utils.QQConnectConfig;
/*    */
import com.qq.connect.utils.http.HttpClient;
/*    */
import com.qq.connect.utils.http.PostParameter;
/*    */
import com.qq.connect.utils.http.Response;
/*    */
import java.util.ArrayList;

/*    */
/*    */public class Blog extends QQConnect
/*    */{
	/*    */private static final long serialVersionUID = -6962921164439096289L;

	/*    */
	/*    */public Blog(String token, String openID)
	/*    */{
		/* 29 */super(token, openID);
		/*    */}

	/*    */
	/*    */private GeneralResultBean addBlog(PostParameter[] parameters)
	/*    */throws QQConnectException
	/*    */{
		/* 45 */return new GeneralResultBean(this.client.post(
				QQConnectConfig.getValue("addBlogURL"), parameters)
				.asJSONObject());
		/*    */}

	/*    */
	/*    */public GeneralResultBean addBlog(String title, String content)
	/*    */throws QQConnectException
	/*    */{
		/* 71 */ArrayList postParameterArray = new ArrayList();
		/*    */
		/* 75 */postParameterArray.add(new PostParameter("title", title));
		/* 76 */postParameterArray.add(new PostParameter("content", content));
		/* 77 */postParameterArray.add(new PostParameter("format", "json"));
		/* 78 */postParameterArray.add(new PostParameter("access_token",
				this.client.getToken()));
		/* 79 */postParameterArray.add(new PostParameter("oauth_consumer_key",
				QQConnectConfig.getValue("app_ID")));
		/* 80 */postParameterArray.add(new PostParameter("openid", this.client
				.getOpenID()));
		/*    */
		/* 83 */return addBlog((PostParameter[]) postParameterArray
				.toArray(new PostParameter[1]));
		/*    */}
	/*    */
}
