package com.yutel.silver.exception;

public class AirplayException extends Exception {
	private static final long serialVersionUID = -781002568217221389L;

	public AirplayException(String msg) {
		super(msg);
	}

	public AirplayException(String msg, Exception e) {
		super(msg, e);
	}

	public AirplayException(Exception e) {
		super(e);
	}

}
