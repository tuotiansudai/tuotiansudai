/*    */package com.qq.connect.api;

/*    */
/*    */import com.qq.connect.QQConnect;
/*    */
import com.qq.connect.QQConnectException;
/*    */
import com.qq.connect.utils.QQConnectConfig;
/*    */
import com.qq.connect.utils.http.HttpClient;
/*    */
import com.qq.connect.utils.http.PostParameter;
/*    */
import com.qq.connect.utils.http.Response;
/*    */
import java.util.regex.Matcher;
/*    */
import java.util.regex.Pattern;

/*    */
/*    */public class OpenID extends QQConnect
/*    */{
	/*    */private static final long serialVersionUID = 6913005509508673584L;

	/*    */
	/*    */public OpenID(String token)
	/*    */{
		/* 26 */this.client.setToken(token);
		/*    */}

	/*    */
	/*    */private String getUserOpenID(String accessToken)
	/*    */throws QQConnectException
	/*    */{
		/* 43 */String openid = "";
		/* 44 */String jsonp = this.client.get(
				QQConnectConfig.getValue("getOpenIDURL"),
				new PostParameter[] { new PostParameter("access_token",
						accessToken) }).asString();
		/*    */
		/* 49 */Matcher m = Pattern.compile("\"openid\"\\s*:\\s*\"(\\w+)\"")
				.matcher(jsonp);
		/*    */
		/* 51 */if (m.find())
			/* 52 */openid = m.group(1);
		/*    */else {
			/* 54 */throw new QQConnectException("server error!");
			/*    */}
		/* 56 */return openid;
		/*    */}

	/*    */
	/*    */public String getUserOpenID()
	/*    */throws QQConnectException
	/*    */{
		/* 70 */String accessToken = this.client.getToken();
		/* 71 */return getUserOpenID(accessToken);
		/*    */}
	/*    */
}
