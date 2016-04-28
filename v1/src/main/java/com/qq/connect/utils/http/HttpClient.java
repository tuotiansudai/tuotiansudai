package com.qq.connect.utils.http;

import com.qq.connect.QQConnectException;
import com.qq.connect.utils.Configuration;
import org.json.JSONException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HostParams;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.log4j.Logger;

public class HttpClient implements Serializable {
	private static final long serialVersionUID = 1458439729090743687L;
	private static final int OK = 200;
	private static final int NOT_MODIFIED = 304;
	private static final int BAD_REQUEST = 400;
	private static final int NOT_AUTHORIZED = 401;
	private static final int FORBIDDEN = 403;
	private static final int NOT_FOUND = 404;
	private static final int NOT_ACCEPTABLE = 406;
	private static final int INTERNAL_SERVER_ERROR = 500;
	private static final int BAD_GATEWAY = 502;
	private static final int SERVICE_UNAVAILABLE = 503;
	private String proxyHost = null;
	private int proxyPort = 0;
	private String proxyAuthUser = null;
	private String proxyAuthPassword = null;
	private String token = null;
	private String openID = null;

	private static final boolean DEBUG = Configuration.getDebug();
	static Logger log = Logger.getLogger(HttpClient.class.getName());
	org.apache.commons.httpclient.HttpClient client = null;
	private MultiThreadedHttpConnectionManager connectionManager;
	private int maxSize;

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public String getOpenID() throws QQConnectException {
		if ((this.openID == null) || (this.openID.equals(""))) {
			throw new QQConnectException(
					"please invoke the setOpenID and setToken first!");
		}
		return this.openID;
	}

	public String getProxyHost() {
		return this.proxyHost;
	}

	public int getProxyPort() {
		return this.proxyPort;
	}

	public String getProxyAuthUser() {
		return this.proxyAuthUser;
	}

	public String getProxyAuthPassword() {
		return this.proxyAuthPassword;
	}

	public String setToken(String token) {
		this.token = token;
		return this.token;
	}

	public HttpClient() {
		this(150, 30000, 30000, 1048576);
	}

	public HttpClient(int maxConPerHost, int conTimeOutMs, int soTimeOutMs,
			int maxSize) {
		this.connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = this.connectionManager.getParams();
		params.setDefaultMaxConnectionsPerHost(maxConPerHost);
		params.setConnectionTimeout(conTimeOutMs);
		params.setSoTimeout(soTimeOutMs);

		HttpClientParams clientParams = new HttpClientParams();

		clientParams.setCookiePolicy("ignoreCookies");
		this.client = new org.apache.commons.httpclient.HttpClient(
				clientParams, this.connectionManager);

		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		this.maxSize = maxSize;

		if ((this.proxyHost != null) && (!this.proxyHost.equals(""))) {
			this.client.getHostConfiguration().setProxy(this.proxyHost,
					this.proxyPort);
			this.client.getParams().setAuthenticationPreemptive(true);
			if ((this.proxyAuthUser != null)
					&& (!this.proxyAuthUser.equals(""))) {
				this.client.getState().setProxyCredentials(
						AuthScope.ANY,
						new UsernamePasswordCredentials(this.proxyAuthUser,
								this.proxyAuthPassword));

				log("Proxy AuthUser: " + this.proxyAuthUser);
				log("Proxy AuthPassword: " + this.proxyAuthPassword);
			}
		}
	}

	private static void log(String message) {
		if (DEBUG)
			log.debug(message);
	}

	public Response get(String url) throws QQConnectException {
		return get(url, new PostParameter[0]);
	}

	public Response get(String url, PostParameter[] params)
			throws QQConnectException {
		log("Request:");
		log("GET:" + url);
		if ((null != params) && (params.length > 0)) {
			String encodedParams = encodeParameters(params);
			log("get request parameter : " + encodedParams);
			if (-1 == url.indexOf("?"))
				url = url + "?" + encodedParams;
			else {
				url = url + "&" + encodedParams;
			}
		}
		GetMethod getmethod = new GetMethod(url);
		return httpRequest(getmethod);
	}

	public Response delete(String url, PostParameter[] params)
			throws QQConnectException {
		if (0 != params.length) {
			String encodedParams = encodeParameters(params);
			if (-1 == url.indexOf("?"))
				url = url + "?" + encodedParams;
			else {
				url = url + "&" + encodedParams;
			}
		}
		DeleteMethod deleteMethod = new DeleteMethod(url);
		return httpRequest(deleteMethod);
	}

	public Response post(String url, PostParameter[] params)
			throws QQConnectException {
		return post(url, params, Boolean.valueOf(false));
	}

	public Response post(String url, PostParameter[] params,
			Boolean WithTokenHeader) throws QQConnectException {
		log("Request:");
		log("POST" + url);
		PostMethod postMethod = new PostMethod(url);
		for (int i = 0; i < params.length; i++) {
			postMethod.addParameter(params[i].getName(), params[i].getValue());
		}
		HttpMethodParams param = postMethod.getParams();
		param.setContentCharset("UTF-8");
		if (WithTokenHeader.booleanValue()) {
			return httpRequest(postMethod);
		}
		return httpRequest(postMethod, WithTokenHeader);
	}

