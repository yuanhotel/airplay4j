package com.yutel.silver.http;

import java.io.IOException;
import java.net.Socket;

import com.yutel.silver.http.handler.HttpHandler;

public class HttpClient extends Thread {
	private Socket mSocket;
	private AirplayServer mAirplayHttp;

	public HttpClient(Socket socket, AirplayServer airplayHttp) {
		mSocket = socket;
		mAirplayHttp = airplayHttp;
	}

	@Override
	public void run() {
		while (true) {
			try {
				// 读取HTTP请求信息
				HttpWrap hw = new HttpWrap();
				if (hw.extract(mSocket)) {
					HttpHandler handler = mAirplayHttp.getHandler(hw
							.getContext());
					if (handler != null) {
						handler.handle(hw);
					} else {
						System.out.println("context \"" + hw.getContext()
								+ "\" is not find!");
					}
				}
				sleep(1000);
			} catch (IOException e) {
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
