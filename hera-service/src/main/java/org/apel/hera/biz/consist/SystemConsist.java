package org.apel.hera.biz.consist;

/**
 * 系统常量
 * @author lijian
 *
 */
public interface SystemConsist {

	ThreadLocal<String> threadExportName = new ThreadLocal<>();
	final static String CODE_TEMPLATE_LOCAL_PATH = System.getProperty("user.dir") + "/code_templates";
	final static String LICENSE_NAME = "v.license";
	final static String BOOT_FILE_NAME = "application.properties";
	final static String BOOT_TPL_NAME = "boot-config.template";
	final static String SETTINGS_FILE_NAME = "settings.xml";
	final static String SETTINGS_TPL_NAME = "settings.template";
	final static String DB_FILE_NAME = "db.properties";
	final static String DB_TPL_NAME = "db.template";
	final static String CONSOLE_ZIP_NAME = "console";
	final static String CONSOLE_CONTROLLER_NAME = "UIIndexController.java";
	final static String CONSOLE_CONTROLLER_TPL_NAME = "UIIndexController.template";
	
	public final static String DOMAIN_TEMPLATE = "domain.template";
	public final static String SERVICE_TEMPLATE = "service.template";
	public final static String SERVICE_IMPL_TEMPLATE = "serviceImpl.template";
	public final static String REPOSITORY_TEMPLATE = "dao.template";
	public final static String CONTROLLER_TEMPLATE = "controller.template";
	public final static String I18N_TEMPLATE = "i18n.template";
	public final static String HTML_TEMPLATE = "html.template";
	public final static String JS_TEMPLATE = "js.template";
	
	public final static String PROJECT_CENTER_PROPERTIES = "center.properties";
	public final static String PROJECT_CENTER_PROPERTIES_TEMPLATE = "project/project.center.template";
	public final static String PROJECT_APPLICATION_PROPERTIES = "application.properties";
	public final static String PROJECT_APPLICATION_PROPERTIES_TEMPLATE = "project/project.application.template";
	public final static String PROJECT_POM = "pom.xml";
	public final static String PROJECT_POM_TEMPLATE = "project/project.pom.template";
	public final static String PROJECT_SIMPLE_SPRING_TEMPLATE = "project/project.simple.spring.template";
	public final static String PROJECT_SPRING_TEMPLATE = "project/project.spring.template";
	
	public static String getTabString(int num){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}
	
}
