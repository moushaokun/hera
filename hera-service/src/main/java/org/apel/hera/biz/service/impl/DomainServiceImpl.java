package org.apel.hera.biz.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Operation;
import org.apel.gaia.commons.pager.RelateType;
import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.gaia.util.UUIDUtil;
import org.apel.hera.biz.dao.DomainRepository;
import org.apel.hera.biz.domain.Domain;
import org.apel.hera.biz.domain.Field;
import org.apel.hera.biz.domain.FieldValidateRule;
import org.apel.hera.biz.domain.JavaCoreParam;
import org.apel.hera.biz.domain.ModuleRowColumn;
import org.apel.hera.biz.domain.Project;
import org.apel.hera.biz.service.CodeGenerationPolicy;
import org.apel.hera.biz.service.DomainService;
import org.apel.hera.biz.service.FieldService;
import org.apel.hera.biz.service.FieldValidateRuleService;
import org.apel.hera.biz.service.ModuleRowColService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DomainServiceImpl extends AbstractBizCommonService<Domain, String> implements DomainService{

	@Autowired
	private FieldValidateRuleService fieldValidateRuleService;
	@Autowired
	private FieldService fieldService;
	@Autowired
	private ModuleRowColService moduleRowColService;
	
	@Override
	protected String getPageQl() {
		return "select d.id,d.domainName,d.className,d.tableName,d.createDate as createDate,p.id as projectId,p.projectName from Domain d left join d.project p where 1=1";
	}

	@Override
	public void deleteById(String... ids) {
		for (String moduleId : ids) {
			Condition c = new Condition();
			c.setPropertyName("moduleId");
			c.setPropertyValue(moduleId);
			c.setRelateType(RelateType.AND);
			c.setOperation(Operation.EQ);
			List<Field> fields = fieldService.findByCondition(c);
			List<ModuleRowColumn> rowCols = moduleRowColService.findByCondition(c);
			String[] fieldIds = fields.stream().map(e -> e.getId()).collect(Collectors.toList()).toArray(new String[]{});
			String[] rowColIds = rowCols.stream().map(e -> e.getId()).collect(Collectors.toList()).toArray(new String[]{});
			moduleRowColService.deleteById(rowColIds);
			fieldService.deleteById(fieldIds);
		}
		super.deleteById(ids);
	}

	@Override
	public List<Domain> findByProjectId(String projectId) {
		return ((DomainRepository)getRepository()).findByProjectId(projectId);	
	}

	@Override
	public void generateCode(String domainId, String rootPath) {
		Domain domain = findById(domainId);
		Project project = domain.getProject();
		Condition c = new Condition();
		c.setPropertyName("moduleId");
		c.setPropertyValue(domainId);
		c.setRelateType(RelateType.AND);
		c.setOperation(Operation.EQ);
		List<ModuleRowColumn> rowCols = moduleRowColService.findByModuleId(domainId);
		domain.setRowCols(rowCols);
		List<Field> fields = fieldService.findByCondition(c);
		for (Field field : fields) {
			Condition c1 = new Condition();
			c1.setPropertyName("fieldId");
			c1.setPropertyValue(field.getId());
			c1.setRelateType(RelateType.AND);
			c1.setOperation(Operation.EQ);
			List<FieldValidateRule> validateRules = fieldValidateRuleService.findByCondition(c1);
			field.setValidateRules(validateRules);
		}
		JavaCoreParam param = new JavaCoreParam();
		param.setDomain(domain);
		param.setFields(fields);
		String rootPackageName = project.getPackageName();
		
		String jsFileName = domain.getDomainCodeName() + "_index.js";
		String htmlFileName = domain.getDomainCodeName() + "_index.html";
		String i18nFileName = domain.getDomainCodeName() + "_zh_CN.properties";
		String domainFileName = domain.getClassName() + ".java";
		String serviceFileName = domain.getClassName() + "Service.java";
		String serviceImplFileName = domain.getClassName() + "ServiceImpl.java";
		String repositoryFileName = domain.getClassName() + "Repository.java";
		String controllerFileName = domain.getClassName() + "Controller.java";
		rootPackageName = rootPackageName.replaceAll("\\.", "/");
		String javaRootPath = rootPath + "/" + rootPackageName;
		File rootPackageDir = new File(javaRootPath);
		if(!rootPackageDir.exists())
			rootPackageDir.mkdirs();
		File jsDir = new File(rootPath + "/platform/js");
		File htmlDir = new File(rootPath + "/platform/templates");
		File i18nDir = new File(rootPath + "/i18n");
		File domainDir = new File(javaRootPath + "/domain");
		File serviceDir = new File(javaRootPath + "/service");
		File serviceImplDir = new File(javaRootPath + "/service/impl");
		File daoDir = new File(javaRootPath + "/dao");
		File controllerDir = new File(javaRootPath + "/controller");

		File jsFile = new File(rootPath + "/platform/js/" + jsFileName);
		File htmlFile = new File(rootPath + "/platform/templates/" + htmlFileName);
		File i18nFile = new File(rootPath + "/i18n/" + i18nFileName);
		File domainFile = new File(javaRootPath + "/domain/" + domainFileName);
		File serviceFile = new File(javaRootPath + "/service/" + serviceFileName);
		File serviceImplFile = new File(javaRootPath + "/service/impl/" + serviceImplFileName);
		File daoFile = new File(javaRootPath + "/dao/" + repositoryFileName);
		File controllerFile = new File(javaRootPath + "/controller/" + controllerFileName);
		if(!jsDir.exists())
			jsDir.mkdirs();
		if(!htmlDir.exists())
			htmlDir.mkdirs();
		if(!i18nDir.exists())
			i18nDir.mkdirs();
		if(!domainDir.exists())
			domainDir.mkdirs();
		if(!serviceDir.exists())
			serviceDir.mkdirs();
		if(!serviceImplDir.exists())
			serviceImplDir.mkdirs();
		if(!daoDir.exists())
			daoDir.mkdirs();
		if(!controllerDir.exists())
			controllerDir.mkdirs();
		byte[] i18nBytes = CodeGenerationPolicy.I18N_TEMPLATE.generateSourceCode(param);
		byte[] jsBytes = CodeGenerationPolicy.JS_TEMPLATE.generateSourceCode(param);
		byte[] htmlBytes = CodeGenerationPolicy.HTML_TEMPLATE.generateSourceCode(param);
		byte[] domainBytes = CodeGenerationPolicy.DOMAIN_TEMPLATE.generateSourceCode(param);
		byte[] serviceBytes = CodeGenerationPolicy.SERVICE_TEMPLATE.generateSourceCode(param);
		byte[] serviceImplBytes = CodeGenerationPolicy.SERVICE_IMPL_TEMPLATE.generateSourceCode(param);
		byte[] repositoryBytes = CodeGenerationPolicy.REPOSITORY_TEMPLATE.generateSourceCode(param);
		byte[] controllerBytes = CodeGenerationPolicy.CONTROLLER_TEMPLATE.generateSourceCode(param);
		try(FileOutputStream i18nOs = new FileOutputStream(i18nFile);
			FileOutputStream jsOs = new FileOutputStream(jsFile);
			FileOutputStream htmlOs = new FileOutputStream(htmlFile);
			FileOutputStream domainOs = new FileOutputStream(domainFile);
			FileOutputStream serviceOs = new FileOutputStream(serviceFile);
			FileOutputStream serviceImplOs = new FileOutputStream(serviceImplFile);
			FileOutputStream repositoryOs = new FileOutputStream(daoFile);
			FileOutputStream controllerOs = new FileOutputStream(controllerFile);){
			IOUtils.write(new String(i18nBytes), i18nOs, "UTF-8");
			IOUtils.write(new String(jsBytes), jsOs, "UTF-8");
			IOUtils.write(new String(htmlBytes), htmlOs, "UTF-8");
			IOUtils.write(new String(domainBytes), domainOs, "UTF-8");
			IOUtils.write(new String(serviceBytes), serviceOs, "UTF-8");
			IOUtils.write(new String(serviceImplBytes), serviceImplOs, "UTF-8");
			IOUtils.write(new String(repositoryBytes), repositoryOs, "UTF-8");
			IOUtils.write(new String(controllerBytes), controllerOs, "UTF-8");
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public byte[] downloadCodeZip(String domainId) {
		String tempKey = UUIDUtil.uuid();
		String exportRootPath = System.getProperty("user.dir") + "/tmp/" + tempKey;
		File tempDir = new File(System.getProperty("user.dir") + "/tmp/" + tempKey);
		String zipPath = System.getProperty("user.dir") + "/tmp/" + tempKey + ".zip";
		generateCode(domainId, exportRootPath);
		//压缩
		byte[] zipBytes = null;
		try {
			ZipFile zipFile = new ZipFile(zipPath);
			ZipParameters parameters = new ZipParameters();  
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);  
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			zipFile.addFolder(tempDir, parameters);//将临时文件进行zip压缩
			try (FileInputStream zipIs = new FileInputStream(zipPath);){
				zipBytes = IOUtils.toByteArray(zipIs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		FileUtils.deleteQuietly(new File(zipPath));
		FileUtils.deleteQuietly(tempDir);
		return zipBytes;
	}
	
	
}
