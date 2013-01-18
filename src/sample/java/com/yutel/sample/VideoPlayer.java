package com.yutel.sample;

import com.yutel.silver.Aika.AikaControlListener;
import com.yutel.silver.exception.AirplayException;
import com.yutel.silver.vo.ControlState;

public class VideoPlayer implements AikaControlListener {

	public void init() {
		AikaServer as = AikaServer.getInstance();
		as.getAika().setControlListener(this);
	}

	@Override
	public ControlState videoStop() throws AirplayException {
		System.out.println("viewPlayer.stop");
		return null;
	}

	@Override
	public ControlState videoPause() throws AirplayException {
		System.out.println("viewPlayer.Pause");
		return null;
	}

	@Override
	public ControlState scrub() throws AirplayException {
		System.out.println("viewPlayer.scrub");
		return null;
	}

	@Override
	public ControlState videoResume() throws AirplayException {
		System.out.println("viewPlayer.resume");
		return null;
	}

}
