package org.apel.hera.web;

import org.apel.gaia.util.ExportUtil;
import org.apel.hera.biz.consist.SystemConsist;
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
		return ExportUtil.getResponseEntityByFile(bytes, SystemConsist.BOOT_FILE_NAME);
	}
	
	/**
	 * 下载mvn settins文件
	 */
	@RequestMapping(value = "/downloadMvnSettings", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> downloadMvnSettings(SettingsConfigParam settingsConfigParam){
		byte[] bytes = CodeGenerationPolicy.SETTINGS_TEMPLATE.generateSourceCode(settingsConfigParam);
		return ExportUtil.getResponseEntityByFile(bytes, SystemConsist.SETTINGS_FILE_NAME);
	}
	
	/**
	 * 下载db properties文件
	 */
	@RequestMapping(value = "/downloadDBConfig", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> downloadDBConfig(DBParams dbParams){
		byte[] bytes = CodeGenerationPolicy.DB_CONFIG.generateSourceCode(dbParams);
		return ExportUtil.getResponseEntityByFile(bytes, SystemConsist.DB_FILE_NAME);
	}
	
	/**
	 * 下载控制台脚手架压缩
	 */
	@RequestMapping(value = "/downloadConsoleZip", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> downloadConsoleZip(String packageName){
		byte[] bytes = CodeGenerationPolicy.CONSOLE_SKETCH.generateSourceCode(packageName);
		return ExportUtil.getResponseEntityByFile(bytes, SystemConsist.threadExportName.get());
	}
	
	
	
}
