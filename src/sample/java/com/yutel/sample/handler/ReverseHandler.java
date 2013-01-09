package com.yutel.sample.handler;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.HttpHandler;

public class ReverseHandler implements HttpHandler {

	public void handle(HttpWrap hw) throws IOException {
		Map<String, String> headers = hw.getRequestHeads();
		for (Map.Entry<String, String> item : headers.entrySet()) {
			System.out.println("name=" + item.getKey() + ",value=" + item.getValue());
		}
		response(hw);
	}

	private void response(HttpWrap hw) throws IOException {
		System.out.println("response");
		hw.getResponseHeads().put("Date", new Date().toString());
		hw.sendResponseHeaders(404, 0);
	}
}
