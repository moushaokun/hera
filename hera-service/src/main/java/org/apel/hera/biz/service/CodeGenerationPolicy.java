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
import org.apel.hera.biz.consist.DataTypeConsist;
import org.apel.hera.biz.consist.InputTypeConsist;
import org.apel.hera.biz.consist.StableCharConsist;
import org.apel.hera.biz.consist.SystemConsist;
import org.apel.hera.biz.domain.DBParams;
import org.apel.hera.biz.domain.Domain;
import org.apel.hera.biz.domain.Field;
import org.apel.hera.biz.domain.FieldValidateRule;
import org.apel.hera.biz.domain.JavaCoreParam;
import org.apel.hera.biz.domain.ModuleRowColumn;
import org.apel.hera.biz.domain.Project;
import org.apel.hera.biz.domain.ProjectParam;
import org.apel.hera.biz.domain.SettingsConfigParam;
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
	SETTINGS_TEMPLATE(SystemConsist.SETTINGS_TPL_NAME) {
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
				value = value.replaceAll(StableCharConsist.REPO_LOCATION, settingsConfigParam.getRepositoryLocation());
				value = value.replaceAll(StableCharConsist.NEXUS_USER_NAME, nexusUsername);
				value = value.replaceAll(StableCharConsist.NEXUS_PASSWORD, nexusPassword);
				value = value.replaceAll(StableCharConsist.NEXUS_RELEASE_ADDRESS, nexusReleaseAddress);
				value = value.replaceAll(StableCharConsist.NEXUS_SNAPSHOT_ADDRESS, nexusSnapshotAddress);
				value = value.replaceAll(StableCharConsist.JDK_VERSION, jdkVersion);
				return value;
			});
			return bytes;
		}
	},
	/**
	 * spring boot application.properties文件生成
	 */
	BOOT_CONFIG(SystemConsist.BOOT_TPL_NAME){
		@Override
		public byte[] generateSourceCode(Object templateParam) {
			return CodeIOUtil.generateSourceBytes(toString(), value -> value);
		}
	},
	/**
	 * db.properties文件生成
	 */
	DB_CONFIG(SystemConsist.DB_TPL_NAME){
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
				value = value.replaceAll(StableCharConsist.CONNECTION_CONFIG, connectionConfig);
				value = value.replaceAll(StableCharConsist.DRUID_CONFIG, sb.toString());
				return value;
			});
		}
	},
	/**
	 * 控制台脚手架生成
	 */
	CONSOLE_SKETCH(SystemConsist.CONSOLE_ZIP_NAME){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			String packageName = (String)templateParam;
			byte[] resultBytes = null;
			String localTemplatePath = System.getProperty("user.dir") + "/code_templates/";
			String localTmpPath = System.getProperty("user.dir") + "/tmp/";
			String tmpKey = UUIDUtil.uuid();
			String tmpDir = localTmpPath + "/" + tmpKey;//在tmp下随机产生的临时文件夹
			String exportZipName = tmpKey + ".zip";
			SystemConsist.threadExportName.set(exportZipName);
			File exportZipFile = new File(localTmpPath + exportZipName);
			try {
				//产生Controller文件
				CodeIOUtil.generateSourceFile(SystemConsist.CONSOLE_CONTROLLER_TPL_NAME, value -> {
					value = value.replace(StableCharConsist.PACKAGE_NAME, packageName);
					return value;
				}, tmpDir, SystemConsist.CONSOLE_CONTROLLER_NAME);
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
	PROJECT_APPLICATION_PROPERTIES(SystemConsist.PROJECT_APPLICATION_PROPERTIES_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			ProjectParam projectParam = (ProjectParam)templateParam;
			Project project = projectParam.getProject();
			String appId = StringUtils.isEmpty(project.getAppId()) ? "" : project.getAppId();
			String casUrl = StringUtils.isEmpty(project.getCasUrl()) ? "" : project.getCasUrl();
			String zkUrl = StringUtils.isEmpty(project.getZookeeperUrl()) ? "" : project.getZookeeperUrl();
			CodeIOUtil.generateSourceFile(toString(), value -> {
				value = value.replaceAll(StableCharConsist.WEB_PORT, project.getWebPort());
				value = value.replaceAll(StableCharConsist.CONTEXT_PATH, "/" + project.getContextPath());
				value = value.replaceAll(StableCharConsist.APP_ID, appId);
				value = value.replaceAll(StableCharConsist.CAS_URL, casUrl);
				value = value.replaceAll(StableCharConsist.ZK_URL, zkUrl);
				return value;
			}, projectParam.getPath(), SystemConsist.PROJECT_APPLICATION_PROPERTIES);
			return null;
		}
		
	},
	/**
	 * 工程的pom properties
	 */
	PROJECT_POM(SystemConsist.PROJECT_POM_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			ProjectParam projectParam = (ProjectParam)templateParam;
			Project project = projectParam.getProject();
			CodeIOUtil.generateSourceFile(toString(), value -> {
				value = value.replaceAll(StableCharConsist.MVN_VERSION, project.getMvnVersion());
				value = value.replaceAll(StableCharConsist.ARTIFACT_ID, project.getArtifactId());
				if(!StringUtils.isEmpty(project.getAppId())){
					value = value.replaceAll(StableCharConsist.CAS_INTEGRATE, StableCharConsist.CAS_INTEGRATE_VALUE);
				}else{
					value = value.replaceAll(StableCharConsist.CAS_INTEGRATE, "");
				}
				return value;
			}, projectParam.getPath(), SystemConsist.PROJECT_POM);
			return null;
		}
		
	},
	/**
	 * 工程的spring配置文件(不集成cas)
	 */
	PROJECT_SIMPLE_SPRING(SystemConsist.PROJECT_SIMPLE_SPRING_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			ProjectParam projectParam = (ProjectParam)templateParam;
			Project project = projectParam.getProject();
			String exportName = "module-" + project.getArtifactId() + ".xml";
			CodeIOUtil.generateSourceFile(toString(), value -> {
				value = value.replaceAll(StableCharConsist.PACKAGE_NAME, project.getPackageName());
				return value;
			}, projectParam.getPath(), exportName);
			return null;
		}
		
	},
	/**
	 * 工程的spring配置文件(集成cas)
	 */
	PROJECT_SPRING(SystemConsist.PROJECT_SPRING_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			ProjectParam projectParam = (ProjectParam)templateParam;
			Project project = projectParam.getProject();
			String exportName = "module-" + project.getArtifactId() + ".xml";
			CodeIOUtil.generateSourceFile(toString(), value -> {
				value = value.replaceAll(StableCharConsist.PACKAGE_NAME, project.getPackageName());
				value = value.replaceAll(StableCharConsist.ARTIFACT_ID, project.getArtifactId());
				return value;
			}, projectParam.getPath(), exportName);
			return null;
		}
		
	},
	/**
	 * 实体领域对象java类产生
	 */
	DOMAIN_TEMPLATE(SystemConsist.DOMAIN_TEMPLATE){

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
				value = value.replaceAll(StableCharConsist.IMPORT_PACKAGE, importPackagesStr);
				value = value.replaceAll(StableCharConsist.ROOT_PACKAGE, d.getProject().getPackageName());
				value = value.replaceAll(StableCharConsist.TABLE_NAME, d.getTableName());
				value = value.replaceAll(StableCharConsist.CLASS_NAME, d.getClassName());
				value = value.replaceAll(StableCharConsist.FIELDS, fieldsStr);
				value = value.replaceAll(StableCharConsist.FIELD_METHODS, fieldMethodsStr);
				return value;
			});
		}
		
	},
	/**
	 * java service接口产生
	 */
	SERVICE_TEMPLATE(SystemConsist.SERVICE_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(StableCharConsist.ROOT_PACKAGE, d.getProject().getPackageName());
				value = value.replaceAll(StableCharConsist.CLASS_NAME, d.getClassName());
				return value;
			});
		}
		
	},
	/**
	 * java service接口实现类产生
	 */
	SERVICE_IMPL_TEMPLATE(SystemConsist.SERVICE_IMPL_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(StableCharConsist.ROOT_PACKAGE, d.getProject().getPackageName());
				value = value.replaceAll(StableCharConsist.CLASS_NAME, d.getClassName());
				return value;
			});
		}
		
	},
	/**
	 * java dao产生
	 */
	REPOSITORY_TEMPLATE(SystemConsist.REPOSITORY_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(StableCharConsist.ROOT_PACKAGE, d.getProject().getPackageName());
				value = value.replaceAll(StableCharConsist.CLASS_NAME, d.getClassName());
				return value;
			});
		}
		
	},
	/**
	 * java controller产生
	 */
	CONTROLLER_TEMPLATE(SystemConsist.CONTROLLER_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			String className = d.getClassName();
			String domainName = String.valueOf(Character.toLowerCase(className.charAt(0))) + className.substring(1, className.length());
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(StableCharConsist.ROOT_PACKAGE, d.getProject().getPackageName());
				value = value.replaceAll(StableCharConsist.CLASS_NAME, className);
				value = value.replaceAll(StableCharConsist.DOMAIN_NAME, domainName);
				return value;
			});
		}
		
	},
	/**
	 * i18n产生
	 */
	I18N_TEMPLATE(SystemConsist.I18N_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			String className = d.getClassName();
			String domainName = String.valueOf(Character.toLowerCase(className.charAt(0))) + className.substring(1, className.length());
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(StableCharConsist.DOMAIN_NAME, domainName);
				return value;
			});
		}
		
	},
	/**
	 * html文件产生
	 */
	HTML_TEMPLATE(SystemConsist.HTML_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			List<Field> fields = javaCoreParam.getFields();
			//搜索框html字符串生成
			StringBuffer searchFieldsSb = new StringBuffer("");
			for (int i = 0; i < fields.size(); i++) {
				Field field = fields.get(i);
				if(field.getIsSearch() != null && field.getIsSearch()){
					searchFieldsSb.append(StableCharConsist.HTML_SEARCH_OPTION_TEMPLATE
							.replaceAll(StableCharConsist.PLACEHOLDER_FIELDNAME, field.getName())
							.replaceAll(StableCharConsist.PLACEHOLDER_FIELDCODENAME, field.getCodeName()));
				}
			}
			String searchFields = searchFieldsSb.toString();
			//表单html字符串生成
			StringBuffer formFieldStr = new StringBuffer();
			List<ModuleRowColumn> rowCols = d.getRowCols();
			if(!CollectionUtils.isEmpty(rowCols)){
				for (int i = 0; i < rowCols.size(); i++) {
					ModuleRowColumn rowCol = rowCols.get(i);
					StringBuffer rowHtml = new StringBuffer();
					rowHtml.append(StableCharConsist.HTML_ROW_START_TEMPLATE);
					Integer colNum = rowCol.getColNum();
					int maxColNum = 24;
					if(colNum == 5){
						maxColNum += 1;
					}
					//计算span
					int span = maxColNum / colNum;
					for (int j = 0; j < colNum; j++) {
						if(colNum == 5 && (j == colNum - 1)){
							span = span - 1;
						}
						//根据行列坐标查找字段
						Field field = findFieldsByRowAndColIndex(fields, i, j);
						String colHtml = "";
						if(field != null){//根据行列坐标查找字段，如果找到根据字段信息渲染html
							colHtml = StableCharConsist.html_COL_TEMPLATE;
							//根据字段的input类型生成对应的html标签
							String formInput = InputTypeConsist.get(field.getInputType()).createFormInput();
							colHtml = colHtml.replaceAll(StableCharConsist.PLACEHOLDER_FORMINPUT, formInput).replaceAll(StableCharConsist.PLACEHOLDER_SPAN, String.valueOf(span))
									.replaceAll(StableCharConsist.PLACEHOLDER_FIELDNAME, field.getName()).replaceAll(StableCharConsist.PLACEHOLDER_FIELDCODENAME, field.getCodeName());
						}else{//没有找到添加空列
							colHtml = StableCharConsist.HTML_EMPTY_COL_TEMPLATE;
							colHtml = colHtml.replaceAll(StableCharConsist.PLACEHOLDER_SPAN, String.valueOf(span));
						}
						rowHtml.append(colHtml);
					}
					rowHtml.append(StableCharConsist.HTML_ROW_END_TEMPLATE);
					formFieldStr.append(rowHtml);
				}
			}
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(StableCharConsist.MODULE_NAME, d.getDomainName());
				value = value.replaceAll(StableCharConsist.DOMAIN_NAME, d.getDomainCodeName());
				value = value.replaceAll(StableCharConsist.SEARCH_FIELDS, searchFields);
				value = value.replaceAll(StableCharConsist.FORM_FIELDS, formFieldStr.toString());
				return value;
			});
		}
	},
	/**
	 * js文件产生
	 */
	JS_TEMPLATE(SystemConsist.JS_TEMPLATE){

		@Override
		public byte[] generateSourceCode(Object templateParam) {
			JavaCoreParam javaCoreParam = (JavaCoreParam)templateParam;
			Domain d = javaCoreParam.getDomain();
			List<Field> fields = javaCoreParam.getFields();
			StringBuffer formFieldsJsonSb = new StringBuffer();
			formFieldsJsonSb.append("{");
			StringBuffer fieldRulesSb = new StringBuffer();
			StringBuffer dateFieldToDateSb = new StringBuffer();
			StringBuffer dateFieldsToStrSb = new StringBuffer();
			StringBuffer jqGridColNamesSb = new StringBuffer();
			StringBuffer jqGridColModelSb = new StringBuffer();
			jqGridColNamesSb.append(StableCharConsist.JQGRID_COL_NAMES_START);
			jqGridColModelSb.append(StableCharConsist.JQGRID_COL_MODEL_START);
			for (int i = 0; i < fields.size(); i++) {
				Field field = fields.get(i);
				
				jqGridColNamesSb.append("'" + field.getName() + "',");
				
				String colModelTemplate = "";
				if(field.getDataType().equals(DataTypeConsist.DATE.toString())){
					colModelTemplate = StableCharConsist.JQGRID_COL_MODEL_DATE;
				}else if(field.getDataType().equals(DataTypeConsist.BOOLEAN.toString())){
					colModelTemplate = StableCharConsist.JQGRID_COL_MODEL_BOOLEAN;
				}else{
					colModelTemplate = StableCharConsist.JQGRID_COL_MODEL_COMMON;
				}
				jqGridColModelSb.append(colModelTemplate.replaceAll(StableCharConsist.PLACEHOLDER_FIELDCODENAME, field.getCodeName()));
				
				
				if(field.getDataType().equals(DataTypeConsist.DATE.toString())){
					dateFieldToDateSb.append(StableCharConsist.DATE_FIELDS_TO_DATE_TEMPLATE.replaceAll(StableCharConsist.PLACEHOLDER_FIELDNAME, field.getCodeName()));
					dateFieldsToStrSb.append(StableCharConsist.DATE_FIELDS_TO_STR_TEMPLATE.replaceAll(StableCharConsist.PLACEHOLDER_FIELDNAME, field.getCodeName()));
				}
				
				if(!field.getCodeName().equals("createDate")){
					formFieldsJsonSb.append(field.getCodeName() + ":");
					if(field.getDataType().equals(DataTypeConsist.BOOLEAN.toString())){
						formFieldsJsonSb.append("false,");
					}else{
						formFieldsJsonSb.append("null,");
					}
				}
				
				fieldRulesSb.append(SystemConsist.getTabString(5) + field.getCodeName() + ":[\n");
				List<FieldValidateRule> validateRules = field.getValidateRules();
				for (int j = 0; j < validateRules.size(); j++) {
					FieldValidateRule r = validateRules.get(j);
					if(r.getValidateType().equals("required")){
						fieldRulesSb.append(SystemConsist.getTabString(6) + "{ required: '" + r.getvValue() + "', message: '" + r.getErrorMsg() + "', trigger: '" + r.getValidateTrigger() + "' },\n");
					}else if(r.getValidateType().equals("range")){
						String min = "";
						String max = "";
						if(StringUtils.isNotEmpty(r.getvValue())){
							min = r.getvValue().split(",")[0];
							if( r.getvValue().split(",").length > 1){
								max = r.getvValue().split(",")[1];
							}
						}
						if(!StringUtils.isEmpty(min)){
							min = "min:" + min + ",";
						}
						if(!StringUtils.isEmpty(max)){
							max = "max:" + max + ",";
						}
						String rangeValidateType = "string";
						if(field.getDataType().equals(DataTypeConsist.INTEGER.toString()))
							rangeValidateType = "number";
						String t = SystemConsist.getTabString(6) + "{ #min##max# type:'" + rangeValidateType + "',message: '" + r.getErrorMsg() + "', trigger: '" + r.getValidateTrigger() + "' },\n";
						fieldRulesSb.append(t.replaceAll("#min#", min).replaceAll("#max#", max));
					}else if(r.getValidateType().equals("length")){
						fieldRulesSb.append(SystemConsist.getTabString(6) + "{ len:" + r.getvValue() + ", message: '" + r.getErrorMsg() + "', trigger: '" + r.getValidateTrigger() + "' },\n");
					}else{
						fieldRulesSb.append(SystemConsist.getTabString(6) + "{ type: '" + r.getValidateType() + "', message: '" + r.getErrorMsg() + "', trigger: '" + r.getValidateTrigger() + "' },\n");
					}
					if(j == validateRules.size() - 1){
						fieldRulesSb = new StringBuffer(fieldRulesSb.substring(0, fieldRulesSb.length() - 2) + "\n");
					}
				}
				fieldRulesSb.append(SystemConsist.getTabString(5) + "],\n");
			}
			jqGridColModelSb.append(StableCharConsist.JQGRID_COL_MODEL_END);
			jqGridColNamesSb.append(StableCharConsist.JQGRID_COL_NAMES_END);
			String dateFieldToDate = dateFieldToDateSb.toString();
			String dateFieldToStr = dateFieldsToStrSb.toString();
			String jqgridColNames = jqGridColNamesSb.toString();
			String jqGridColModel= jqGridColModelSb.toString();
			String formFieldsJson = formFieldsJsonSb.toString().equals("{") ? "{}" : formFieldsJsonSb.substring(0, formFieldsJsonSb.length() - 1) + "}";
			String fieldRules = StringUtils.isEmpty(fieldRulesSb.toString()) ? "" : fieldRulesSb.substring(0, fieldRulesSb.length() - 2).toString() + "\n";
			return CodeIOUtil.generateSourceBytes(toString(), value -> {
				value = value.replaceAll(StableCharConsist.DOMAIN_NAME, d.getDomainCodeName());
				value = value.replaceAll(StableCharConsist.TABLE_NAME, d.getTableName());
				value = value.replaceAll(StableCharConsist.FORM_FIELDS_JSON, formFieldsJson);
				value = value.replaceAll(StableCharConsist.VALIDATE_RULES, fieldRules);
				value = value.replaceAll(StableCharConsist.DATE_FIELDS_TO_DATE, dateFieldToDate);
				value = value.replaceAll(StableCharConsist.DATE_FIELDS_TO_STR, dateFieldToStr);
				value = value.replaceAll(StableCharConsist.JQGRID_COL_NAMES, jqgridColNames);
				value = value.replaceAll(StableCharConsist.JQGRID_COL_MODEL, jqGridColModel);
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