	public Response multPartURL(String url, PostParameter[] params,
			ImageItem item) throws QQConnectException {
		PostMethod postMethod = new PostMethod(url);
		try {
			Part[] parts = null;
			if (params == null)
				parts = new Part[1];
			else {
				parts = new Part[params.length + 1];
			}
			if (params != null) {
				int i = 0;
				for (PostParameter entry : params) {
					parts[(i++)] = new StringPart(entry.getName(),
							entry.getValue(), "UTF-8");
				}

				parts[(parts.length - 1)] = new ByteArrayPart(
						item.getContent(), item.getName(),
						item.getContentType());
			}

			postMethod.setRequestEntity(new MultipartRequestEntity(parts,
					postMethod.getParams()));

			return httpRequest(postMethod);
		} catch (Exception ex) {
			throw new QQConnectException(ex.getMessage(), ex, -1);
		}
	}

	private Response multPartURL(String fileParamName, String url,
			PostParameter[] params, File file, boolean authenticated)
			throws QQConnectException {
		PostMethod postMethod = new PostMethod(url);
		try {
			Part[] parts = null;
			if (params == null)
				parts = new Part[1];
			else {
				parts = new Part[params.length + 1];
			}
			if (params != null) {
				int i = 0;
				for (PostParameter entry : params) {
					parts[(i++)] = new StringPart(entry.getName(),
							entry.getValue());
				}
			}

			FilePart filePart = new FilePart(fileParamName, file.getName(),
					file, new MimetypesFileTypeMap().getContentType(file),
					"UTF-8");

			filePart.setTransferEncoding("binary");
			parts[(parts.length - 1)] = filePart;

			postMethod.setRequestEntity(new MultipartRequestEntity(parts,
					postMethod.getParams()));

			return httpRequest(postMethod);
		} catch (Exception ex) {
			throw new QQConnectException(ex.getMessage(), ex, -1);
		}
	}

	public Response httpRequest(HttpMethod method) throws QQConnectException {
		return httpRequest(method, Boolean.valueOf(false));
	}

	public Response httpRequest(HttpMethod method, Boolean WithTokenHeader)
			throws QQConnectException {
		int responseCode = -1;
		try {
			InetAddress ipaddr = InetAddress.getLocalHost();
			List<Header> headers = new ArrayList();
			if (WithTokenHeader.booleanValue()) {
				headers.add(new Header("Authorization", "OAuth2 "));
				headers.add(new Header("API-RemoteIP", ipaddr.getHostAddress()));
				this.client.getHostConfiguration().getParams()
						.setParameter("http.default-headers", headers);

				for (Header hd : headers) {
					log(hd.getName() + ": " + hd.getValue());
				}
			}

			method.getParams().setParameter("http.method.retry-handler",
					new DefaultHttpMethodRetryHandler(3, false));

			this.client.executeMethod(method);
			Header[] resHeader = method.getResponseHeaders();
			responseCode = method.getStatusCode();
			log("Response:");
			log("https StatusCode:" + String.valueOf(responseCode));

			for (Header header : resHeader) {
				log(header.getName() + ":" + header.getValue());
			}
			Response response = new Response();

			response.setResponseAsString(new String(method.getResponseBody(),
					"utf-8"));
			log(response.toString() + "\n");

			if (responseCode != 200) {
				try {
					throw new QQConnectException(getCause(responseCode),
							response.asJSONObject(), method.getStatusCode());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return response;
		} catch (IOException ioe) {
			throw new QQConnectException(ioe.getMessage(), ioe, responseCode);
		} finally {
			method.releaseConnection();
		}
	}

	private static String encodeParameters(PostParameter[] postParams) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < postParams.length; j++) {
			if (j != 0)
				buf.append("&");
			try {
				buf.append(URLEncoder.encode(postParams[j].getName(), "UTF-8"))
						.append("=")
						.append(URLEncoder.encode(postParams[j].getValue(),
								"UTF-8"));
			} catch (UnsupportedEncodingException neverHappen) {
			}

		}

		return buf.toString();
	}

	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
		case 304:
			break;
		case 400:
			cause = "请求无效";
			break;
		case 401:
			cause = "未获得授权";
			break;
		case 403:
			cause = "无权限访问当前资源";
			break;
		case 404:
			cause = "资源不存在";
			break;
		case 406:
			cause = "请检查请求参数";
			break;
		case 500:
			cause = "服务器出错了";
			break;
		case 502:
			cause = "服务器出错了";
			break;
		case 503:
			cause = "当前访问量过大，请稍后重试";
			break;
		default:
			cause = "";
		}
		return statusCode + ":" + cause;
	}

	public String getToken() throws QQConnectException {
		if ((this.token == null) || (this.token.equals(""))) {
			throw new QQConnectException("please invoke the setToken first !");
		}
		return this.token;
	}

	private static class ByteArrayPart extends PartBase {
		private byte[] mData;
		private String mName;

		public ByteArrayPart(byte[] data, String name, String type)
				throws IOException {
			super(name, type, "UTF-8", "binary");
			this.mName = name;
			this.mData = data;
		}

		protected void sendData(OutputStream out) throws IOException {
			out.write(this.mData);
		}

		protected long lengthOfData() throws IOException {
			return this.mData.length;
		}

		protected void sendDispositionHeader(OutputStream out)
				throws IOException {
			super.sendDispositionHeader(out);
			StringBuilder buf = new StringBuilder();
			buf.append("; filename=\"").append(this.mName).append("\"");
			out.write(buf.toString().getBytes());
		}
	}
}
