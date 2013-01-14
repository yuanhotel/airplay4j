package com.yutel.sample.handler;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.yutel.silver.AikaProxy;
import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.BaseHttpHandler;

public class PlayHandler extends BaseHttpHandler {

	public PlayHandler(AikaProxy proxy) {
		super(proxy);
	}

	public void handle(HttpWrap hw) throws AirplayException {
		System.out.println("head:"+hw.getRequestHeads());
		try {
			NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(hw
					.getRequestBodys());
			String url = rootDict.objectForKey("Content-Location").toString();
			String rate = rootDict.objectForKey("rate").toString();
			String pos = rootDict.objectForKey("Start-Position").toString();
			System.out.println("url=" + url + ",rate=" + rate);
			if (mProxy != null) {
				mProxy.video(pos, rate, url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response(hw);
	}

	private void response(HttpWrap hw) throws AirplayException {
		System.out.println("response");
		hw.sendResponseHeaders(200, 0);
	}
}
