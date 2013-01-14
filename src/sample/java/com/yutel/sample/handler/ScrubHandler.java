package com.yutel.sample.handler;

import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.HttpHandler;

public class ScrubHandler implements HttpHandler {

	public void handle(HttpWrap hw) throws AirplayException {
		System.out.println("head:"+hw.getRequestHeads());
		response(hw);
	}

	private void response(HttpWrap hw) throws AirplayException {
		System.out.println("response");
		hw.sendResponseHeaders(200, 0);
	}
}
