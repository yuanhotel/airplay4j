package com.yutel.sample.handler;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;
import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.HttpHandler;

public class PlayHandler implements HttpHandler {

	public void handle(HttpWrap hw) throws IOException {
		Map<String, String> headers = hw.getRequestHeads();
		for (Map.Entry<String, String> item : headers.entrySet()) {
			System.out.println("name=" + item.getKey() + ",value="
					+ item.getValue());
		}
		try {
			NSObject plist = PropertyListParser.parse(hw.getRequestBodys());
			System.out.println(plist.toXMLPropertyList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		response(hw);
	}

	private void response(HttpWrap hw) throws IOException {
		System.out.println("response");
		hw.getResponseHeads().put("date", new Date().toString());
		hw.sendResponseHeaders(200, 0);
	}
}
