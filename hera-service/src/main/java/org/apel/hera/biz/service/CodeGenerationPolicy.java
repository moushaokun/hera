package org.apel.hera.biz.service;

import org.apache.commons.lang3.StringUtils;
import org.apel.hera.biz.consist.FileConsist;
import org.apel.hera.biz.domain.DBParams;
import org.apel.hera.biz.domain.SettingsConfigParam;
import org.apel.hera.biz.util.CodeIOUtil;

/**
 * 枚举抽象策略模式，代码机器人核心
 * @author lijian
 *
 */
public enum CodeGenerationPolicy {
	
	/**
	 *	mvn settins配置文件生成
	 */
	SETTINGS_TEMPLATE(FileConsist.SETTINGS_TPL_NAME) {
		@Override
		public byte[] generateSourceCode(Object templateParam) {
			SettingsConfigParam settingsConfigParam = (SettingsConfigParam)templateParam;
			byte[] bytes = CodeIOUtil.generateSourceBytes(toString(), value -> {
				String nexusUsername = settingsConfigParam.getNexusUser();
				if(StringUtils.isEmpty(nexusUsername)){
					nexusUsername = "admin";
				}
				String nexusPassword = settingsConfigParam.getNexusPassword();
				if(StringUtils.isEmpty(nexusPassword)){
					nexusPassword = "admin123";
				}
				String nexusReleaseAddress = settingsConfigParam.getNexusReleaseAddress();
				if(StringUtils.isEmpty(nexusReleaseAddress)){
					nexusReleaseAddress = "http://192.168.0.2:81/nexus/content/groups/public";
				}
				String nexusSnapshotAddress = settingsConfigParam.getNexusSnapshotAddress();
				if(StringUtils.isEmpty(nexusSnapshotAddress)){
					nexusSnapshotAddress = "http://192.168.0.2:81/nexus/content/groups/public";
				}
				String jdkVersion = settingsConfigParam.getJdkVersion();
				if(StringUtils.isEmpty(jdkVersion)){
					jdkVersion = SettingsConfigParam.JDK_VERSION_18;
				}
				value = value.replaceAll(SettingsConfigParam.REPO_LOCATION, settingsConfigParam.getRepositoryLocation());
				value = value.replaceAll(SettingsConfigParam.NEXUS_USER_NAME, nexusUsername);
				value = value.replaceAll(SettingsConfigParam.NEXUS_PASSWORD, nexusPassword);
				value = value.replaceAll(SettingsConfigParam.NEXUS_RELEASE_ADDRESS, nexusReleaseAddress);
				value = value.replaceAll(SettingsConfigParam.NEXUS_SNAPSHOT_ADDRESS, nexusSnapshotAddress);
				value = value.replaceAll(SettingsConfigParam.JDK_VERSION, jdkVersion);
				return value;
			});
			return bytes;
		}
	},
	/**
	 * spring boot application.properties文件生成
	 */
	BOOT_CONFIG(FileConsist.BOOT_TPL_NAME){
		@Override
		public byte[] generateSourceCode(Object templateParam) {
			return CodeIOUtil.generateSourceBytes(toString(), value -> value);
		}
	},
	/**
	 * db.properties文件生成
	 */
	DB_CONFIG(FileConsist.DB_TPL_NAME){
		@Override
		public byte[] generateSourceCode(Object templateParam) {
			DBParams dbParams = (DBParams)templateParam;
			String connectionConfig = DatabaseConfigGenerator.buildConfig(dbParams.getUrl(), dbParams.getDriverClass(), dbParams.getUsername(), dbParams.getPassword(), dbParams.getXaDsClassName());
			StringBuffer sb = new StringBuffer();
			if(StringUtils.isNotEmpty(dbParams.getInitialSize())){
				sb.append("#\u914d\u7f6e\u521d\u59cb\u5316\u5927\u5c0f\u3001\u6700\u5c0f\u3001\u6700\u5927\n");
				sb.append("initialSize=" + dbParams.getInitialSize() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getMinIdle())){
				sb.append("minIdle=" + dbParams.getMinIdle() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getMaxActive())){
				sb.append("maxActive=" + dbParams.getMaxActive() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getMaxWait())){
				sb.append("#\u914d\u7f6e\u83b7\u53d6\u8fde\u63a5\u7b49\u5f85\u8d85\u65f6\u7684\u65f6\u95f4\n");
				sb.append("maxWait=" + dbParams.getMaxWait() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getTimeBetweenEvictionRunsMillis())){
				sb.append("#\u914d\u7f6e\u95f4\u9694\u591a\u4e45\u624d\u8fdb\u884c\u4e00\u6b21\u68c0\u6d4b\uff0c\u68c0\u6d4b\u9700\u8981\u5173\u95ed\u7684\u7a7a\u95f2\u8fde\u63a5\uff0c\u5355\u4f4d\u662f\u6beb\u79d2\n");
				sb.append("timeBetweenEvictionRunsMillis=" + dbParams.getTimeBetweenEvictionRunsMillis() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getMinEvictableIdleTimeMillis())){
				sb.append("#\u914d\u7f6e\u4e00\u4e2a\u8fde\u63a5\u5728\u6c60\u4e2d\u6700\u5c0f\u751f\u5b58\u7684\u65f6\u95f4\uff0c\u5355\u4f4d\u662f\u6beb\u79d2\n");
				sb.append("minEvictableIdleTimeMillis=" + dbParams.getMinEvictableIdleTimeMillis() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getValidationQuery())){
				sb.append("#\u68c0\u6d4b\n");
				sb.append("validationQuery=" + dbParams.getValidationQuery() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getTestWhileIdle())){
				sb.append("testWhileIdle=" + dbParams.getTestWhileIdle() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getTestOnBorrow())){
				sb.append("testOnBorrow=" + dbParams.getTestOnBorrow() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getTestOnReturn())){
				sb.append("testOnReturn=" + dbParams.getTestOnReturn() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getPoolPreparedStatements())){
				sb.append("#\u6253\u5f00PSCache\uff0c\u5e76\u4e14\u6307\u5b9a\u6bcf\u4e2a\u8fde\u63a5\u4e0aPSCache\u7684\u5927\u5c0f\n");
				sb.append("poolPreparedStatements=" + dbParams.getPoolPreparedStatements() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getMaxPoolPreparedStatementPerConnectionSize())){
				sb.append("maxPoolPreparedStatementPerConnectionSize=" + dbParams.getMaxPoolPreparedStatementPerConnectionSize() + "\n");
			}
			if(StringUtils.isNotEmpty(dbParams.getDialect())){
				sb.append("#\u6570\u636e\u5e93\u65b9\u8a00\n");
				sb.append("dialect=" + dbParams.getDialect() + "\n");
			}
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(DBParams.CONNECTION_CONFIG, connectionConfig);
				value = value.replaceAll(DBParams.DRUID_CONFIG, sb.toString());
				return value;
			});
		}
	};
	
	
	private final String value;
	
	/**
	 * 策略抽象方法
	 * @param templateParam 模板参数
	 */
	public abstract byte[] generateSourceCode(Object templateParam); 
	
	private CodeGenerationPolicy(String value){
		this.value = value;
	}
	
	public String toString(){
		return this.value;
	}
	
	
}
