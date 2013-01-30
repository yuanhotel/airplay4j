package com.yutel.silver.http;

import java.net.Socket;
import java.util.logging.Logger;

import com.yutel.silver.util.AirplayUtil;

public class HttpClient extends Thread {
	private static Logger logger = Logger.getLogger(HttpClient.class.getName());
	private Socket mSocket;
	private boolean mConnect = true;
	private String mSession;
	private AirplayServer airplayServer;

	public HttpClient(Socket socket, AirplayServer airplayServer) {
		this.airplayServer = airplayServer;
		mSocket = socket;
	}

	@Override
	public void run() {
		while (mConnect) {
			try {
				// 读取HTTP请求信息
				HttpAnaly ha = new HttpAnaly(mSocket.getInputStream(),
						mSocket.getOutputStream());
				HttpWrap hw = ha.parse(mSocket.getPort());
				if (hw.getRequestHeads().size() > 0) {
					if (mSession == null || "".equals(mSession)) {
						mSession = hw.getRequestHeads().get(
								HttpProtocol.AIRPLAY_SESSION);
						logger.info("init:port=" + mSocket.getPort()
								+ ",mSession=" + mSession);
					}
					DefaultHandler dhh = new DefaultHandler(airplayServer, hw);
					dhh.process();
					ha.sendResponse(hw.buildResponse());
					if (hw.getResponseCode() == 101) {
						airplayServer.addReverseResponse(hw.getRequestHeads()
								.get(HttpProtocol.AIRPLAY_SESSION), mSocket);
						// mConnect=false;
					}
					if (hw.isReverse()) {
						String event = HttpProtocol.getEvent(hw
								.getReverseEvent());
						String reverse = AirplayUtil.getEventInfo(event);
						airplayServer.sendReverseResponse(mSession,reverse);
					}

				}
				sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void finalize() {
		try {
			if (mSocket != null) {
				mSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mSocket = null;
	}
}
