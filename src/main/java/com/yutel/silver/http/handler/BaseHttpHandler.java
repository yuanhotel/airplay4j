package com.yutel.silver.http.handler;

import com.yutel.silver.AikaProxy;
import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.HttpWrap;

public abstract class BaseHttpHandler implements HttpHandler {
	protected AikaProxy mProxy;

	public BaseHttpHandler(AikaProxy proxy) {
		mProxy = proxy;
	}

	public abstract void handle(HttpWrap hw) throws AirplayException;
}
