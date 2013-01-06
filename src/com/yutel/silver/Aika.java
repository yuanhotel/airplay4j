package com.yutel.silver;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import com.yutel.silver.http.AirplayServer;
import com.yutel.silver.http.handler.PlayHandler;
import com.yutel.silver.http.handler.ReverseHandler;
import com.yutel.silver.http.handler.ServerInfoHandler;
import com.yutel.silver.util.CommonUtil;

public class Aika {
	private static Logger logger = Logger.getLogger(Aika.class.getName());
	private static String type = "_airplay._tcp.local.";
	private static JmDNS jmdns;
	AirplayServer ah;

	public static void main(String[] args) {
		new Aika().start(8888);
	}

	public boolean restart(int port) {
		stop();
		return start(port);
	}

	public boolean start(int port) {
		try {
			// http server
			ah = new AirplayServer(port);
			ah.createContext("/reverse", new ReverseHandler());
			ah.createContext("/server-info", new ServerInfoHandler());
			ah.createContext("/play", new PlayHandler());
			ah.start();
			// jmdns server
			InetAddress address = CommonUtil.getAddress();
			jmdns = JmDNS.create(address);
			logger.log(Level.INFO, "oOpened JmDNS!");
			final HashMap<String, String> values = new HashMap<String, String>();
			values.put("deviceid", "58:55:CA:1A:E2:44");
			values.put("model", "AppleTV2,1");
			values.put("features", "0x77");
			ServiceInfo serviceInfo = ServiceInfo.create(type, "AndroidTest",
					port, 0, 0, values);
			jmdns.registerService(serviceInfo);
			logger.log(Level.INFO, "Registered Service as " + serviceInfo);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			stop();
			return false;
		}
	}

	public void stop() {
		try {
			// jmdns
			if (jmdns != null) {
				jmdns.unregisterAllServices();
				jmdns.close();
				jmdns = null;
			}
			// http
			if (ah != null) {
				ah.forceStop();
				ah = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
