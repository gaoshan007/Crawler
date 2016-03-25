package com.jikexueyuan.util;

public class ClassUtil {
	
	public static String getClassPath(Class<?> c) {
		return c.getResource("").getPath().replaceAll("%20", " ");
	}
	
	public static String getClassRootPath(Class<?> c) {
		return c.getResource("/").getPath().replaceAll("%20", " ");
	}
	
	public static void main(String[] args) {
		
	}

}
