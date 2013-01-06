package com.yutel.silver.http;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.yutel.silver.Aika;
import com.yutel.silver.http.handler.HttpHandler;

public class AirplayServer extends Thread {
	private static Logger logger = Logger.getLogger(Aika.class.getName());
	private boolean stop = false;
	private int mPort;
	private ServerSocket ss;
	private Map<String, HttpHandler> handlers;

	public static void main(String[] args) {
		new AirplayServer(8888).start();
	}

	public AirplayServer(int port) {
		mPort = port;
		handlers = new HashMap<String, HttpHandler>();
	}

	public void createContext(String key, HttpHandler handler) {
		handlers.put(key, handler);
	}

	public HttpHandler getHandler(String key) {
		return handlers.get(key);
	}

	public void forceStop() {
		stop = true;
	}

	@Override
	public void run() {
		try {
			ss = new ServerSocket(mPort);
			logger.log(Level.INFO, "Http server started!");
			while (!stop) {
				final Socket socket = ss.accept();
				String ip = socket.getInetAddress().getHostAddress();
				logger.log(Level.INFO, "client:" + ip + "/" + socket.getPort());
				new HttpClient(socket, this).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void finalize() {
		try {
			if (ss != null) {
				ss.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ss = null;
	}
}
