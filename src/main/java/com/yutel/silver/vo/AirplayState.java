package com.yutel.silver.vo;

public class AirplayState {
	public static final int ERROR = -1; // 取值出错
	public static final int PLAYING = 1; // 正在播放
	public static final int PAUSING = 2; // 暂停状态
	public static final int STOPPED = 3; // 停止状态
	public static final int SEEKING = 4; // 正在seek
	public static final int CURPOS = 5; // current position
	public static final int DURATION = 6; // media duration
	public static final int CACHING = 7; // 缓存

	public static final int EVENT_NONE = -1;
	public static final int EVENT_PLAYING = 0;
	public static final int EVENT_PAUSED = 1;
	public static final int EVENT_LOADING = 2;
	public static final int EVENT_STOPPED = 3;

	public final static String TEXT = "text/parameters";
	public final static String binPLIST = "application/x-apple-binary-plist";
	public final static String txtPLIST = "text/x-apple-plist+xml";
	public final static String ContentLength = "Content-Length";
}
