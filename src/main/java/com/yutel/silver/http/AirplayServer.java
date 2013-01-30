package com.yutel.silver.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.yutel.silver.AikaProxy;
import com.yutel.silver.vo.Device;

public class AirplayServer extends Thread {
	private static Logger logger = Logger.getLogger(AirplayServer.class
			.getName());
	public int mDuration;
	public int mPosition;
	public int mState;

	private Device mDevice;
	private AikaProxy mProxy;
	private ServerSocket mServerSocket;
	private Map<String, Socket> reverseResponse;
	private int mPort;
	private boolean stop = false;

	public static void main(String[] args) {
		new AirplayServer(8888).start();
	}

	public AirplayServer(int port) {
		mPort = port;
	}

	public Device getDevice() {
		return mDevice;
	}

	public void setDevice(Device device) {
		mDevice = device;
	}

	public AikaProxy getProxy() {
		return mProxy;
	}

	public void setProxy(AikaProxy mProxy) {
		this.mProxy = mProxy;
	}

	public void addReverseResponse(String sessionid, Socket socket) {
		reverseResponse.put(sessionid, socket);
	}

	public void sendReverseResponse(String sessionid, String body) {
		try {
			System.out.println("EVENT:"+body);
			Socket s = reverseResponse.get(sessionid);
			if (s != null && s.isConnected()) {
				OutputStream os = s.getOutputStream();
				os.flush();
				os.write(body.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void forceStop() {
		stop = true;
	}

	@Override
	public void run() {
		try {
			reverseResponse = new HashMap<String, Socket>();
			mServerSocket = new ServerSocket(mPort);
			logger.log(Level.INFO, "Http server started listening:" + mPort);
			while (!stop) {
				final Socket socket = mServerSocket.accept();
				String ip = socket.getInetAddress().getHostAddress();
				logger.log(Level.INFO, "client:" + ip + "/" + socket.getPort());
				new HttpClient(socket, this).start();
			}
			if (stop) {
				mServerSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void finalize() {
		try {
			if (mServerSocket != null) {
				mServerSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mServerSocket = null;
	}
}
