package com.yutel.sample.handler;

import com.yutel.silver.AikaProxy;
import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.BaseHttpHandler;

public class ReverseHandler extends BaseHttpHandler {

	public ReverseHandler(AikaProxy proxy) {
		super(proxy);
	}

	public void handle(HttpWrap hw) throws AirplayException {
		System.out.println("head:"+hw.getRequestHeads());
		response(hw);
	}

	private void response(HttpWrap hw) throws AirplayException {
		System.out.println("response");
		hw.sendResponseHeaders(404, 0);
	}
}
