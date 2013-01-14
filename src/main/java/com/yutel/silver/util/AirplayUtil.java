package com.yutel.silver.util;

import com.yutel.silver.http.HttpProtocol;
import com.yutel.silver.vo.Device;

public class AirplayUtil {
	private static StringBuffer res = new StringBuffer();

	public static void main(String[] args) {
		Device d=new Device();
		d.setDeviceid("58:55:CA:1A:E2:88");
		d.setFeatures("119");
		d.setModel("AppleTV2,1");
		d.setProtovers("1.0");
		d.setSrcvers("120.2");
		System.out.println(getServerInfo(d));
	}

	public static String getServerInfo(Device d) {
		return String.format(serverInfo(), d.getDeviceid(), d.getFeatures(),
				d.getModel(), d.getProtovers(), d.getSrcvers());
	}

	private static String serverInfo() {
		res.setLength(0);
		res.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(HttpProtocol.CRLF);
		res.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\"");
		res.append(" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">").append(HttpProtocol.CRLF);
		res.append("<plist version=\"1.0\">").append(HttpProtocol.CRLF);
		res.append(" <dict>").append(HttpProtocol.CRLF);
		res.append("  <key>deviceid</key>").append(HttpProtocol.CRLF);
		res.append("  <string>%1$s</string>").append(HttpProtocol.CRLF);
		res.append("  <key>features</key>").append(HttpProtocol.CRLF);
		res.append("  <integer>%2$S</integer>").append(HttpProtocol.CRLF);
		res.append("  <key>model</key>").append(HttpProtocol.CRLF);
		res.append("  <string>%3$s</string>").append(HttpProtocol.CRLF);
		res.append("  <key>protovers</key>").append(HttpProtocol.CRLF);
		res.append("  <string>%4$s</string>").append(HttpProtocol.CRLF);
		res.append("  <key>srcvers</key>").append(HttpProtocol.CRLF);
		res.append("  <string>%5$s</string>").append(HttpProtocol.CRLF);
		res.append(" </dict>").append(HttpProtocol.CRLF);
		res.append("</plist>").append(HttpProtocol.CRLF).append(HttpProtocol.CRLF);
		return res.toString();
	}

	public static String int2ip(int ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}
}
