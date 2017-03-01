package org.apel.hera.biz.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apel.gaia.util.UUIDUtil;
import org.apel.hera.biz.consist.FileConsist;
import org.apel.hera.biz.consist.InputTypeConsist;
import org.apel.hera.biz.domain.DBParams;
import org.apel.hera.biz.domain.Domain;
import org.apel.hera.biz.domain.Field;
import org.apel.hera.biz.domain.JavaCoreParam;
import org.apel.hera.biz.domain.ModuleRowColumn;
import org.apel.hera.biz.domain.Project;
import org.apel.hera.biz.domain.ProjectParam;
import org.apel.hera.biz.domain.SettingsConfigParam;
import org.apel.hera.biz.domain.TemplateParam;
import org.apel.hera.biz.util.CodeIOUtil;
import org.apel.hera.biz.util.CodePropertyUtil;
import org.springframework.util.CollectionUtils;

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
				if(StringUtils.isNotEmpty(dbParams.getMaxPoolPreparedStatementPerConnectionSize())){
					sb.append("maxPoolPreparedStatementPerConnectionSize=" + dbParams.getMaxPoolPreparedStatementPerConnectionSize() + "\n");
				}
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
	},
	/**
	 * 控制台脚手架生成
	 */
	CONSOLE_SKETCH(FileConsist.CONSOLE_ZIP_NAME){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			String packageName = (String)templateParam;
			byte[] resultBytes = null;
			String localTemplatePath = System.getProperty("user.dir") + "/code_templates/";
			String localTmpPath = System.getProperty("user.dir") + "/tmp/";
			String tmpKey = UUIDUtil.uuid();
			String tmpDir = localTmpPath + "/" + tmpKey;//在tmp下随机产生的临时文件夹
			String exportZipName = tmpKey + ".zip";
			FileConsist.threadExportName.set(exportZipName);
			File exportZipFile = new File(localTmpPath + exportZipName);
			try {
				//产生Controller文件
				CodeIOUtil.generateSourceFile(FileConsist.CONSOLE_CONTROLLER_TPL_NAME, value -> {
					value = value.replace(TemplateParam.CONSOLE_SCAFFOLD_PACKAGE_NAME, packageName);
					return value;
				}, tmpDir, FileConsist.CONSOLE_CONTROLLER_NAME);
				ZipFile zipFile = new ZipFile(exportZipFile);
				ZipParameters parameters = new ZipParameters();  
				parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);  
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
				FileUtils.copyDirectory(new File(localTemplatePath + toString()), new File(tmpDir));//拷贝模板进入临时文件夹中
				zipFile.addFolder(tmpDir, parameters);//将临时文件进行zip压缩
				try(InputStream is = new FileInputStream(exportZipFile)){
					resultBytes = IOUtils.toByteArray(is);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}finally{
				FileUtils.deleteQuietly(new File(tmpDir));
				FileUtils.deleteQuietly(exportZipFile);
			}
			return resultBytes;
		}
		
	},
	/**
	 * 工程的application properties
	 */
	PROJECT_APPLICATION_PROPERTIES(ProjectParam.PROJECT_APPLICATION_PROPERTIES_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			ProjectParam projectParam = (ProjectParam)templateParam;
			Project project = projectParam.getProject();
			CodeIOUtil.generateSourceFile(toString(), value -> {
				value = value.replaceAll(ProjectParam.WEB_PORT, project.getWebPort());
				value = value.replaceAll(ProjectParam.CONTEXT_PATH, project.getContextPath());
				return value;
			}, projectParam.getPath(), ProjectParam.PROJECT_APPLICATION_PROPERTIES);
			return null;
		}
		
	},
	/**
	 * 工程的center properties
	 */
	PROJECT_CENTER_PROPERTIES(ProjectParam.PROJECT_CENTER_PROPERTIES_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			ProjectParam projectParam = (ProjectParam)templateParam;
			Project project = projectParam.getProject();
			String appId = StringUtils.isEmpty(project.getAppId()) ? "" : project.getAppId();
			String casUrl = StringUtils.isEmpty(project.getCasUrl()) ? "" : project.getCasUrl();
			String zkUrl = StringUtils.isEmpty(project.getZookeeperUrl()) ? "" : project.getZookeeperUrl();
			CodeIOUtil.generateSourceFile(toString(), value -> {
				value = value.replaceAll(ProjectParam.APP_ID, appId);
				value = value.replaceAll(ProjectParam.CAS_URL, casUrl);
				value = value.replaceAll(ProjectParam.ZK_URL, zkUrl);
				return value;
			}, projectParam.getPath(), ProjectParam.PROJECT_CENTER_PROPERTIES);
			return null;
		}
		
	},
	/**
	 * 工程的pom properties
	 */
	PROJECT_POM(ProjectParam.PROJECT_POM_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			ProjectParam projectParam = (ProjectParam)templateParam;
			Project project = projectParam.getProject();
			CodeIOUtil.generateSourceFile(toString(), value -> {
				value = value.replaceAll(ProjectParam.MVN_VERSION, project.getMvnVersion());
				value = value.replaceAll(ProjectParam.ARTIFACT_ID, project.getArtifactId());
				if(!StringUtils.isEmpty(project.getAppId())){
					value = value.replaceAll(ProjectParam.CAS_INTEGRATE, ProjectParam.CAS_INTEGRATE_VALUE);
				}else{
					value = value.replaceAll(ProjectParam.CAS_INTEGRATE, "");
				}
				return value;
			}, projectParam.getPath(), ProjectParam.PROJECT_POM);
			return null;
		}
		
	},
	/**
	 * 工程的spring配置文件(不集成cas)
	 */
	PROJECT_SIMPLE_SPRING(ProjectParam.PROJECT_SIMPLE_SPRING_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			ProjectParam projectParam = (ProjectParam)templateParam;
			Project project = projectParam.getProject();
			String exportName = "module-" + project.getArtifactId() + ".xml";
			CodeIOUtil.generateSourceFile(toString(), value -> {
				value = value.replaceAll(ProjectParam.PACKAGE_NAME, project.getPackageName());
				return value;
			}, projectParam.getPath(), exportName);
			return null;
		}
		
	},
	/**
	 * 工程的spring配置文件(集成cas)
	 */
	PROJECT_SPRING(ProjectParam.PROJECT_SPRING_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			ProjectParam projectParam = (ProjectParam)templateParam;
			Project project = projectParam.getProject();
			String exportName = "module-" + project.getArtifactId() + ".xml";
			CodeIOUtil.generateSourceFile(toString(), value -> {
				value = value.replaceAll(ProjectParam.PACKAGE_NAME, project.getPackageName());
				value = value.replaceAll(ProjectParam.ARTIFACT_ID, project.getArtifactId());
				return value;
			}, projectParam.getPath(), exportName);
			return null;
		}
		
	},
	/**
	 * 实体领域对象java类产生
	 */
	DOMAIN_TEMPLATE(JavaCoreParam.DOMAIN_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			List<Field> fields = javaCoreParam.getFields();
			String[] handledInfo = CodePropertyUtil.handleCodeJavaProperty(fields);
			String fieldsStr = handledInfo[0];
			String fieldMethodsStr = handledInfo[1];
			String importPackagesStr = handledInfo[2];
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(JavaCoreParam.IMPORT_PACKAGE, importPackagesStr);
				value = value.replaceAll(JavaCoreParam.ROOT_PACKAGE, d.getProject().getPackageName());
				value = value.replaceAll(JavaCoreParam.TABLE_NAME, d.getTableName());
				value = value.replaceAll(JavaCoreParam.CLASS_NAME, d.getClassName());
				value = value.replaceAll(JavaCoreParam.FIELDS, fieldsStr);
				value = value.replaceAll(JavaCoreParam.FIELD_METHODS, fieldMethodsStr);
				return value;
			});
		}
		
	},
	/**
	 * java service接口产生
	 */
	SERVICE_TEMPLATE(JavaCoreParam.SERVICE_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(JavaCoreParam.ROOT_PACKAGE, d.getProject().getPackageName());
				value = value.replaceAll(JavaCoreParam.CLASS_NAME, d.getClassName());
				return value;
			});
		}
		
	},
	/**
	 * java service接口实现类产生
	 */
	SERVICE_IMPL_TEMPLATE(JavaCoreParam.SERVICE_IMPL_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(JavaCoreParam.ROOT_PACKAGE, d.getProject().getPackageName());
				value = value.replaceAll(JavaCoreParam.CLASS_NAME, d.getClassName());
				return value;
			});
		}
		
	},
	/**
	 * java dao产生
	 */
	REPOSITORY_TEMPLATE(JavaCoreParam.REPOSITORY_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(JavaCoreParam.ROOT_PACKAGE, d.getProject().getPackageName());
				value = value.replaceAll(JavaCoreParam.CLASS_NAME, d.getClassName());
				return value;
			});
		}
		
	},
	/**
	 * java controller产生
	 */
	CONTROLLER_TEMPLATE(JavaCoreParam.CONTROLLER_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			String className = d.getClassName();
			String domainName = String.valueOf(Character.toLowerCase(className.charAt(0))) + className.substring(1, className.length());
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(JavaCoreParam.ROOT_PACKAGE, d.getProject().getPackageName());
				value = value.replaceAll(JavaCoreParam.CLASS_NAME, className);
				value = value.replaceAll(JavaCoreParam.DOMAIN_NAME, domainName);
				return value;
			});
		}
		
	},
	/**
	 * i18n产生
	 */
	I18N_TEMPLATE(JavaCoreParam.I18N_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			String className = d.getClassName();
			String domainName = String.valueOf(Character.toLowerCase(className.charAt(0))) + className.substring(1, className.length());
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(JavaCoreParam.DOMAIN_NAME, domainName);
				return value;
			});
		}
		
	},
	/**
	 * html文件产生
	 */
	HTML_TEMPLATE(JavaCoreParam.HTML_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			List<Field> fields = javaCoreParam.getFields();
			String className = d.getClassName();
			String domainName = String.valueOf(Character.toLowerCase(className.charAt(0))) + className.substring(1, className.length());
			//搜索框html
			StringBuffer searchFieldsSb = new StringBuffer("");
			for (int i = 0; i < fields.size(); i++) {
				Field field = fields.get(i);
				if(field.getIsSearch() != null && field.getIsSearch()){
					searchFieldsSb.append("\t\t\t\t\t\t\t\t<el-option label=\"" + field.getName() + "\" value=\"" + field.getCodeName() + "\"></el-option>\n");
				}
			}
			String searchFields = searchFieldsSb.toString();
			//表单html
			StringBuffer formFieldStr = new StringBuffer();
			List<ModuleRowColumn> rowCols = d.getRowCols();
			if(!CollectionUtils.isEmpty(rowCols)){
				for (int i = 0; i < rowCols.size(); i++) {
					ModuleRowColumn rowCol = rowCols.get(i);
					StringBuffer rowHtml = new StringBuffer();
					rowHtml.append("\t\t\t\t<el-row>\n");
					Integer colNum = rowCol.getColNum();
					int maxColNum = 24;
					if(colNum == 5){
						maxColNum += 1;
					}
					int span = maxColNum / colNum;
					for (int j = 0; j < colNum; j++) {
						if(colNum == 5 && (j == colNum - 1)){
							span = span - 1;
						}
						Field field = findFieldsByRowAndColIndex(fields, i, j);
						String colHtml = "";
						if(field != null){//根据行列坐标查找字段，如果找到根据字段信息渲染html
							colHtml = "\t\t\t\t\t<el-col :span=\"#span#\">\n\t\t\t\t\t\t<el-form-item label=\"#fieldName#\" prop=\"#fieldCodeName#\">\n#formInput#\t\t\t\t\t\t</el-form-item>\n\t\t\t\t\t</el-col>\n";
							String formInput = "";
							if(field.getInputType().equals(InputTypeConsist.PASSWORD)){
								formInput = "\t\t\t\t\t\t\t<el-input v-model=\"form.#fieldCodeName#\" type=\"password\" size=\"small\" placeholder=\"请输入#fieldName#\"></el-input>\n";
							}else if(field.getInputType().equals(InputTypeConsist.SELECT)){
								formInput = "\t\t\t\t\t\t\t<el-select size=\"small\" v-model=\"form.#fieldCodeName#\" placeholder=\"请选择\">\n\t\t\t\t\t\t\t\t<el-option label=\"选项一\" value=\"选项一\"></el-option>\n\t\t\t\t\t\t\t</el-select>\n";
							}else if(field.getInputType().equals(InputTypeConsist.SWITCH)){
								formInput = "\t\t\t\t\t\t\t<el-switch on-text=\"是\" off-text=\"否\" v-model=\"form.#fieldCodeName#\"></el-switch>\n";
							}else if(field.getInputType().equals(InputTypeConsist.DATE)){
								formInput = "\t\t\t\t\t\t\t<el-date-picker size=\"small\" v-model=\"form.#fieldCodeName#\" type=\"datetime\" placeholder=\"选择日期\"></el-date-picker>\n";
							}else if(field.getInputType().equals(InputTypeConsist.TEXTAREA)){
								formInput = "\t\t\t\t\t\t\t<el-input v-model=\"form.#fieldCodeName#\" type=\"textarea\" size=\"small\" placeholder=\"请输入#fieldName#\"></el-input>\n";
							}else if(field.getInputType().equals(InputTypeConsist.NUMBER)){
								formInput = "\t\t\t\t\t\t\t<el-input-number size=\"small\" v-model=\"form.#fieldCodeName#\" :min=\"0\"></el-input-number>\n";
							}else{
								formInput = "\t\t\t\t\t\t\t<el-input v-model=\"form.#fieldCodeName#\" size=\"small\" placeholder=\"请输入#fieldName#\"></el-input>\n";
							}
							colHtml = colHtml.replaceAll("#formInput#", formInput).replaceAll("#span#", String.valueOf(span))
									.replaceAll("#fieldName#", field.getName()).replaceAll("#fieldCodeName#", field.getCodeName());
						}else{//没有找到添加空
							colHtml = "\t\t\t\t\t<el-col :span=\"" + span + "\"></el-col>\n";
						}
						rowHtml.append(colHtml);
					}
					rowHtml.append("\t\t\t\t</el-row>\n");
					formFieldStr.append(rowHtml);
				}
			}
			
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(JavaCoreParam.MODULE_NAME, d.getDomainName());
				value = value.replaceAll(JavaCoreParam.DOMAIN_NAME, domainName);
				value = value.replaceAll(JavaCoreParam.SEARCH_FIELDS, searchFields);
				value = value.replaceAll(JavaCoreParam.FORM_FIELDS, formFieldStr.toString());
				return value;
			});
		}
		
	};
	
	private static Field findFieldsByRowAndColIndex(List<Field> fields, int rowIndex, int colIndex) {
		Optional<Field> optional = fields.stream().filter(f -> {
			if(Integer.valueOf(f.getMark().split("-")[0]) == rowIndex 
					&& Integer.valueOf(f.getMark().split("-")[1]) == colIndex){
				return true;
			}
			return false;
		}).findFirst();
		if(optional.isPresent()){
			return optional.get();
		}else{
			return null;
		}
	}
	
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
