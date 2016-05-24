/*     */package com.qq.connect.api.qzone;

/*     */
/*     */import com.qq.connect.QQConnect;
/*     */
import com.qq.connect.QQConnectException;
/*     */
import com.qq.connect.javabeans.qzone.AlbumBean;
/*     */
import com.qq.connect.javabeans.qzone.AlbumPicBean;
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
import org.json.JSONArray;
/*     */
import org.json.JSONException;
/*     */
import org.json.JSONObject;
/*     */
import java.util.ArrayList;

/*     */
/*     */public class Album extends QQConnect
/*     */{
	/*     */private static final long serialVersionUID = -6962921164439096289L;

	/*     */
	/*     */public Album(String token, String openID)
	/*     */{
		/* 35 */super(token, openID);
		/*     */}

	/*     */
	/*     */private AlbumBean addAlbum(PostParameter[] parameters)
			throws QQConnectException
	/*     */{
		/* 40 */return new AlbumBean(this.client.post(
				QQConnectConfig.getValue("addAlbumURL"), parameters)
				.asJSONObject());
		/*     */}

	/*     */
	/*     */public AlbumBean addAlbum(String albumname, String[] parameters)
	/*     */throws QQConnectException
	/*     */{
		/* 66 */ArrayList postParameterArray = new ArrayList();
		/*     */
		/* 68 */postParameterArray
				.add(new PostParameter("albumname", albumname));
		/* 69 */for (String parameter : parameters) {
			/* 70 */if (parameter.indexOf("albumdesc") == 0)
				/* 71 */postParameterArray.add(new PostParameter("albumdesc",
						parameter.substring(10)));
			/* 72 */else if (parameter.indexOf("priv") == 0)
				/* 73 */postParameterArray.add(new PostParameter("priv",
						parameter.substring(5)));
			/*     */else {
				/* 75 */throw new QQConnectException(
						"you pass one illegal parameter");
				/*     */}
			/*     */
			/*     */}
		/*     */
		/* 80 */postParameterArray.add(new PostParameter("format", "json"));
		/* 81 */postParameterArray.add(new PostParameter("access_token",
				this.client.getToken()));
		/* 82 */postParameterArray.add(new PostParameter("oauth_consumer_key",
				QQConnectConfig.getValue("app_ID")));
		/* 83 */postParameterArray.add(new PostParameter("openid", this.client
				.getOpenID()));
		/*     */
		/* 86 */return addAlbum((PostParameter[]) postParameterArray
				.toArray(new PostParameter[1]));
		/*     */}

	/*     */
	/*     */private ArrayList<AlbumBean> listAlbum(PostParameter[] parameters)
	/*     */throws QQConnectException, JSONException
	/*     */{
		/* 93 */ArrayList abs = new ArrayList();
		/*     */
		/* 95 */JSONObject returnJSONObj = this.client.post(
				QQConnectConfig.getValue("listAlbumURL"), parameters)
				.asJSONObject();
		/*     */
		/* 101 */int ret = returnJSONObj.getInt("ret");
		/* 102 */if (0 == ret)
		/*     */{
			/* 104 */int albumnum = returnJSONObj.getInt("albumnum");
			/* 105 */JSONArray jsonA = returnJSONObj.getJSONArray("album");
			/* 106 */for (int i = 0; i < albumnum; i++) {
				/* 107 */JSONObject jo = jsonA.getJSONObject(i);
				/* 108 */abs.add(new AlbumBean(jo.getString("albumid"), jo
						.getString("classid"), Long.valueOf(jo
						.getLong("createtime")), jo.getString("desc"), jo
						.getString("name"), jo.getInt("priv"), jo
						.getString("coverurl"), jo.getInt("picnum")));
				/*     */}
			/*     */
			/*     */}
		/*     */else
		/*     */{
			/* 119 */abs
					.add(new AlbumBean(ret, returnJSONObj.getString("msg")));
			/*     */}
		/*     */
		/* 122 */return abs;
		/*     */}

	/*     */
	/*     */public ArrayList<AlbumBean> listAlbum()
	/*     */throws QQConnectException, JSONException
	/*     */{
		/* 137 */ArrayList postParameterArray = new ArrayList();
		/*     */
		/* 141 */postParameterArray.add(new PostParameter("format", "json"));
		/* 142 */postParameterArray.add(new PostParameter("access_token",
				this.client.getToken()));
		/* 143 */postParameterArray.add(new PostParameter("oauth_consumer_key",
				QQConnectConfig.getValue("app_ID")));
		/* 144 */postParameterArray.add(new PostParameter("openid", this.client
				.getOpenID()));
		/*     */
		/* 147 */return listAlbum((PostParameter[]) postParameterArray
				.toArray(new PostParameter[1]));
		/*     */}

	/*     */
	/*     */public AlbumPicBean uploadPic(byte[] picture, String[] parameters)
	/*     */throws QQConnectException
	/*     */{
		/* 200 */ArrayList postParameterArray = new ArrayList();
		/* 201 */int flag = 0;
		/*     */
		/* 203 */for (String parameter : parameters) {
			/* 204 */if (parameter.indexOf("photodesc=") == 0)
				/* 205 */postParameterArray.add(new PostParameter("photodesc",
						parameter.substring(10)));
			/* 206 */else if (parameter.indexOf("title=") == 0)
				/* 207 */postParameterArray.add(new PostParameter("title",
						parameter.substring(6)));
			/* 208 */else if (parameter.indexOf("albumid=") == 0)
				/* 209 */postParameterArray.add(new PostParameter("albumid",
						parameter.substring(8)));
			/* 210 */else if (parameter.indexOf("x=") == 0)
				/* 211 */postParameterArray.add(new PostParameter("x",
						parameter.substring(2)));
			/* 212 */else if (parameter.indexOf("y=") == 0)
				/* 213 */postParameterArray.add(new PostParameter("y",
						parameter.substring(2)));
			/* 214 */else if (parameter.indexOf("mobile=") == 0)
				/* 215 */postParameterArray.add(new PostParameter("mobile",
						parameter.substring(7)));
			/* 216 */else if (parameter.indexOf("needfeed=") == 0)
				/* 217 */postParameterArray.add(new PostParameter("needfeed",
						parameter.substring(9)));
			/* 218 */else if (parameter.indexOf("successnum=") == 0)
				/* 219 */postParameterArray.add(new PostParameter("successnum",
						parameter.substring(11)));
			/* 220 */else if (parameter.indexOf("picnum=") == 0)
				/* 221 */postParameterArray.add(new PostParameter("picnum",
						parameter.substring(7)));
			/*     */else {
				/* 223 */throw new QQConnectException(
						"you pass one illegal parameter");
				/*     */}
			/*     */
			/*     */}
		/*     */
		/* 228 */ImageItem image = new ImageItem("picture", picture);
		/*     */
		/* 231 */postParameterArray.add(new PostParameter("format", "json"));
		/* 232 */postParameterArray.add(new PostParameter("access_token",
				this.client.getToken()));
		/* 233 */postParameterArray.add(new PostParameter("oauth_consumer_key",
				QQConnectConfig.getValue("app_ID")));
		/* 234 */postParameterArray.add(new PostParameter("openid", this.client
				.getOpenID()));
		/*     */
		/* 237 */PostParameter[] parameters1 = (PostParameter[]) postParameterArray
				.toArray(new PostParameter[1]);
		/*     */
		/* 239 */return new AlbumPicBean(this.client.multPartURL(
				QQConnectConfig.getValue("uploadPicURL"), parameters1, image)
				.asJSONObject());
		/*     */}
	/*     */
}
