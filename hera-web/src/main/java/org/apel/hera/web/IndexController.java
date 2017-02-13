package org.apel.hera.web;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apel.gaia.util.ExportUtil;
import org.apel.hera.biz.consist.FileConsist;
import org.apel.hera.biz.domain.DBParams;
import org.apel.hera.biz.domain.SettingsConfigParam;
import org.apel.hera.biz.service.CodeGenerationPolicy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class IndexController {
	
	@RequestMapping
	public String index(){
		return "index";
	}
	
	/**
	 * 下载spring boot application.properties文件
	 */
	@RequestMapping(value = "/downloadBootConfig", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> downloadBootConfig(){
		byte[] bytes = CodeGenerationPolicy.BOOT_CONFIG.generateSourceCode(null);
		return ExportUtil.getResponseEntityByFile(bytes, FileConsist.BOOT_FILE_NAME);
	}
	
	/**
	 * 下载mvn settins文件
	 */
	@RequestMapping(value = "/downloadMvnSettings", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> downloadMvnSettings(SettingsConfigParam settingsConfigParam){
		byte[] bytes = CodeGenerationPolicy.SETTINGS_TEMPLATE.generateSourceCode(settingsConfigParam);
		return ExportUtil.getResponseEntityByFile(bytes, FileConsist.SETTINGS_FILE_NAME);
	}
	
	/**
	 * 下载db properties文件
	 */
	@RequestMapping(value = "/downloadDBConfig", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> downloadDBConfig(DBParams dbParams){
		byte[] bytes = CodeGenerationPolicy.DB_CONFIG.generateSourceCode(dbParams);
		return ExportUtil.getResponseEntityByFile(bytes, FileConsist.DB_FILE_NAME);
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		ZipFile zipFile = new ZipFile(new File("D:/1.zip"));
		ZipParameters parameters = new ZipParameters();  
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);  
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		zipFile.addFolder("D:/sys_img", parameters);
	}
}
