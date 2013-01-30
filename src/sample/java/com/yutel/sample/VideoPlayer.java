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

	@Override
	public void videoResume() throws AirplayException {
		System.out.println("viewPlayer.resume");
	}

	@Override
	public void videoSeek(int position) throws AirplayException {
		System.out.println("videoSeek=" + position);
	}

	@Override
	public int videoStatus() throws AirplayException {
		return 0;
	}

	@Override
	public int videoPostion() throws AirplayException {
		return 0;
	}

	@Override
	public int videoDuration() throws AirplayException {
		return 0;
	}

}
