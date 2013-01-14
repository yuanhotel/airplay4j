package com.yutel.sample.handler;

import com.yutel.silver.AikaProxy;
import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.BaseHttpHandler;

public class StopHandler extends BaseHttpHandler {

	public StopHandler(AikaProxy proxy) {
		super(proxy);
	}

	public void handle(HttpWrap hw) throws AirplayException {
		if (mProxy != null) {
			mProxy.videoStop();
		}
		hw.sendResponseHeaders(200, 0);
	}
}
