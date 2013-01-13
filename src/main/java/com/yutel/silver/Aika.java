package com.yutel.silver;

import java.net.InetAddress;

import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.http.handler.HttpHandler;
import com.yutel.silver.vo.Device;

public abstract class Aika {
	public static final String VERSION = "1.0";

	public static Aika create(InetAddress inetAddress, int port) {
		return new AikaImpl(inetAddress, port, null);
	}

	public static Aika create(InetAddress inetAddress, int port, String name) {
		return new AikaImpl(inetAddress, port, name);
	}

	public abstract AikaProxy getAikaProxy();

	public abstract void setConnectListener(AikaConnectListener listener);

	public abstract AikaConnectListener getConnectListener();

	public abstract void setControlListener(AikaControlListener listener);

	public abstract AikaControlListener getControlListener();

	public abstract void createContext(String key, HttpHandler handler);

	public abstract void config(Device device);

	public boolean restart(int port) {
		stop();
		return start();
	}

	public abstract boolean start();

	public abstract void stop();

	public interface AikaConnectListener {
		public void video(String url, String rate, String pos)
				throws AirplayException;

		public void photo() throws AirplayException;
	}

	public interface AikaControlListener {

		public void videoStop() throws AirplayException;

		public void videoPause() throws AirplayException;
	}
}
