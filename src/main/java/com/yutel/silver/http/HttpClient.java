package com.yutel.silver.http;

import java.net.Socket;
import java.util.logging.Logger;

import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.handler.HttpHandler;

public class HttpClient extends Thread {
	private static Logger logger = Logger.getLogger(HttpClient.class.getName());
	private boolean connect = true;
	private Socket mSocket;
	private AirplayServer mAirplayHttp;

	public HttpClient(Socket socket, AirplayServer airplayHttp) {
		mSocket = socket;
		mAirplayHttp = airplayHttp;
	}

	@Override
	public void run() {
		while (connect) {
			try {
				// 读取HTTP请求信息
				HttpWrap hw = new HttpWrap();
				if (hw.extract(mSocket)) {
					if (hw.getContext() != null) {
						HttpHandler handler = mAirplayHttp.getHandler(hw
								.getContext());
						if (handler != null) {
							handler.handle(hw);
						} else {
							logger.info("context \"" + hw.getContext()
									+ "\" is not find!");
						}
					} else {
						connect = false;
					}
				}
				sleep(1000);
			} catch (AirplayException e) {
				e.printStackTrace();
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
