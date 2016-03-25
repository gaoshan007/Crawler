/**
 * 
 */
package com.jikexueyuan.crawl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.jikexueyuan.util.CharsetUtil;



/**
 * @author gaoshan
 *
 */
public abstract class CrawlBase {
	
	private static Logger log = Logger.getLogger(CrawlBase.class);
	private String pageSourceCode = "";
	private Header[] reponseHeaders = null;
	private static int connectTimeOut = 10000;
	private static int readTimeOut = 10000;
	private static int maxConnectTimes = 3;
	private static String charsetName = "iso-8859-1";
	private static MultiThreadedHttpConnectionManager httpConnectManager = new MultiThreadedHttpConnectionManager();
	private static HttpClient httpClient = new HttpClient(httpConnectManager);
	
	static {
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeOut);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(readTimeOut);
		httpClient.getParams().setContentCharset("utf-8");
	}
	
	public boolean readPageByGet(String urlStr, HashMap<String, String> params, String charsetName) {
		GetMethod method = createGetMethod(urlStr, params);
		return readPage(method, charsetName, urlStr);
	}
	
	public boolean readPageByPost(String urlStr, HashMap<String, String> params, String charsetName) {
		PostMethod method = createPostMethod(urlStr, params);
		return readPage(method, charsetName, urlStr);
	}
	
	
	private boolean readPage(HttpMethod method, String defaultCharset, String urlStr) {
		int n = maxConnectTimes;
		while(n > 0) {
			try {
				if(httpClient.executeMethod(method) != HttpStatus.SC_OK) {
					log.info("con't connect " + urlStr + (maxConnectTimes - n + 1));
					n --;	
				} else {
					reponseHeaders = method.getRequestHeaders();
					InputStream inputStream = method.getResponseBodyAsStream();
					BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
					StringBuffer stringBuffer = new StringBuffer();
					String lineString = "";
					while((lineString = bufferReader.readLine()) != null) {
						stringBuffer.append(lineString);
						stringBuffer.append("\n");
					}
					pageSourceCode = stringBuffer.toString();
					InputStream in = new ByteArrayInputStream(pageSourceCode.getBytes(charsetName));
					String charset = CharsetUtil.getStreamCharset(in, defaultCharset);
					//String charset = defaultCharset;
					if(!charsetName.toLowerCase().equals(charset.toLowerCase())) {
						pageSourceCode = new String(pageSourceCode.getBytes(charsetName), charset);
					}
					return true;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error(urlStr + "con't connect " + (maxConnectTimes - n + 1));
				n --;
			}
		}
		return false;
	}
	
	private GetMethod createGetMethod(String urlStr, HashMap<String, String> params) {
		GetMethod method = new GetMethod(urlStr);
		if(params == null) {
			return method;
		}
		Iterator<Entry<String, String>> itor = params.entrySet().iterator();
		while(itor.hasNext()) {
			Entry entry = itor.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			method.setRequestHeader(key, val);
		}
		return method;
	}
	
	private PostMethod createPostMethod(String urlStr, HashMap<String, String> params) {
		PostMethod method = new PostMethod(urlStr);
		if(params == null) {
			return method;
		}
		Iterator<Entry<String, String>> itor = params.entrySet().iterator();
		while(itor.hasNext()) {
			Entry entry = itor.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			method.setRequestHeader(key, val);
		}
		return method;
	}
	
	
	public static Logger getLog() {
		return log;
	}


	public static void setLog(Logger log) {
		CrawlBase.log = log;
	}


	public String getPageSourceCode() {
		return pageSourceCode;
	}


	public void setPageSourceCode(String pageSourceCode) {
		this.pageSourceCode = pageSourceCode;
	}


	public Header[] getReponseHeaders() {
		return reponseHeaders;
	}


	public void setReponseHeaders(Header[] reponseHeaders) {
		this.reponseHeaders = reponseHeaders;
	}


	public static int getConnectTimeOut() {
		return connectTimeOut;
	}


	public static void setConnectTimeOut(int connectTimeOut) {
		CrawlBase.connectTimeOut = connectTimeOut;
	}


	public static int getReadTimeOut() {
		return readTimeOut;
	}


	public static void setReadTimeOut(int readTimeOut) {
		CrawlBase.readTimeOut = readTimeOut;
	}


	public static int getMaxConnectTimes() {
		return maxConnectTimes;
	}


	public static void setMaxConnectTimes(int maxConnectTimes) {
		CrawlBase.maxConnectTimes = maxConnectTimes;
	}


	public static String getCharsetName() {
		return charsetName;
	}


	public static void setCharsetName(String charsetName) {
		CrawlBase.charsetName = charsetName;
	}


	public static MultiThreadedHttpConnectionManager getHttpConnectManager() {
		return httpConnectManager;
	}


	public static void setHttpConnectManager(
			MultiThreadedHttpConnectionManager httpConnectManager) {
		CrawlBase.httpConnectManager = httpConnectManager;
	}


	public static HttpClient getHttpClient() {
		return httpClient;
	}


	public static void setHttpClient(HttpClient httpClient) {
		CrawlBase.httpClient = httpClient;
	}


	public static void main(String[] args) {
		
	}
	
}
