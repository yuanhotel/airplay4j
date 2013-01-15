package com.yutel.sample.handler;

import com.yutel.silver.AikaProxy;
import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.BaseHttpHandler;

public class RateHandler extends BaseHttpHandler {
	public RateHandler(AikaProxy proxy) {
		super(proxy);
	}
	public void handle(HttpWrap hw) throws AirplayException {
		hw.sendResponseHeaders(200, 0);
	}

}
