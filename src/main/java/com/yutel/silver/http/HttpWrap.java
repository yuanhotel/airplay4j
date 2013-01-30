package com.yutel.silver.http;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpWrap {
	private int responseCode;
	private String context;
	private String mothod;
	private String protocol;
	private Map<String, String> requestParameters = new HashMap<String, String>();
	private Map<String, String> requestHeads = new HashMap<String, String>();
	private Map<String, String> responseHeads = new HashMap<String, String>();
	private byte[] responseBody;
	private boolean response;
	private boolean reverse;
	private String bodys;
	private int reverseEvent;

	public String buildResponse() {
		int length = 0;
		if (bodys != null) {
			length=bodys.length();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(HttpProtocol.getResponse(responseCode));
		for (Map.Entry<String, String> item : responseHeads.entrySet()) {
			sb.append(item.getKey()).append(": ").append(item.getValue());
			sb.append(HttpAnaly.CR).append(HttpAnaly.LF);
		}
		sb.append("Date: ").append(HttpProtocol.getGMTTime(new Date()));
		sb.append(HttpAnaly.CR).append(HttpAnaly.LF);
		sb.append("Content-Length: ").append(length).append(HttpAnaly.CR);
		sb.append(HttpAnaly.LF).append(HttpAnaly.CR).append(HttpAnaly.LF);
		if (length > 0) {
			sb.append(bodys);
		}
		return sb.toString();
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
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

	public byte[] getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(byte[] responseBody) {
		this.responseBody = responseBody;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	public String getBodys() {
		return bodys;
	}

	public void setBodys(String bodys) {
		this.bodys = bodys;
	}

	public int getReverseEvent() {
		return reverseEvent;
	}

	public void setReverseEvent(int reverseEvent) {
		this.reverseEvent = reverseEvent;
	}
}