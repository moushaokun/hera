package org.apel.hera.biz.consist;

/**
 * 模板文件名称常量
 * @author lijian
 *
 */
public interface FileConsist {
	ThreadLocal<String> threadExportName = new ThreadLocal<>();
	final static String BOOT_FILE_NAME = "application.properties";
	final static String BOOT_TPL_NAME = "boot-config.template";
	final static String SETTINGS_FILE_NAME = "settings.xml";
	final static String SETTINGS_TPL_NAME = "settings.template";
	final static String DB_FILE_NAME = "db.properties";
	final static String DB_TPL_NAME = "db.template";
	final static String CONSOLE_ZIP_NAME = "console";
	final static String CONSOLE_CONTROLLER_NAME = "UIIndexController.java";
	final static String CONSOLE_CONTROLLER_TPL_NAME = "UIIndexController.template";
}
