package org.apel.hera.biz.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;
import org.apel.hera.biz.service.LineValueCall;
import org.springframework.core.io.ClassPathResource;

/**
 * 源码IO工具类
 * @author lijian
 *
 */
public class CodeIOUtil {

	/**
	 * 根据模板文件产生源代码文件
	 * @param templateName 存储在resources/code_templates下面的模板文件名称 
	 * @param lineValueCall 回调处理
	 * @return 最终的源码文件bytes
	 */
	public static byte[] generateSourceBytes(String templateName, LineValueCall lineValueCall){
		ClassPathResource classPathResource = new ClassPathResource("code_templates/" + templateName);
		try (InputStream is = classPathResource.getInputStream();
				InputStreamReader reader = new InputStreamReader(is, "UTF-8");
				BufferedReader bReader = new BufferedReader(reader);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(bos, "UTF-8");
				BufferedWriter bWriter = new BufferedWriter(osw);){
			for (String line = bReader.readLine(); line != null; line = bReader.readLine()) {
				line = lineValueCall.exec(line);
			    bWriter.write(line);
			    bWriter.newLine();
			}
			bWriter.flush();
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据模板文件产生源代码文件(生成到磁盘中)
	 * @param templateName 存储在resources/code_templates下面的模板文件名称 
	 * @param lineValueCall 回调处理
	 * @param path 磁盘存储目录
	 * @param fileName 存储文件名称
	 */
	public static void generateSourceFile(String templateName, LineValueCall lineValueCall, String path, String fileName){
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		String location = "";
		if(path.charAt(path.length() - 1) == '/'){
			location = path + fileName;
		}else{
			location = path + "/" + fileName;
		}
		ClassPathResource classPathResource = new ClassPathResource("code_templates/" + templateName);
		try (InputStream is = classPathResource.getInputStream();
				InputStreamReader reader = new InputStreamReader(is, "UTF-8");
				BufferedReader bReader = new BufferedReader(reader);
				FileOutputStream bos = new FileOutputStream(location);
				OutputStreamWriter osw = new OutputStreamWriter(bos, "UTF-8");
				BufferedWriter bWriter = new BufferedWriter(osw);){
			for (String line = bReader.readLine(); line != null; line = bReader.readLine()) {
				line = lineValueCall.exec(line);
			    bWriter.write(line);
			    bWriter.newLine();
			}
			bWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 直接获取模板二进制文件
	 * @param templateName 模板名称
	 * @return
	 */
	public static byte[] getTemplateBytes(String templateName){
		ClassPathResource classPathResource = new ClassPathResource("code_templates/" + templateName);
		try (InputStream is = classPathResource.getInputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();){
			IOUtils.copy(is, bos);
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
}
