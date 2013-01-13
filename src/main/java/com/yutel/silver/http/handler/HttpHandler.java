package com.yutel.silver.http.handler;

import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.HttpWrap;

public interface HttpHandler {
	public void handle(HttpWrap hw) throws AirplayException;
}
