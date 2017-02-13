package org.apel.hera.biz.domain;

/**
 * mvn settings配置文件中的配置参数
 * 
 * @author lijian
 *
 */
public class SettingsConfigParam {
	
	public static final String REPO_LOCATION = "#repository_location#";
	public static final String NEXUS_USER_NAME = "#nexus_username#";
	public static final String NEXUS_PASSWORD = "#nexus_password#";
	//http://192.168.0.2:81/nexus/content/groups/public
	public static final String NEXUS_SNAPSHOT_ADDRESS = "#nexus_snapshot_address#";
	public static final String NEXUS_RELEASE_ADDRESS = "#nexus_release_address#";
	public static final String JDK_VERSION = "#jdk_version#";
	
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
