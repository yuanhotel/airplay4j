package com.yutel.silver.http;

import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;
import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.util.AirplayUtil;
import com.yutel.silver.util.StringUtil;
import com.yutel.silver.vo.AirplayState;

public class DefaultHandler {
	protected AirplayServer server;
	protected HttpWrap wrap;

	public DefaultHandler(AirplayServer server, HttpWrap wrap) {
		this.server = server;
		this.wrap = wrap;
	}

	public void process() {
		try {
			if ("/reverse".equals(wrap.getContext())) {
				wrap.setReverse(true);
				wrap.setResponseCode(101);
			} else if ("/server-info".equals(wrap.getContext())) {
				wrap.setReverse(true);
				wrap.setResponseCode(200);
				wrap.getResponseHeads().put("Content-Type",
						"text/x-apple-plist+xml");
				String res = AirplayUtil.getServerInfo(server.getDevice());
				wrap.setBodys(res);
			} else if ("/rate".equals(wrap.getContext())) {
				wrap.setResponseCode(200);
				String rate = wrap.getRequestParameters().get("value");
				if (rate != null) {
					float ratef = StringUtil.toFloat(rate);
					int ratei = (int) ratef;
					if (ratei == 1) {
						server.getProxy().videoResume();
						wrap.setReverseEvent(AirplayState.EVENT_PLAYING);
					} else {
						server.getProxy().videoPause();
						wrap.setReverseEvent(AirplayState.EVENT_PAUSED);
					}
				}
				String res = AirplayUtil.getServerInfo(server.getDevice());
				wrap.setBodys(res);
			} else if ("/stop".equals(wrap.getContext())) {
				wrap.setReverse(true);
				wrap.setReverseEvent(AirplayState.EVENT_STOPPED);
			} else if ("/scrub".equals(wrap.getContext())) {
				if ("GET".equals(wrap.getContext())) {
					wrap.setResponse(true);
					StringBuffer sb = new StringBuffer();
					sb.append("duration: ")
							.append(server.getProxy().videoDuration())
							.append(HttpProtocol.CRLF);
					sb.append("position: ")
							.append(server.getProxy().videoPostion())
							.append(HttpProtocol.CRLF);
					wrap.setBodys(sb.toString());
				} else {
					String requestbody = new String(wrap.getResponseBody());
					int start = requestbody.indexOf("position=");
					String pos = requestbody.substring(start + 9);
					int position = StringUtil.toInteger(pos);
					server.getProxy().videoSeek(position);
				}
				wrap.setReverse(true);
				if (server.getProxy().videoStatus() == 1
						|| server.getProxy().videoStatus() == 2) {
					wrap.setReverseEvent(AirplayState.EVENT_PLAYING);
				}
			} else if ("/stop".equals(wrap.getContext())) {
				wrap.setReverse(true);
				wrap.setReverseEvent(AirplayState.EVENT_STOPPED);
			} else if ("/playback-info".equals(wrap.getContext())) {
				playbackInfo();
			} else if ("/play".equals(wrap.getContext())) {
				play();
			} else {
				wrap.setReverse(true);
				wrap.setResponseCode(404);
			}
		} catch (AirplayException ae) {
			ae.printStackTrace();
		}
	}

	private void play() {
		try {
			if (wrap.getResponseBody() != null
					&& wrap.getResponseBody().length > 0) {
				String conType = wrap.getRequestHeads().get(
						HttpProtocol.ContentType);
				System.out.println("ContentType=" + conType);
				if (AirplayState.binPLIST.equals(conType)) {
					NSDictionary rootDict = (NSDictionary) PropertyListParser
							.parse(wrap.getResponseBody());
					String url = rootDict.objectForKey("Content-Location")
							.toString();
					String rate = rootDict.objectForKey("rate").toString();
					String pos = "0f";
					NSObject p = rootDict.objectForKey("Start-Position");
					if (p != null) {
						pos = p.toString();
					}
					System.out.println("rl=" + url);
					server.getProxy().video(url, rate, pos);
				} else {
					System.out.println("body="
							+ wrap.getResponseBody().toString());
				}
			} else {
				System.out.println("body is null");
			}
		} catch (AirplayException ae) {
			ae.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void playbackInfo() {
		try {
			wrap.setResponse(true);
			wrap.setReverse(true);
			int state = server.getProxy().videoStatus();
			int duration = server.getProxy().videoDuration();
			int position = server.getProxy().videoPostion();
			wrap.getResponseHeads().put(HttpProtocol.ContentType,
					AirplayState.txtPLIST);
			if (state != AirplayState.STOPPED) {
				wrap.setBodys(AirplayUtil.getPlaybackInfo(duration, position,
						state));
			} else {
				wrap.setBodys(AirplayUtil.getPlaybackInfoNotReady());
			}
			switch (state) {
			case AirplayState.STOPPED:
				wrap.setReverseEvent(AirplayState.EVENT_STOPPED);
				break;
			case AirplayState.CACHING:
				wrap.setReverseEvent(AirplayState.EVENT_LOADING);
				break;
			case AirplayState.PAUSING:
				wrap.setReverseEvent(AirplayState.EVENT_PAUSED);
				break;
			case AirplayState.PLAYING:
				wrap.setReverseEvent(AirplayState.EVENT_PLAYING);
				break;
			}
		} catch (AirplayException ae) {
			ae.printStackTrace();
		}
	}

}
