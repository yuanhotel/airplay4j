package com.yutel.sample;

import java.net.InetAddress;

import com.yutel.sample.util.InetAddressUtil;
import com.yutel.silver.vo.Device;

public class Main {

	public static void main(String[] args) {
		try {
			Device dev = new Device();
			dev.setDeviceid("58:55:CA:1A:E2:44");
			dev.setFeatures("0x277");
			dev.setModel("AppleTV2,1");
			dev.setProtovers("1.0");
			dev.setSrcvers("120.2");
			InetAddress ia = InetAddressUtil.getAddress();
			System.out.println("ip="+ia.getHostAddress());
			AikaServer as = AikaServer.getInstance(dev, ia);
			as.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
