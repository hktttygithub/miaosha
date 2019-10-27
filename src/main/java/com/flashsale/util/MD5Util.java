package com.flashsale.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	private static final String salt = "1a2b3c4d";

	//根据服务器固定的salt进行加密
	public static String inputPassToFormPass(String inputPass) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}

	//根据特定的salt进行加密
	public static String formPassToDBPass(String formPass, String salt) {
		String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}

	//直接将明文加密两次
	public static String inputPassToDbPass(String inputPass, String saltDB) {
		String formPass = inputPassToFormPass(inputPass);
		String dbPass = formPassToDBPass(formPass, saltDB);
		return dbPass;
	}


	
	public static void main(String[] args) {

		System.out.println("000000两次加密后：" + inputPassToDbPass("000000","556788"));//d3b1294a61a07da9b49b6e22b2cbd7f9
//		System.out.println("123456两次加密后：" + inputPassToDbPass("123456","hk2009"));//d3b1294a61a07da9b49b6e22b2cbd7f9
//		System.out.println(inputPassToDbPass("d3b1294a61a07da9b49b6e22b2cbd7f9","hk2009"));
//		System.out.println(formPassToDBPass(inputPassToFormPass("123456"), "1a2b3c4d"));
//		System.out.println(inputPassToDbPass("123456", "1a2b3c4d"));//b7797cce01b4b131b433b6acf4add449
	}
	
}
