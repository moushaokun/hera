package org.apel.hera.biz.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 工程实体
 * @author lijian
 *
 */
@Entity
@Table(name = "robot_project")
public class Project {

	@Id
	private String id;
	
	/**
	 * 工程名(显示)
	 */
	private String projectName;
	
	/**
	 * 工程根包名
	 */
	private String packageName;
	
	
	/**
	 * 工程maven名称
	 */
	private String artifactId;
	
	/**
	 * 工程maven版本
	 */
	private String mvnVersion;
	
	/**
	 * appId
	 */
	private String appId;
	
	/**
	 * casUrl
	 */
	private String casUrl;
	
	/**
	 * zookeeper地址
	 */
	private String zookeeperUrl;
	
	/**
	 * web上下文名称
	 */
	private String contextPath;
	
	
	/**
	 * web端口号
	 */
	private String webPort;
	
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getMvnVersion() {
		return mvnVersion;
	}

	public void setMvnVersion(String mvnVersion) {
		this.mvnVersion = mvnVersion;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCasUrl() {
		return casUrl;
	}

	public void setCasUrl(String casUrl) {
		this.casUrl = casUrl;
	}

	public String getZookeeperUrl() {
		return zookeeperUrl;
	}

	public void setZookeeperUrl(String zookeeperUrl) {
		this.zookeeperUrl = zookeeperUrl;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getWebPort() {
		return webPort;
	}

	public void setWebPort(String webPort) {
		this.webPort = webPort;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
