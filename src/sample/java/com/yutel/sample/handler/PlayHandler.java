package com.yutel.sample.handler;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;
import com.yutel.silver.AikaProxy;
import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.BaseHttpHandler;
import com.yutel.silver.util.StringUtil;

public class PlayHandler extends BaseHttpHandler {

	public PlayHandler(AikaProxy proxy) {
		super(proxy);
	}

	public void handle(HttpWrap hw) throws AirplayException {
		try {
			String body = hw.getRequestHeads().get("Content-Length");
			int len = StringUtil.toInteger(body);
			if (len > 0) {
				byte[] data = new byte[len];
				hw.getRequestBody().read(data);
				NSDictionary rootDict = (NSDictionary) PropertyListParser
						.parse(data);
				String url = rootDict.objectForKey("Content-Location")
						.toString();
				String rate = rootDict.objectForKey("rate").toString();
				String pos = "0f";
				NSObject p = rootDict.objectForKey("Start-Position");
				if (p != null) {
					pos = p.toString();
				}
				if (mProxy != null) {
					mProxy.video(url, rate, pos);
				}
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
