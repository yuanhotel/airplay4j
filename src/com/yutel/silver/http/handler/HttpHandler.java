package com.yutel.silver.http.handler;

import java.io.IOException;

import com.yutel.silver.http.HttpWrap;


public interface HttpHandler {
	public void handle(HttpWrap hw) throws IOException ;
}
