/*    */package com.qq.connect.api.qzone;

/*    */
/*    */import com.qq.connect.QQConnect;
/*    */
import com.qq.connect.QQConnectException;
/*    */
import com.qq.connect.javabeans.qzone.PageFansBean;
/*    */
import com.qq.connect.utils.QQConnectConfig;
/*    */
import com.qq.connect.utils.http.HttpClient;
/*    */
import com.qq.connect.utils.http.PostParameter;
/*    */
import com.qq.connect.utils.http.Response;

/*    */
/*    */public class PageFans extends QQConnect
/*    */{
	/*    */private static final long serialVersionUID = -3088533004308446275L;

	/*    */
	/*    */public PageFans(String token, String openID)
	/*    */{
		/* 26 */super(token, openID);
		/*    */}

	/*    */
	/*    */private PageFansBean checkPageFans(PostParameter[] parameters)
			throws QQConnectException {
		/* 30 */return new PageFansBean(this.client.get(
				QQConnectConfig.getValue("checkPageFansURL"), parameters)
				.asJSONObject());
		/*    */}

	/*    */
	/*    */public PageFansBean checkPageFans(String pageID)
	/*    */throws QQConnectException
	/*    */{
		/* 50 */return checkPageFans(new PostParameter[] {
				new PostParameter("page_id", pageID),
				new PostParameter("format", "json"),
				new PostParameter("access_token", this.client.getToken()),
				new PostParameter("oauth_consumer_key",
						QQConnectConfig.getValue("app_ID")),
				new PostParameter("openid", this.client.getOpenID()) });
		/*    */}
	/*    */
}
