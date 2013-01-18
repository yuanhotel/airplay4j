package com.yutel.silver;

import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.vo.ControlState;

public class AikaProxy implements Aika.AikaConnectListener,
		Aika.AikaControlListener {
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
	public ControlState videoStop() throws AirplayException {
		return controlListener.videoStop();
	}

	@Override
	public ControlState videoPause() throws AirplayException {
		return controlListener.videoPause();
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

	@Override
	public ControlState scrub() throws AirplayException {
		return controlListener.scrub();
	}

	@Override
	public ControlState videoResume() throws AirplayException {
		return controlListener.videoResume();
	}
}
