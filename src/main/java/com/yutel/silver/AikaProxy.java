package com.yutel.silver;

import com.yutel.silver.exception.AirplayException;

public class AikaProxy implements Aika.AikaConnectListener, Aika.AikaControlListener {
	private Aika.AikaConnectListener connectListener;
	private Aika.AikaControlListener controlListener;

	public AikaProxy() {
	}

	public Aika.AikaConnectListener getConnectListener() {
		return connectListener;
	}

	public void setConnectListener(Aika.AikaConnectListener connectListener) {
		this.connectListener = connectListener;
	}

	public Aika.AikaControlListener getControlListener() {
		return controlListener;
	}

	public void setControlListener(Aika.AikaControlListener controlListener) {
		this.controlListener = controlListener;
	}

	@Override
	public void videoStop() throws AirplayException {
		controlListener.videoStop();
	}

	@Override
	public void videoPause() throws AirplayException {
		controlListener.videoPause();
	}

	@Override
	public void video(String url, String rate, String pos)
			throws AirplayException {
		connectListener.video(url, rate, pos);
	}

	@Override
	public void photo() throws AirplayException {
		connectListener.photo();
	}
}
