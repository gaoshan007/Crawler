package com.jikexueyuan.util;

public class ParseMD5 extends Encrypt {

	public static String parseStrToMD5(String str) {
		return encrypt(str, MD5);
	}
	
	public static String parseStrToUpperMD5(String str) {
		return parseStrToMD5(str).toUpperCase();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(ParseMD5.parseStrToMD5("abc"));
	}

}
