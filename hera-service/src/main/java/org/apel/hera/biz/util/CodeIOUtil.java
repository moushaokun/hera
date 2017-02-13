package org.apel.hera.biz.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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
	
}
