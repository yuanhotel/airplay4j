package com.yutel.sample.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.yutel.silver.http.HttpWrap;
import com.yutel.silver.http.handler.HttpHandler;
import com.yutel.silver.util.AirplayUtil;
import com.yutel.silver.vo.Device;

public class ServerInfoHandler implements HttpHandler {
	public Device device;

	public ServerInfoHandler(Device device) {
		this.device = device;
	}

	public void handle(HttpWrap hw) throws IOException {
		Map<String, String> headers = hw.getRequestHeads();
		for (Map.Entry<String, String> item : headers.entrySet()) {
			System.out.println("name=" + item.getKey() + ",value="
					+ item.getValue());
		}
		response(hw);
	}

	private void response(HttpWrap hw) throws IOException {
		System.out.println("response");
		hw.getResponseHeads().put("Content-Type", "text/x-apple-plist+xml");
		String res = AirplayUtil.getServerInfo(device);
		hw.sendResponseHeaders(200, res.getBytes().length);
		OutputStream os = hw.getResponseBody();
		os.write(res.getBytes());
		os.flush();
	}
}
