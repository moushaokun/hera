package org.apel.hera.biz.service;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apel.gaia.util.encryption.DESUtil;
import org.apel.gaia.util.encryption.MD5Util;


/**
 * 数据库连接配置文件产生器
 * @author lijian
 *
 */
public class DatabaseConfigGenerator {
	
	public static String buildConfig(String url, String driverClass, String username,
			String passowrd, String xaDsClassName){
		StringBuffer sb;
		try {
			char[] urlChars = new char[] { 0x6a, 0x64, 0x62, 0x63, 0x2e, 0x75,
					0x72, 0x6c };
			char[] driverclassChars = new char[] { 0x6a, 0x64, 0x62, 0x63, 0x2e,
					0x64, 0x72, 0x69, 0x76, 0x65, 0x72, 0x63, 0x6c, 0x61, 0x73,
					0x73 };
			char[] usernameChars = new char[] { 0x6a, 0x64, 0x62, 0x63, 0x2e, 0x75,
					0x73, 0x65, 0x72, 0x6e, 0x61, 0x6d, 0x65 };
			char[] passwordChars = new char[] { 0x6a, 0x64, 0x62, 0x63, 0x2e, 0x70,
					0x61, 0x73, 0x73, 0x77, 0x6f, 0x72, 0x64 };
			char[] xaDsClassNameChars = new char[] { 0x6a, 0x64, 0x62, 0x63, 0x2e,
					0x78, 0x61, 0x44, 0x61, 0x74, 0x61, 0x53, 0x6f, 0x75, 0x72,
					0x63, 0x65, 0x43, 0x6c, 0x61, 0x73, 0x73, 0x4e, 0x61, 0x6d,
					0x65 };
			char[] seed = new char[] {0x77,0x77,0x77,0x2e,0x6f,0x72,0x67,0x2e,0x61,0x70,0x65,0x6c};
			String urlVal = createUUID()
					+ Base64.encodeBase64String(url.getBytes());// jdbc:oracle:thin:@192.168.0.2:1521:HYXT
			String driverclassVal = createUUID()
					+ Base64.encodeBase64String(driverClass.getBytes());
			String usernameVal = createUUID()
					+ Base64.encodeBase64String(username.getBytes());
			String passwordVal = createUUID()
					+ Base64.encodeBase64String(passowrd.getBytes());
			String xaDsClassNameVal = createUUID()
					+ Base64.encodeBase64String(xaDsClassName.getBytes());
			byte[] key = DESUtil.initKey(new String(seed));
			byte[] urlEncryptVal = DESUtil.encrypt(urlVal.getBytes(), key);
			byte[] driverclassEncryptVal = DESUtil.encrypt(
					driverclassVal.getBytes(), key);
			byte[] usernameEncryptVal = DESUtil
					.encrypt(usernameVal.getBytes(), key);
			byte[] passwordEncryptVal = DESUtil
					.encrypt(passwordVal.getBytes(), key);
			byte[] xaDsClassNameEncryptVal = DESUtil.encrypt(
					xaDsClassNameVal.getBytes(), key);
			sb = new StringBuffer();
			sb.append("#-----------------------------"+driverClass+"---------------------------------\n");
			sb.append(encryption(urlChars) + "=" + Base64.encodeBase64String(urlEncryptVal) + "\n");
			sb.append(encryption(driverclassChars) + "=" + Base64.encodeBase64String(driverclassEncryptVal) + "\n");
			sb.append(encryption(usernameChars) + "=" + Base64.encodeBase64String(usernameEncryptVal) + "\n");
			sb.append(encryption(passwordChars) + "=" + Base64.encodeBase64String(passwordEncryptVal) + "\n");
			sb.append(encryption(xaDsClassNameChars) + "=" + Base64.encodeBase64String(xaDsClassNameEncryptVal) + "\n");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
	
	public static String createUUID() {
		return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
	}
	
	public static String encryption(char[] content){
		String result = MD5Util.encodeMD5Hex(new String(content));
		return Base64.encodeBase64String(result.getBytes()).replaceAll(
				new String(new char[] { 0x3d }),
				new String(new char[] { 0x4e }));
	}
	
}
