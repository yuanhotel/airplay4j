package com.yutel.silver.util;

import com.yutel.silver.http.HttpProtocol;
import com.yutel.silver.vo.ControlState;
import com.yutel.silver.vo.Device;

public class AirplayUtil {
	private final static StringBuffer res = new StringBuffer();
	private final static String serverInfo;
	private final static String playbackInfo;
	static {
		serverInfo = serverInfo();
		playbackInfo = playbackInfo();
	}

	public static void main(String[] args) {
		Device d = new Device();
		d.setDeviceid("58:55:CA:1A:E2:88");
		d.setFeatures("119");
		d.setModel("AppleTV2,1");
		d.setProtovers("1.0");
		d.setSrcvers("120.2");
		System.out.println(getServerInfo(d));
	}

	public static String getServerInfo(Device d) {
		return String.format(serverInfo, d.getDeviceid(), d.getFeatures(),
				d.getModel(), d.getProtovers(), d.getSrcvers());
	}

	public static String getPlaybackInfo(ControlState cs) {
		float duration = cs.getDuration() / 1000f;
		float position = cs.getPosition() / 1000f;
		return String.format(playbackInfo, duration, position,cs.getState());
	}

	private static String serverInfo() {
		res.setLength(0);
		res.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(
				HttpProtocol.CRLF);
		res.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\"");
		res.append(" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">")
				.append(HttpProtocol.CRLF);
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
		res.append("</plist>").append(HttpProtocol.CRLF)
				.append(HttpProtocol.CRLF);
		return res.toString();
	}

	private static String playbackInfo() {
		res.setLength(0);
		res.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(
				HttpProtocol.CRLF);
		res.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\"");
		res.append(" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">")
				.append(HttpProtocol.CRLF);
		res.append("<plist version=\"1.0\">").append(HttpProtocol.CRLF);
		res.append(" <dict>").append(HttpProtocol.CRLF);

		res.append("  <key>duration</key> <real>%1$f</real>").append(
				HttpProtocol.CRLF);
		res.append("  <key>loadedTimeRanges</key>").append(HttpProtocol.CRLF);
		res.append("   <array> <dict>").append(HttpProtocol.CRLF);
		res.append("    <key>duration</key> <real>%2$f</real>").append(
				HttpProtocol.CRLF);
		res.append("    <key>start</key> <real>0.0</real>")
				.append(HttpProtocol.CRLF);
		res.append("   </dict> </array>").append(HttpProtocol.CRLF);

		res.append("  <key>playbackBufferEmpty</key> <true/>").append(
				HttpProtocol.CRLF);
		res.append("  <key>playbackBufferFull</key> <false/>").append(
				HttpProtocol.CRLF);
		res.append("  <key>playbackLikelyToKeepUp</key> <true/>").append(
				HttpProtocol.CRLF);
		res.append("  <key>position</key> <real>%2$f</real>").append(
				HttpProtocol.CRLF);
		res.append("  <key>rate</key> <real>%3$d</real>")
				.append(HttpProtocol.CRLF);
		res.append("  <key>readyToPlay</key> <true/>")
				.append(HttpProtocol.CRLF);
		res.append("  <key>seekableTimeRanges</key>").append(HttpProtocol.CRLF);
		res.append("   <array> <dict>").append(HttpProtocol.CRLF);
		res.append("    <key>duration</key> <real>%1$f</real>").append(
				HttpProtocol.CRLF);
		res.append("    <key>start</key> <real>0.0</real>").append(
				HttpProtocol.CRLF);
		res.append("   </dict> </array>").append(HttpProtocol.CRLF);

		res.append(" </dict>").append(HttpProtocol.CRLF);
		res.append("</plist>").append(HttpProtocol.CRLF)
				.append(HttpProtocol.CRLF);
		return res.toString();
	}
}
