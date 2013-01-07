package com.yutel.sample;

import com.yutel.sample.handler.PlayHandler;
import com.yutel.sample.handler.ReverseHandler;
import com.yutel.sample.handler.ServerInfoHandler;
import com.yutel.sample.util.InetAddressUtil;
import com.yutel.silver.Aika;

public class Main {

	public static void main(String[] args) {
		Aika aika = new Aika(InetAddressUtil.getAddress());
		//http context
		aika.createContext("/reverse", new ReverseHandler());
		aika.createContext("/server-info", new ServerInfoHandler());
		aika.createContext("/play", new PlayHandler());
		//device info
		aika.ConfigDivice("deviceid", "58:55:CA:1A:E2:44");
		aika.ConfigDivice("model", "AppleTV2,1");
		aika.ConfigDivice("features", "0x77");
		aika.start(8888);
	}

}
