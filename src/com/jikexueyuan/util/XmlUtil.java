package com.jikexueyuan.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

public class XmlUtil {
	private static final String noResult = "<result>no result</result>";
	
	public static Document createFromString(String xml) {
		try {
			return DocumentHelper.parseText(xml);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String parseObject2XmlString(Object object) {
		if(object == null) {
			return noResult;
		}
		StringWriter sw = new StringWriter();
		JAXBContext jAXBContent;
		Marshaller marshaller;
		try {
			jAXBContent = JAXBContext.newInstance(object.getClass());
			marshaller = jAXBContent.createMarshaller();
			marshaller.marshal(object, sw);
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return noResult;
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
