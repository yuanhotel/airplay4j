package com.yutel.silver.vo;

import java.io.Serializable;

public class ControlState implements Serializable {
	private static final long serialVersionUID = -38552173635504318L;
	private int state;
	private int duration;
	private int position;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
