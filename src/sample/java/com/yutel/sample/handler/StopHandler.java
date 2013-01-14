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
		System.out.println("head:" + hw.getRequestHeads());
		if (mProxy != null) {
			mProxy.videoStop();
		}
		response(hw);
	}

	private void response(HttpWrap hw) throws AirplayException {
		System.out.println("response");
		hw.sendResponseHeaders(200, 0);
	}
}
