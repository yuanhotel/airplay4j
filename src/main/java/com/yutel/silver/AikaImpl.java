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
import com.yutel.silver.vo.Device;

public class AikaImpl extends Aika {
	private static Logger logger = Logger.getLogger(AikaImpl.class.getName());
	private int mPort;
	private AirplayServer as;
	private JmDNS mJmDNS;
	private InetAddress mInetAddress;
	private String mType = "_airplay._tcp.local.";
	private String mName;
	private Device mDevice;
	private HashMap<String, HttpHandler> mHandlers;
	private AikaProxy mProxy;
	private HashMap<String, String> mConfig;

	public AikaImpl(InetAddress inetAddress, int port, String name) {
		mInetAddress = inetAddress;
		mPort = port < 7000 ? 7000 : port;
		mName = name == null ? "aika" : name;
		mHandlers = new HashMap<String, HttpHandler>();
		mProxy = new AikaProxy();
	}

	@Override
	public void createContext(String key, HttpHandler handler) {
		mHandlers.put(key, handler);
	}

	@Override
	public AikaProxy getAikaProxy() {
		return mProxy;
	}

	@Override
	public void setConnectListener(AikaConnectListener listener) {
		mProxy.setConnectListener(listener);
	}

	@Override
	public AikaConnectListener getConnectListener() {
		return mProxy.getConnectListener();
	}

	@Override
	public void setControlListener(AikaControlListener listener) {
		mProxy.setControlListener(listener);
	}

	@Override
	public AikaControlListener getControlListener() {
		return mProxy.getControlListener();
	}

	@Override
	public void config(Device device) {
		mDevice = device;
		mConfig = new HashMap<String, String>();
		mConfig.put("deviceid", device.getDeviceid());
		mConfig.put("model", device.getModel());
		mConfig.put("features", device.getFeatures());
	}

	@Override
	public boolean start() {
		try {
			// http server
			as = new AirplayServer(mPort);
			as.setProxy(this.getAikaProxy());
			as.setDevice(mDevice);
			// as.setHandlers(mHandlers);
			as.start();
			// jmdns server
			mJmDNS = JmDNS.create(mInetAddress);
			logger.log(Level.INFO, "Opened JmDNS!");
			ServiceInfo serviceInfo = ServiceInfo.create(mType, mName, mPort,
					0, 0, mConfig);
			mJmDNS.registerService(serviceInfo);
			logger.log(Level.INFO, "Registered Service as " + serviceInfo);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			stop();
			return false;
		}
	}

	@Override
	public void stop() {
		try {
			// jmdns
			if (mJmDNS != null) {
				mJmDNS.unregisterAllServices();
				mJmDNS.close();
				mJmDNS = null;
				logger.log(Level.INFO, "JmDNS stoped");
			}
			// http
			if (as != null) {
				as.forceStop();
				as = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
