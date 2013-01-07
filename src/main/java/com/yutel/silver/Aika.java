package com.yutel.silver;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import com.yutel.silver.http.AirplayServer;
import com.yutel.silver.http.handler.HttpHandler;

public class Aika {
	private static Logger logger = Logger.getLogger(Aika.class.getName());
	private static AirplayServer ah;
	private static JmDNS jmdns;
	private static InetAddress mInetAddress;
	private static String mType = "_airplay._tcp.local.";
	private static String mName;
	private HashMap<String, HttpHandler> handlers;
	private HashMap<String, String> values;

	public Aika(InetAddress inetAddress) {
		mInetAddress = inetAddress;
		mName = "aika";
		handlers = new HashMap<String, HttpHandler>();
		values = new HashMap<String, String>();
	}

	public Aika(InetAddress inetAddress, String name) {
		mInetAddress = inetAddress;
		mName = name;
		handlers = new HashMap<String, HttpHandler>();
		values = new HashMap<String, String>();
	}

	public void createContext(String key, HttpHandler handler) {
		handlers.put(key, handler);
	}

	public void ConfigDivice(String key, String value) {
		values.put(key, value);
	}

	public boolean restart(int port) {
		stop();
		return start(port);
	}

	public boolean start(int port) {
		try {
			// http server
			ah = new AirplayServer(port);
			ah.setHandlers(handlers);
			ah.start();
			// jmdns server
			jmdns = JmDNS.create(mInetAddress);
			logger.log(Level.INFO, "oOpened JmDNS!");
			ServiceInfo serviceInfo = ServiceInfo.create(mType, mName, port, 0,
					0, values);
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
