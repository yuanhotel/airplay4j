package com.yutel.silver.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import com.yutel.silver.util.StringUtil;

public class HttpAnaly {
	private static Logger logger = Logger.getLogger(HttpAnaly.class.getName());
	public final static char CR = '\r';
	public final static char LF = '\n';

	private HeadStream mHeadStream;
	private HttpWrap mHttpWrap;
	private InputStream mInputStream;
	private OutputStream mOutputStream;

	public HttpAnaly(InputStream inputStream, OutputStream outputStream) {
		mInputStream = inputStream;
		mOutputStream = outputStream;
		mHttpWrap = new HttpWrap();
		mHeadStream = new HeadStream();
	}

	public boolean canRead() {
		read2Mark(new byte[] { (byte) CR, (byte) LF });
		if (mHeadStream.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public HttpWrap parse(int sign) {
		read2Mark(new byte[] { (byte) CR, (byte) LF, (byte) CR, (byte) LF });
		headers();
		bodys();
		if (mHttpWrap.getRequestHeads().size() > 0) {
			logger.info("header(" + mHttpWrap.getContext() + ") parse compled:"
					+ sign + mHttpWrap.getRequestHeads().toString());
		}
		if (mHttpWrap.getResponseBody() != null) {
			logger.info("body=" + mHttpWrap.getResponseBody().toString());
		}
		return mHttpWrap;
	}

	public void sendResponse(String body) {
		try {
			byte[] bodys = body.getBytes();
			mOutputStream.write(bodys);
			mOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void read2Mark(byte[] mark) {
		try {
			byte[] temp = new byte[1];
			while ((mInputStream.read(temp)) != -1) {
				mHeadStream.write(temp);
				if (mHeadStream.equalTail(mark)) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean headers() {
		try {
			byte[] buf = mHeadStream.toByteArray();
			ByteArrayInputStream bin = new ByteArrayInputStream(buf);
			BufferedReader in = new BufferedReader(new InputStreamReader(bin));
			String tempbf = in.readLine();
			if (tempbf == null || tempbf.length() < 1) {
				return false;
			}
			fistline(tempbf);
			tempbf = in.readLine();
			while (tempbf != null && tempbf.length() > 0) {
				header(tempbf);
				tempbf = in.readLine();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	private void bodys() {
		try {
			String body = mHttpWrap.getRequestHeads().get("Content-Length");
			if (body != null && !"".equals(body)) {
				int len = StringUtil.toInteger(body);
				if (len > 0) {
					byte[] data = new byte[len];
					mInputStream.read(data);
					mHttpWrap.setResponseBody(data);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void header(String line) {
		if (line != null && !"".equals(line.trim())) {
			String[] parts = line.split(":");
			if (parts.length == 2) {
				mHttpWrap.getRequestHeads().put(parts[0].trim(),
						parts[1].trim());
			}
		}
	}

	private void fistline(String line) {
		StringTokenizer st = new StringTokenizer(line, " ");
		if (st.hasMoreTokens()) {
			mHttpWrap.setMothod(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			String uri = st.nextToken();
			int qmi = uri.indexOf('?');
			if (qmi >= 0) {
				decodeParms(uri.substring(qmi + 1));
				mHttpWrap.setContext(decodePercent(uri.substring(0, qmi)));
			} else {
				mHttpWrap.setContext(decodePercent(uri));
			}
		}
		if (st.hasMoreTokens()) {
			mHttpWrap.setProtocol(st.nextToken());
		}
	}

	private void decodeParms(String parms) {
		StringTokenizer st = new StringTokenizer(parms, "&");
		while (st.hasMoreTokens()) {
			String e = st.nextToken();
			int sep = e.indexOf('=');
			if (sep >= 0) {
				mHttpWrap.getRequestParameters().put(
						decodePercent(e.substring(0, sep)).trim(),
						decodePercent(e.substring(sep + 1)));
			}
		}
	}

	private String decodePercent(String str) {
		try {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				switch (c) {
				case '+':
					sb.append(' ');
					break;
				case '%':
					sb.append((char) Integer.parseInt(
							str.substring(i + 1, i + 3), 16));
					i += 2;
					break;
				default:
					sb.append(c);
					break;
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
