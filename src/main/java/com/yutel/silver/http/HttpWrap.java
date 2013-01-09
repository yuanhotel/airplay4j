package com.yutel.silver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HttpWrap {
	private static Logger logger = Logger.getLogger(HttpWrap.class.getName());
	private String context;
	private String mothod;
	private String protocol;
	Entry entry = new Entry();

	public boolean extract(Socket socket) {
		try {
			InputStream is = socket.getInputStream(); // 获得输入流
			OutputStream os = socket.getOutputStream(); // 获得输入流
			entry.requestBody = is;
			entry.responseBody = os;
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String tempbf = br.readLine();
			if (tempbf != null) {
				fistline(tempbf);
				logger.info("fistline=" + tempbf);
				while ((tempbf = br.readLine()) != null) {
					if (!"".equals(tempbf.trim())) {
						header(tempbf);
					} else {
						break;
					}
				}
				logger.info("extract is finished!");
				return true;
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void sendResponseHeaders(int responseCode, int length) {
		try {
			entry.responseBody.write(HttpProtocol.getResponse(responseCode)
					.getBytes());
			for (Map.Entry<String, String> item : entry.responseHeads
					.entrySet()) {
				entry.responseBody.write((item.getKey() + ": "
						+ item.getValue() + "\r\n").getBytes());
			}
			entry.responseBody.write(("Content-Length: " + length + "\r\n\r\n")
					.getBytes());
			if (length == 0) {
				entry.responseBody.write(("\r\n").getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getRequestHeads() {
		return entry.requestHeads;
	}

	public Map<String, String> getResponseHeads() {
		return entry.responseHeads;
	}

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

	public InputStream getRequestBody() {
		return entry.requestBody;
	}

	public OutputStream getResponseBody() {
		return entry.responseBody;
	}

	private void header(String line) {
		if (line != null && !"".equals(line.trim())) {
			String[] parts = line.split(":");
			if (parts.length == 2) {
				entry.getRequestHeads().put(parts[0].trim(), parts[1].trim());
			}
		}
	}

	private void fistline(String line) {
		String[] parts = line.split(" ");
		if (parts.length >= 2) {
			setMothod(parts[0]);
			setContext(parts[1]);
		}
		if (parts.length >= 3) {
			setProtocol(parts[2]);
		}
	}

	public class Entry {
		private Map<String, String> requestHeads = new HashMap<String, String>();
		private Map<String, String> responseHeads = new HashMap<String, String>();
		private InputStream requestBody;
		private OutputStream responseBody;

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
