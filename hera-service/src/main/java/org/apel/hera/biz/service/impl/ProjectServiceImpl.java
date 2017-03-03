package org.apel.hera.biz.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.gaia.util.UUIDUtil;
import org.apel.hera.biz.consist.SystemConsist;
import org.apel.hera.biz.domain.Domain;
import org.apel.hera.biz.domain.Project;
import org.apel.hera.biz.domain.ProjectParam;
import org.apel.hera.biz.service.CodeGenerationPolicy;
import org.apel.hera.biz.service.DomainService;
import org.apel.hera.biz.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectServiceImpl extends AbstractBizCommonService<Project, String> implements ProjectService{

	@Autowired
	private DomainService domainService;
	
	
	
	@Override
	public void deleteById(String... ids) {
		for (String id : ids) {
			List<Domain> domains = domainService.findByProjectId(id);
			String[] domainIds = domains.stream().map(e -> e.getId()).collect(Collectors.toList()).toArray(new String[]{});
			domainService.deleteById(domainIds);
		}
		super.deleteById(ids);
	}



	@Override
	public byte[] generateProjectScaffold(String projectId) {
		Project project = findById(projectId);
		String tempKey = UUIDUtil.uuid();
		File tempDir = new File(System.getProperty("user.dir") + "/tmp/" + tempKey);
		File exportProjectDir = new File(tempDir.getPath() + "/" + project.getArtifactId());
		String zipPath = System.getProperty("user.dir") + "/tmp/" + tempKey + ".zip";
		if(!exportProjectDir.exists()){
			exportProjectDir.mkdirs();
		}
		byte[] zipBytes = null;
		try {
			FileUtils.copyDirectory(new File(SystemConsist.CODE_TEMPLATE_LOCAL_PATH + "/project"), exportProjectDir);
			ProjectParam projectParam = new ProjectParam();
			projectParam.setProject(project);
			//产生pom.xml
			projectParam.setPath(exportProjectDir.getPath());
			CodeGenerationPolicy.PROJECT_POM.generateSourceCode(projectParam);
			//产生center.properties
			projectParam.setPath(exportProjectDir.getPath() + "/config");
			CodeGenerationPolicy.PROJECT_CENTER_PROPERTIES.generateSourceCode(projectParam);
			//产生application.properties
			CodeGenerationPolicy.PROJECT_APPLICATION_PROPERTIES.generateSourceCode(projectParam);
			//产生spring module-*.xml
			projectParam.setPath(exportProjectDir.getPath() + "/src/main/resources/META-INF/spring");
			if(!StringUtils.isEmpty(project.getAppId())){
				CodeGenerationPolicy.PROJECT_SPRING.generateSourceCode(projectParam);
			}else{
				CodeGenerationPolicy.PROJECT_SIMPLE_SPRING.generateSourceCode(projectParam);
			}
			//产生包文件结果
			String[] packageDir = project.getPackageName().split("\\.");
			String exportJavaDir = exportProjectDir.getPath() + "/src/main/java/";
			for (int i = 0; i < packageDir.length; i++) {
				exportJavaDir += packageDir[i] + "/";
			}
			new File(exportJavaDir).mkdirs();
			//压缩
			ZipFile zipFile = new ZipFile(zipPath);
			ZipParameters parameters = new ZipParameters();  
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);  
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			zipFile.addFolder(tempDir, parameters);//将临时文件进行zip压缩
			try (FileInputStream zipIs = new FileInputStream(zipPath);){
				zipBytes = IOUtils.toByteArray(zipIs);
			}
			FileUtils.deleteQuietly(new File(zipPath));
			FileUtils.deleteQuietly(tempDir);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		SystemConsist.threadExportName.set(tempKey + ".zip");
		return zipBytes;
	}



	@Override
	public byte[] downloadCodeZip(String projectId) {
		String tempKey = UUIDUtil.uuid();
		String exportRootPath = System.getProperty("user.dir") + "/tmp/" + tempKey;
		File tempDir = new File(System.getProperty("user.dir") + "/tmp/" + tempKey);
		if(!tempDir.exists()){
			tempDir.mkdirs();
		}
		String zipPath = System.getProperty("user.dir") + "/tmp/" + tempKey + ".zip";
		List<Domain> domains = domainService.findByProjectId(projectId);
		for (Domain domain : domains) {
			domainService.generateCode(domain.getId(), exportRootPath);
		}
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
