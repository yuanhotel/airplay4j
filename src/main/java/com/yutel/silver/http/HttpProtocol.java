package com.yutel.silver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpProtocol {
	private static Map<Integer, String> resposes = new HashMap<Integer, String>();
	static {
		resposes.put(200, "HTTP/1.1 200 OK\r\n");
		resposes.put(101, "HTTP/1.1 101 Switching Protocols\r\n");
		resposes.put(501, "HTTP/1.1 501 Not Implemented\r\n");
		resposes.put(401, "HTTP/1.1 401 Unauthorized\r\n");
		resposes.put(404, "HTTP/1.1 404 Not Found\r\n");
		resposes.put(405, "HTTP/1.1 405 Method Not Allowed\r\n");
	}

	public static void main(String[] args) {

	}

	public static String getResponse(int code) {
		return resposes.get(code);
	}

}
