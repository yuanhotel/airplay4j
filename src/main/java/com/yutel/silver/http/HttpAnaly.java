package com.yutel.silver.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HttpAnaly {
	private static Logger logger = Logger.getLogger(HttpWrap.class.getName());
	private final static char CR;
	private final static char LF;
	private final static byte[] DCRLF;
	private final static StringBuffer sb;

	private HttpEntry entry;
	private HeadStream bsos;
	private Socket mSocket;
	static {
		CR = '\r';
		LF = '\n';
		DCRLF = new byte[] { (byte) CR, (byte) LF, (byte) CR, (byte) LF };
		sb = new StringBuffer();
	}

	public HttpAnaly(Socket socket) {
		bsos = new HeadStream();
		mSocket = socket;
		try {
			entry = new HttpEntry();
			entry.setRequestBody(mSocket.getInputStream());
			entry.setResponseBody(mSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HttpEntry getEntry() {
		return entry;
	}

	public void setEntry(HttpEntry entry) {
		this.entry = entry;
	}

	public synchronized HttpEntry analy() {
		read2CRLF();
		if (headers()) {
			return entry;
		} else {
			return null;
		}
	}

	public String buildResponseHeaders(int responseCode, int length) {
		sb.setLength(0);
		sb.append(HttpProtocol.getResponse(responseCode));
		for (Map.Entry<String, String> item : entry.getResponseHeads()
				.entrySet()) {
			sb.append(item.getKey()).append(": ").append(item.getValue())
					.append(CR).append(LF);
		}
		sb.append("Date: ").append(HttpProtocol.getGMTTime(new Date()))
				.append(CR).append(LF);
		sb.append("Content-Length: ").append(length).append(CR).append(LF)
				.append(CR).append(LF);
		if (length == 0) {
			sb.append(CR).append(LF).append(CR).append(LF);
		}
		return sb.toString();
	}

	private synchronized void read2CRLF() {
		try {
			byte[] temp = new byte[1];
			while ((entry.getRequestBody().read(temp)) != -1) {
				bsos.write(temp);
				if (bsos.equalTail(DCRLF)) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized boolean headers() {
		try {
			byte[] buf = bsos.toByteArray();
			ByteArrayInputStream bin = new ByteArrayInputStream(buf);
			BufferedReader in = new BufferedReader(new InputStreamReader(bin));
			String tempbf = in.readLine();
			if (tempbf == null || tempbf.length() < 1) {
				return false;
			}
			logger.info("fistline=" + tempbf);
			fistline(tempbf);
			tempbf = in.readLine();
			while (tempbf != null && tempbf.length() > 0) {
				header(tempbf);
				tempbf = in.readLine();
			}
			logger.info("header parse compled");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	private void header(String line) {
		if (line != null && !"".equals(line.trim())) {
			String[] parts = line.split(":");
			if (parts.length == 2) {
				getEntry().requestHeads.put(parts[0].trim(), parts[1].trim());
			}
		}
	}

	private void fistline(String line) {
		String[] parts = line.split(" ");
		if (parts.length >= 2) {
			getEntry().setMothod(parts[0]);
			getEntry().setContext(parts[1]);
		}
		if (parts.length >= 3) {
			getEntry().setProtocol(parts[2]);
		}
	}

	public class HttpEntry {
		private String context;
		private String mothod;
		private String protocol;
		private Map<String, String> requestparameters = new HashMap<String, String>();
		private Map<String, String> requestHeads = new HashMap<String, String>();
		private Map<String, String> responseHeads = new HashMap<String, String>();
		private volatile InputStream requestBody;
		private volatile OutputStream responseBody;

		public String getContext() {
			return context;
		}

		public void setContext(String context) {
			this.context = context;
		}

		public String getMothod() {
			return mothod;
		}

		public void setMothod(String mothod) {
			this.mothod = mothod;
		}

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

		public Map<String, String> getRequestparameters() {
			return requestparameters;
		}

		public void setRequestparameters(Map<String, String> requestparameters) {
			this.requestparameters = requestparameters;
		}

		public Map<String, String> getRequestHeads() {
			return requestHeads;
		}

		public void setRequestHeads(Map<String, String> requestHeads) {
			this.requestHeads = requestHeads;
		}

		public Map<String, String> getResponseHeads() {
			return responseHeads;
		}

		public void setResponseHeads(Map<String, String> responseHeads) {
			this.responseHeads = responseHeads;
		}

		public InputStream getRequestBody() {
			return requestBody;
		}

		public void setRequestBody(InputStream requestBody) {
			this.requestBody = requestBody;
		}

		public OutputStream getResponseBody() {
			return responseBody;
		}

		public void setResponseBody(OutputStream responseBody) {
			this.responseBody = responseBody;
		}
	}

}
