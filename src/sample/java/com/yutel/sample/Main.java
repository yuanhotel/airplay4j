package com.yutel.sample;

import com.yutel.sample.handler.PlayHandler;
import com.yutel.sample.handler.ReverseHandler;
import com.yutel.sample.handler.ServerInfoHandler;
import com.yutel.sample.util.InetAddressUtil;
import com.yutel.silver.Aika;

public class Main {

	public static void main(String[] args) {
		int argc = args.length;
		if(argc==1) {
			for(String item:args) {
				System.out.println(":"+item);
			}
			if("start".equals(args[0])) {
				Aika aika = Aika.getInstance(InetAddressUtil.getAddress(),null);
				//http context
				aika.createContext("/reverse", new ReverseHandler());
				aika.createContext("/server-info", new ServerInfoHandler());
				aika.createContext("/play", new PlayHandler());
				//device info
				aika.ConfigDivice("deviceid", "58:55:CA:1A:E2:44");
				aika.ConfigDivice("model", "AppleTV2,1");
				aika.ConfigDivice("features", "0x77");
				aika.start(8888);
			} else if("stop".equals(args[0])) {
				Aika aika = Aika.getInstance(InetAddressUtil.getAddress(),null);
				aika.stop();
			}
			
		} else {
			System.out.println("java -jar sample.jar <option>");
			System.out.println("   option: start stop");
		}
	}

}
