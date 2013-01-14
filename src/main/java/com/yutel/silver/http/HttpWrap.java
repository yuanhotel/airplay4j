package com.yutel.silver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import com.yutel.silver.http.HttpAnaly.HttpEntry;

public class HttpWrap {
	private HttpEntry entry;
	private HttpAnaly ha;

	public boolean extract(Socket socket) {
		ha = new HttpAnaly(socket);
		entry = ha.analy();
		return true;
	}

	public String getContext() {
		return entry == null ? null : entry.getContext();
	}

	public Map<String, String> getRequestHeads() {
		return entry == null ? null : entry.getRequestHeads();
	}

	public Map<String, String> getResponseHeads() {
		return entry == null ? null : entry.getResponseHeads();
	}

	public InputStream getRequestBody() {
		return entry == null ? null : entry.getRequestBody();
	}

	public OutputStream getResponseBody() {
		return entry == null ? null : entry.getResponseBody();
	}

	public void sendResponseHeaders(int responseCode, int length) {
		try {
			String res = ha.buildResponseHeaders(responseCode, length);
			System.out.println("res=" + res);
			entry.getResponseBody().write(res.getBytes());
			entry.getResponseBody().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * public boolean extract_bak(Socket socket) { try { InputStream is =
	 * socket.getInputStream(); // 获得输入流 OutputStream os =
	 * socket.getOutputStream(); // 获得输入流 entry.requestBody = is;
	 * entry.responseBody = os; int bufsize = 8192; byte[] buf = new
	 * byte[bufsize]; int rlen = is.read(buf, 0, bufsize); if (rlen <= 0) {
	 * return false; } logger.info("rlen=" + rlen); // We are looking for the
	 * byte separating header from body. // It must be the last byte of the
	 * first two sequential new lines. int splitbyte = 0; boolean sbfound =
	 * false; while (splitbyte < rlen) { if (buf[splitbyte] == '\r' &&
	 * buf[++splitbyte] == '\n' && buf[++splitbyte] == '\r' && buf[++splitbyte]
	 * == '\n') { sbfound = true; break; } splitbyte++; } splitbyte++;
	 * logger.info("splitbyte=" + splitbyte); // Write the part of body already
	 * read to ByteArrayOutputStream f ByteArrayOutputStream f = new
	 * ByteArrayOutputStream(); if (splitbyte <= rlen) { // head f.write(buf, 0,
	 * splitbyte); byte[] fbuf = f.toByteArray(); ByteArrayInputStream bin = new
	 * ByteArrayInputStream(fbuf); BufferedReader in = new BufferedReader(new
	 * InputStreamReader( bin)); String tempbf = in.readLine(); if (tempbf ==
	 * null || tempbf.length() < 1) { return false; } logger.info("fistline=" +
	 * tempbf); fistline(tempbf); tempbf = in.readLine(); while (tempbf != null
	 * && tempbf.length() > 0) { header(tempbf); tempbf = in.readLine(); }
	 * logger.info("header parse compled"); // body if (sbfound) { int len =
	 * rlen - splitbyte; byte[] body = new byte[len]; System.arraycopy(buf,
	 * splitbyte, body, 0, len); entry.setRequestBodys(body);
	 * logger.info("body.size=" + entry.requestBodys.length); } return true; }
	 * return false; } catch (IOException e) { e.printStackTrace(); return
	 * false; } catch (Exception e) { e.printStackTrace(); return false; } }
	 * 
	 * public void sendResponseHeaders(int responseCode, int length) { try {
	 * entry.responseBody.write(HttpProtocol.getResponse(responseCode)
	 * .getBytes()); for (Map.Entry<String, String> item : entry.responseHeads
	 * .entrySet()) { entry.responseBody.write((item.getKey() + ": " +
	 * item.getValue() + HttpProtocol.CRLF).getBytes()); }
	 * entry.responseBody.write(("Date: " + HttpProtocol.getGMTTime(new Date())
	 * + HttpProtocol.CRLF) .getBytes());
	 * entry.responseBody.write(("Content-Length: " + length + HttpProtocol.CRLF
	 * + HttpProtocol.CRLF).getBytes()); if (length == 0) { entry.responseBody
	 * .write((HttpProtocol.CRLF + HttpProtocol.CRLF) .getBytes()); } } catch
	 * (IOException e) { e.printStackTrace(); } }
	 * 
	 * public Map<String, String> getRequestHeads() { return entry.requestHeads;
	 * }
	 * 
	 * public Map<String, String> getResponseHeads() { return
	 * entry.responseHeads; }
	 * 
	 * public String getContext() { return context; }
	 * 
	 * public void setContext(String context) { this.context = context; }
	 * 
	 * public String getMothod() { return mothod; }
	 * 
	 * public void setMothod(String mothod) { this.mothod = mothod; }
	 * 
	 * public String getProtocol() { return protocol; }
	 * 
	 * public void setProtocol(String protocol) { this.protocol = protocol; }
	 * 
	 * public InputStream getRequestBody() { return entry.requestBody; }
	 * 
	 * public byte[] getRequestBodys() { return entry.requestBodys; }
	 * 
	 * public OutputStream getResponseBody() { return entry.responseBody; }
	 * 
	 * private void header(String line) { if (line != null &&
	 * !"".equals(line.trim())) { String[] parts = line.split(":"); if
	 * (parts.length == 2) { entry.getRequestHeads().put(parts[0].trim(),
	 * parts[1].trim()); } } }
	 * 
	 * private void fistline(String line) { String[] parts = line.split(" "); if
	 * (parts.length >= 2) { setMothod(parts[0]); setContext(parts[1]); } if
	 * (parts.length >= 3) { setProtocol(parts[2]); } }
	 * 
	 * public class Entry { private Map<String, String> requestHeads = new
	 * HashMap<String, String>(); private Map<String, String> responseHeads =
	 * new HashMap<String, String>(); private byte[] requestBodys; private
	 * InputStream requestBody; private OutputStream responseBody;
	 * 
	 * public Map<String, String> getRequestHeads() { return requestHeads; }
	 * 
	 * public void setRequestHeads(Map<String, String> requestHeads) {
	 * this.requestHeads = requestHeads; }
	 * 
	 * public Map<String, String> getResponseHeads() { return responseHeads; }
	 * 
	 * public void setResponseHeads(Map<String, String> responseHeads) {
	 * this.responseHeads = responseHeads; }
	 * 
	 * public byte[] getRequestBodys() { return requestBodys; }
	 * 
	 * public void setRequestBodys(byte[] requestBodys) { this.requestBodys =
	 * requestBodys; }
	 * 
	 * public InputStream getRequestBody() { return requestBody; }
	 * 
	 * public void setRequestBody(InputStream requestBody) { this.requestBody =
	 * requestBody; }
	 * 
	 * public OutputStream getResponseBody() { return responseBody; }
	 * 
	 * public void setResponseBody(OutputStream responseBody) {
	 * this.responseBody = responseBody; } }
	 */
}
