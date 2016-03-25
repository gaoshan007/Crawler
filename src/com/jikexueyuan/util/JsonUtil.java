/**
 * 
 */
package com.jikexueyuan.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtil {
	
	private static final String noData = "{\"result\": null}";
	private static ObjectMapper mapper;
	
	static {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	public static String parseJson(Object object) {
		if(object == null) {
			return noData;
		}
		try {
			return mapper.writeValueAsString(object);
		} catch(Exception e) {
			e.printStackTrace();
			return noData;
		}
	}
	
	public JsonNode json2Object (String json) {
		try {
			return mapper.readTree(json);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
