/**
 * 
 */
package com.jikexueyuan.util;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;


public class CharsetUtil {

	
	private static final CodepageDetectorProxy detector;
	
	static {
		detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		detector.add(JChardetFacade.getInstance());
	}
	
	public static String getStreamCharset(InputStream inputStream, String defaultCharset) {
		if(inputStream == null) {
			return defaultCharset;
		}
		int count = 200;
		try {
			count = inputStream.available();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Charset charset = detector.detectCodepage(inputStream, count);
			if(charset != null) {
				return charset.name();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return defaultCharset;
		
	}
	
	
	public static String getStreamCharset(URL url, String defaultCharset) {
		if(url == null) {
			return defaultCharset;
		}
		
		try {
			Charset charset = detector.detectCodepage(url);
			if(charset != null) {
				return charset.name();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return defaultCharset;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
