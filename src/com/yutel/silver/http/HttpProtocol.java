package com.yutel.silver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpProtocol {
	private static Map<Integer, String> resposes = new HashMap<Integer, String>();
	static {
		resposes.put(200, "HTTP/1.1 200 OK\r\n");
		resposes.put(101, "HTTP/1.1 101 Switching Protocols\r\n");
	}

	public static void main(String[] args) {

	}

	public static String getResponse(int code) {
		return resposes.get(code);
	}

}
