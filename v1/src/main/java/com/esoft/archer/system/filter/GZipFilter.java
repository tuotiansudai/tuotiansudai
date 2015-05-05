package com.esoft.archer.system.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class GZipFilter implements Filter {
	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		// 排除swf请求的返回信息，因为flash控件不支持gzip压缩，所以如果是返回给flash控件的信息，必须关闭gzip，例如ueditor中的上传。
		if ("Shockwave Flash".equals(httpRequest.getHeader("User-Agent"))) {
			chain.doFilter(request, response);
		} else {
			byte[] data = null;

			CachedResponseWrapper wrapper = new CachedResponseWrapper(
					httpResponse);

			// 写入wrapper:
			chain.doFilter(request, wrapper);

			// 对响应进行处理，这里是进行GZip压缩:
			if (data == null) {
				data = GZipUtil.gzip(wrapper.getResponseData());
			}
			// httpResponse.setStatus(304);
			httpResponse.setHeader("Content-Encoding", "gzip");
			httpResponse.setContentLength(data.length);

			ServletOutputStream output = response.getOutputStream();

			// response.reset();
			output.write(data);
			output.flush();
		}
	}

	public void destroy() {

	}

}

class GZipUtil {
	/** * Do a gzip operation. */
	public static byte[] gzip(byte[] data) {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(10240);
		GZIPOutputStream output = null;
		try {
			output = new GZIPOutputStream(byteOutput);
			output.write(data);
		} catch (IOException e) {
			throw new RuntimeException("G-Zip failed.", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
		}
		return byteOutput.toByteArray();
	}
}

class CachedResponseWrapper extends HttpServletResponseWrapper {

	/**
	 * Indicate that getOutputStream() or getWriter() is not called yet.
	 */
	public static final int OUTPUT_NONE = 0;

	/**
	 * Indicate that getWriter() is already called.
	 */
	public static final int OUTPUT_WRITER = 1;

	/**
	 * Indicate that getOutputStream() is already called.
	 */
	public static final int OUTPUT_STREAM = 2;

	private int outputType = OUTPUT_NONE;

	private int status = SC_OK;

	private ServletOutputStream output = null;

	private PrintWriter writer = null;

	private ByteArrayOutputStream buffer = null;

	public CachedResponseWrapper(HttpServletResponse resp) throws IOException {
		super(resp);
		buffer = new ByteArrayOutputStream();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		super.setStatus(status);
		this.status = status;
	}

	public void setStatus(int status, String string) {
		super.setStatus(status, string);
		this.status = status;
	}

	public void sendError(int status, String string) throws IOException {
		super.sendError(status, string);
		this.status = status;
	}

	public void sendError(int status) throws IOException {
		super.sendError(status);
		this.status = status;
	}

	public void sendRedirect(String location) throws IOException {
		super.sendRedirect(location);
		this.status = SC_MOVED_TEMPORARILY;
	}

	public PrintWriter getWriter() throws IOException {
		if (outputType == OUTPUT_STREAM)
			throw new IllegalStateException();
		else if (outputType == OUTPUT_WRITER)
			return writer;
		else {
			outputType = OUTPUT_WRITER;

			writer = new PrintWriter(new OutputStreamWriter(buffer,
					getCharacterEncoding()));
			return writer;
		}
	}

	public ServletOutputStream getOutputStream() throws IOException {
		if (outputType == OUTPUT_WRITER)
			throw new IllegalStateException();
		else if (outputType == OUTPUT_STREAM)
			return output;
		else {
			outputType = OUTPUT_STREAM;
			output = new WrappedOutputStream(buffer);
			return output;
		}
	}

	public void flushBuffer() throws IOException {
		if (outputType == OUTPUT_WRITER)
			writer.flush();
		if (outputType == OUTPUT_STREAM)
			output.flush();
	}

	public void reset() {
		outputType = OUTPUT_NONE;
		buffer.reset();
	}

	/**
	 * Call this method to get cached response data.
	 * 
	 * @return byte array buffer.
	 * @throws IOException
	 */
	public byte[] getResponseData() throws IOException {
		flushBuffer();
		return buffer.toByteArray();
	}

	/**
	 * This class is used to wrap a ServletOutputStream and store output stream
	 * in byte[] buffer.
	 */
	class WrappedOutputStream extends ServletOutputStream {

		private ByteArrayOutputStream buffer;

		public WrappedOutputStream(ByteArrayOutputStream buffer) {
			this.buffer = buffer;
		}

		public void write(int b) throws IOException {
			buffer.write(b);
		}

		public byte[] toByteArray() {
			return buffer.toByteArray();
		}
	}

}