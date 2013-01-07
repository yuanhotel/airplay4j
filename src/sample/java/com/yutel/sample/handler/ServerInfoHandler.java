package com.yutel.sample.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.HttpHandler;

public class ServerInfoHandler implements HttpHandler {

	public void handle(HttpWrap hw) throws IOException {
		Map<String, String> headers = hw.getRequestHeads();
		for (Map.Entry<String, String> item : headers.entrySet()) {
			System.out.println("name=" + item.getKey() + ",value=" + item.getValue());
		}
		response(hw);
	}

	private void response(HttpWrap hw) throws IOException {
		System.out.println("response");
		StringBuffer res = new StringBuffer();
		res.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		res.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\"");
		res.append(" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
		res.append("<plist version=\"1.0\">");
		res.append(" <dict>");
		res.append("  <key>deviceid</key>");
		res.append("  <string>58:55:CA:1A:E2:88</string>");
		res.append("  <key>features</key>");
		res.append("  <integer>119</integer>");
		res.append("  <key>model</key>");
		res.append("  <string>AppleTV2,1</string>");
		res.append("  <key>protovers</key>");
		res.append("  <string>1.0</string>");
		res.append("  <key>srcvers</key>");
		res.append("  <string>120.2</string>");
		res.append(" </dict>");
		res.append("</plist>");
		res.append("\r\n");
		hw.sendResponseHeaders(200, res.toString().getBytes().length);
		OutputStream os = hw.getResponseBody();
		os.write(res.toString().getBytes());
		os.flush();
		os.close();
	}
}
