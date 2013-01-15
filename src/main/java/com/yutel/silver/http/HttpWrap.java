package com.yutel.silver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import com.yutel.silver.http.HttpAnaly.HttpEntry;

public class HttpWrap {
	private HttpEntry entry;
	private HttpAnaly ha;

	public boolean extract(Socket socket) {
		ha = new HttpAnaly(socket);
		entry = ha.analy();
		return true;
	}

	public String getContext() {
		return entry == null ? null : entry.getContext();
	}

	public Map<String, String> getRequestParameters() {
		return entry == null ? null : entry.getRequestParameters();
	}

	public Map<String, String> getRequestHeads() {
		return entry == null ? null : entry.getRequestHeads();
	}

	public Map<String, String> getResponseHeads() {
		return entry == null ? null : entry.getResponseHeads();
	}

	public InputStream getRequestBody() {
		return entry == null ? null : entry.getRequestBody();
	}

	public OutputStream getResponseBody() {
		return entry == null ? null : entry.getResponseBody();
	}

	public void sendResponseHeaders(int responseCode, int length) {
		try {
			String res = ha.buildResponseHeaders(responseCode, length);
			System.out.println("res="+res);
			entry.getResponseBody().write(res.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
