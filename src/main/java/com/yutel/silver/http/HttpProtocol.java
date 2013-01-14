package com.yutel.silver.http;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class HttpProtocol {
	public final static String ContentType = "Content-Type";
	public final static String ContentLength = "Content-Length";
	public final static String CRLF = "\r\n";
	private static Map<Integer, String> resposes = new HashMap<Integer, String>();
	private static java.text.SimpleDateFormat gmtFrmt;

	static {
		resposes.put(200, "HTTP/1.1 200 OK"+CRLF);
		resposes.put(101, "HTTP/1.1 101 Switching Protocols"+CRLF);
		resposes.put(501, "HTTP/1.1 501 Not Implemented"+CRLF);
		resposes.put(401, "HTTP/1.1 401 Unauthorized"+CRLF);
		resposes.put(404, "HTTP/1.1 404 Not Found"+CRLF);
		resposes.put(405, "HTTP/1.1 405 Method Not Allowed"+CRLF);
		gmtFrmt = new java.text.SimpleDateFormat(
				"E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		gmtFrmt.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public static void main(String[] args) {

	}

	public static String getResponse(int code) {
		return resposes.get(code);
	}

	public static String getGMTTime(Date d) {
		return gmtFrmt.format(d);
	}

}
