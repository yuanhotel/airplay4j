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
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class HttpAnaly {
	private static Logger logger = Logger.getLogger(HttpAnaly.class.getName());
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
			logger.info("context=" + entry.context);
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
//		if (length == 0) {
//			sb.append(CR).append(LF);
//		}
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
				entry.requestHeads.put(parts[0].trim(), parts[1].trim());
			}
		}
	}

	private void fistline(String line) {
		StringTokenizer st = new StringTokenizer(line, " ");
		if (st.hasMoreTokens()) {
			entry.mothod = st.nextToken();
		}
		if (st.hasMoreTokens()) {
			String uri = st.nextToken();
			int qmi = uri.indexOf('?');
			if (qmi >= 0) {
				decodeParms(uri.substring(qmi + 1));
				entry.context = decodePercent(uri.substring(0, qmi));
			} else {
				entry.context = decodePercent(uri);
			}
		}
		if (st.hasMoreTokens()) {
			entry.protocol = st.nextToken();
		}
	}

	private void decodeParms(String parms) {
		StringTokenizer st = new StringTokenizer(parms, "&");
		while (st.hasMoreTokens()) {
			String e = st.nextToken();
			int sep = e.indexOf('=');
			if (sep >= 0) {
				entry.requestParameters.put(decodePercent(e.substring(0, sep))
						.trim(), decodePercent(e.substring(sep + 1)));
			}
		}
	}

	private String decodePercent(String str) {
		try {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				switch (c) {
				case '+':
					sb.append(' ');
					break;
				case '%':
					sb.append((char) Integer.parseInt(
							str.substring(i + 1, i + 3), 16));
					i += 2;
					break;
				default:
					sb.append(c);
					break;
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public class HttpEntry {
		private String context;
		private String mothod;
		private String protocol;
		private Map<String, String> requestParameters = new HashMap<String, String>();
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

		public Map<String, String> getRequestParameters() {
			return requestParameters;
		}

		public void setRequestParameters(Map<String, String> requestParameters) {
			this.requestParameters = requestParameters;
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
