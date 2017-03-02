package org.apel.hera.biz.domain;

/**
 * mvn settings配置文件中的配置参数
 * 
 * @author lijian
 *
 */
public class SettingsConfigParam  extends TemplateParam{
	
	public static final String JDK_VERSION_16 = "1.6";
	public static final String JDK_VERSION_17 = "1.7";
	public static final String JDK_VERSION_18 = "1.8";
	public static final String JDK_VERSION_19 = "1.9";

	/**
	 * 本地repository存储地址
	 */
	private String repositoryLocation;

	/**
	 * nexus私服用户
	 */
	private String nexusUser;

	/**
	 * nexus登录密码
	 */
	private String nexusPassword;

	/**
	 * nexus snapshot仓库地址
	 */
	private String nexusSnapshotAddress;

	/**
	 * nexus release仓库地址
	 */
	private String nexusReleaseAddress;

	/**
	 * jdk版本
	 */
	private String jdkVersion;

	public String getRepositoryLocation() {
		return repositoryLocation;
	}

	public void setRepositoryLocation(String repositoryLocation) {
		this.repositoryLocation = repositoryLocation;
	}

	public String getNexusUser() {
		return nexusUser;
	}

	public void setNexusUser(String nexusUser) {
		this.nexusUser = nexusUser;
	}

	public String getNexusPassword() {
		return nexusPassword;
	}

	public void setNexusPassword(String nexusPassword) {
		this.nexusPassword = nexusPassword;
	}

	public String getNexusSnapshotAddress() {
		return nexusSnapshotAddress;
	}

	public void setNexusSnapshotAddress(String nexusSnapshotAddress) {
		this.nexusSnapshotAddress = nexusSnapshotAddress;
	}

	public String getNexusReleaseAddress() {
		return nexusReleaseAddress;
	}

	public void setNexusReleaseAddress(String nexusReleaseAddress) {
		this.nexusReleaseAddress = nexusReleaseAddress;
	}

	public String getJdkVersion() {
		return jdkVersion;
	}

	public void setJdkVersion(String jdkVersion) {
		this.jdkVersion = jdkVersion;
	}

}
