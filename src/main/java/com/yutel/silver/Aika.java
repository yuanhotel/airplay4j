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
	private static Aika aika;
	private static Logger logger = Logger.getLogger(Aika.class.getName());
	private AirplayServer ah;
	private JmDNS jmdns;
	private InetAddress mInetAddress;
	private String mType = "_airplay._tcp.local.";
	private String mName;
	private HashMap<String, HttpHandler> handlers;
	private HashMap<String, String> values;

	private Aika(InetAddress inetAddress, String name) {
		mInetAddress = inetAddress;
		mName = name == null ? "aika" : name;
		handlers = new HashMap<String, HttpHandler>();
		values = new HashMap<String, String>();
	}

	public static Aika getInstance(InetAddress inetAddress, String name) {
		if (aika == null) {
			aika = new Aika(inetAddress, name);
		}
		return aika;
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
			aika = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
