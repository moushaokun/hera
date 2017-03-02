package org.apel.hera.web;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apel.gaia.commons.i18n.Message;
import org.apel.gaia.commons.i18n.MessageUtil;
import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.util.ExportUtil;
import org.apel.gaia.util.jqgrid.JqGridUtil;
import org.apel.hera.biz.consist.FileConsist;
import org.apel.hera.biz.domain.Project;
import org.apel.hera.biz.service.ProjectService;
import org.apel.poseidon.security.plug.license.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/project")
public class ProjectController {

	private final static String INDEX_URL = "project_index";
	
	@Autowired
	private ProjectService projectService;
	
	//首页
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(){
		return INDEX_URL;
	}
	
	//列表查询
	@RequestMapping
	public @ResponseBody PageBean list(QueryParams queryParams){
		PageBean pageBean = JqGridUtil.getPageBean(queryParams);
		projectService.pageQuery(pageBean);
		return pageBean;
	}
	
	//新增
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Message create(Project project){
		projectService.save(project);
		return MessageUtil.message("project.create.success");
	}
	
	//更新
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody Message create(@PathVariable String id, Project project){
		project.setId(id);
		projectService.update(project);
		return MessageUtil.message("project.update.success");
	}
	
	//查看
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody Project view(@PathVariable String id){
		return projectService.findById(id);
	}
	
	//删除
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Message delete(@PathVariable String id){
		projectService.deleteById(id);
		return MessageUtil.message("project.delete.success");
	}
	
	//批量删除
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody Message batchDelete(@RequestParam("ids[]") String[] ids){
		projectService.deleteById(ids);
		return MessageUtil.message("project.delete.success");
	}
	
	//查询全部数据
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<Project> getAll(){
		return projectService.findAll(new Sort(Direction.DESC, "createDate"));
	}
	
	//license下载
	@RequestMapping(value = "/downloadLicense", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> downloadLicense(String projectId, @DateTimeFormat(pattern = "yyyy-MM-dd") Date expireDate){
		Project project = projectService.findById(projectId);
		License license = new License();
		license.setAppId(project.getAppId());
		license.setName(project.getProjectName());
		if(expireDate == null){
			LocalDate nextMonthLocalDate = LocalDate.now().plus(1, ChronoUnit.MONTHS);
			Date nextMonthDate = Date.from(nextMonthLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			license.setExpireDate(nextMonthDate);
		}else{
			license.setExpireDate(expireDate);
		}
		Schema<License> schema = RuntimeSchema.getSchema(License.class);
		byte[] bytes = ProtostuffIOUtil.toByteArray(license, schema, LinkedBuffer.allocate());
		byte[] newBytes = null;
		try(FileOutputStream fos = new FileOutputStream(new File("d:/v.license"));){
			newBytes = Arrays.copyOf(bytes, bytes.length + 1);
			newBytes[bytes.length] = 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return ExportUtil.getResponseEntityByFile(newBytes, FileConsist.LICENSE_NAME);
	}
	
	//工程脚手架下载
	@RequestMapping(value = "/downloadProjectScaffold", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> downloadProjectScaffold(String projectId){
		return ExportUtil.getResponseEntityByFile(projectService.generateProjectScaffold(projectId), FileConsist.threadExportName.get());
	}
	
	//实体代码下载
	@RequestMapping(value = "/downloadCode", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> downloadCode(String projectId){
		return ExportUtil.getResponseEntityByFile(projectService.downloadCodeZip(projectId), "代码文件.zip");
	}
	
}
