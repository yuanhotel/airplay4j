package com.yutel.sample.handler;

import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.HttpHandler;

public class ScrubHandler implements HttpHandler {

	public void handle(HttpWrap hw) throws AirplayException {
		hw.sendResponseHeaders(200, 0);
	}

}
