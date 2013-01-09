package com.yutel.sample;

import com.yutel.sample.handler.PlayHandler;
import com.yutel.sample.handler.ReverseHandler;
import com.yutel.sample.handler.ServerInfoHandler;
import com.yutel.sample.util.InetAddressUtil;
import com.yutel.silver.Aika;
import com.yutel.silver.vo.Device;

public class Main {
	private static Device mDevice;

	public static void main(String[] args) {
		int argc = args.length;
		String helper = "java -jar sample.jar <option>\n\toption: start stop";
		if (argc == 1) {
			if ("start".equals(args[0])) {
				mDevice = new Device();
				mDevice.setDeviceid("58:55:CA:1A:E2:88");
				mDevice.setFeatures("119");
				mDevice.setModel("AppleTV2,1");
				mDevice.setProtovers("1.0");
				mDevice.setSrcvers("120.2");
				Aika aika = Aika
						.getInstance(InetAddressUtil.getAddress(), null);
				// http context
				aika.createContext("/reverse", new ReverseHandler());
				aika.createContext("/server-info", new ServerInfoHandler(
						mDevice));
				aika.createContext("/play", new PlayHandler());
				// device info
				aika.ConfigDivice("deviceid", "58:55:CA:1A:E2:44");
				aika.ConfigDivice("model", "AppleTV2,1");
				aika.ConfigDivice("features", "0x277");
				aika.start(8888);
			} else if ("stop".equals(args[0])) {
				Aika.getInstance(InetAddressUtil.getAddress(), null).stop();
			} else {
				System.out.println(helper);
			}
		} else {
			System.out.println(helper);
		}
	}

}
