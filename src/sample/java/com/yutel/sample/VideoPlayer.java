package com.yutel.sample;

import com.yutel.silver.Aika.AikaControlListener;
import com.yutel.silver.exception.AirplayException;

public class VideoPlayer implements AikaControlListener {

	public void init() {
		AikaServer as = AikaServer.getInstance();
		as.getAika().setControlListener(this);
	}

	@Override
	public void videoStop() throws AirplayException {
		System.out.println("viewPlayer.stop");
	}

	@Override
	public void videoPause() throws AirplayException {
		System.out.println("viewPlayer.Pause");
	}

}
