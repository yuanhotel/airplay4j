package com.yutel.sample.handler;

import java.io.IOException;
import java.io.OutputStream;

import com.yutel.silver.AikaProxy;
import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.BaseHttpHandler;
import com.yutel.silver.util.AirplayUtil;
import com.yutel.silver.vo.Device;

public class ServerInfoHandler extends BaseHttpHandler {
	public Device device;

	public ServerInfoHandler(AikaProxy proxy, Device device) {
		super(proxy);
		this.device = device;
	}

	public void handle(HttpWrap hw) throws AirplayException {
		System.out.println("head:"+hw.getRequestHeads());
		response(hw);
	}

	private void response(HttpWrap hw) throws AirplayException {
		try {
			System.out.println("response");
			hw.getResponseHeads().put("Content-Type", "text/x-apple-plist+xml");
			String res = AirplayUtil.getServerInfo(device);
			hw.sendResponseHeaders(200, res.getBytes().length);
			OutputStream os = hw.getResponseBody();
			os.write(res.getBytes());
			os.flush();
		} catch (IOException e) {
			throw new AirplayException(e);
		}
	}
}
