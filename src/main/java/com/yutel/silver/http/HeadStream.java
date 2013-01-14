package com.yutel.silver.http;

import java.io.ByteArrayOutputStream;

public class HeadStream extends ByteArrayOutputStream {
	public synchronized boolean equalTail(byte[] temp) {
		if (temp == null) {
			return false;
		}
		int post = count - temp.length;
		if (post < 0) {
			return false;
		}
		for (int i = post, j = 0; j < temp.length; i++, j++) {
			if (buf[i] != temp[j]) {
				return false;
			}
		}
		return true;
	}
}
