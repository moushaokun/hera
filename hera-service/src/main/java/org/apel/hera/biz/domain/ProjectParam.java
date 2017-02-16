package org.apel.hera.biz.domain;

public class ProjectParam {

	public final static String PROJECT_CENTER_PROPERTIES = "center.properties";
	public final static String PROJECT_CENTER_PROPERTIES_TEMPLATE = "project/project.center.template";
	public final static String PROJECT_APPLICATION_PROPERTIES = "application.properties";
	public final static String PROJECT_APPLICATION_PROPERTIES_TEMPLATE = "project/project.application.template";
	public final static String PROJECT_POM = "pom.xml";
	public final static String PROJECT_POM_TEMPLATE = "project/project.pom.template";
	public final static String PROJECT_SIMPLE_SPRING_TEMPLATE = "project/project.simple.spring.template";
	public final static String PROJECT_SPRING_TEMPLATE = "project/project.spring.template";
	
	public static final String WEB_PORT = "#web_port#";
	public static final String CONTEXT_PATH = "#context_path#";
	public static final String APP_ID = "#appId#";
	public static final String CAS_URL = "#cas_url#";
	public static final String ZK_URL = "#zk_url#";
	public static final String ARTIFACT_ID = "#artifactId#";
	public static final String PACKAGE_NAME = "#packageName#";
	public static final String MVN_VERSION = "#mvnVersion#";
	public static final String CAS_INTEGRATE = "#cas_integrate#";
	public static final String CAS_INTEGRATE_VALUE = "<dependency>\n\t\t\t<groupId>org.apel</groupId>\n\t\t\t<artifactId>poseidon-security-plug</artifactId>\n\t\t\t<version>1.0.2-SNAPSHOT</version>\n\t\t</dependency>\n";
	
	private Project project;

	private String path;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
