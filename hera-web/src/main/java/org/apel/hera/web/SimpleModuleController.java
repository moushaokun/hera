package org.apel.hera.web;

import java.util.List;

import org.apel.gaia.commons.i18n.Message;
import org.apel.gaia.commons.i18n.MessageUtil;
import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.util.ExportUtil;
import org.apel.gaia.util.jqgrid.JqGridUtil;
import org.apel.hera.biz.domain.Domain;
import org.apel.hera.biz.domain.ModuleRowColumn;
import org.apel.hera.biz.service.DomainService;
import org.apel.hera.biz.service.ModuleRowColService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/simpleModule")
public class SimpleModuleController {
	
	private final static String INDEX_URL = "simple_module_index";
	
	@Autowired
	private DomainService domainService;
	@Autowired
	private ModuleRowColService moduleRowColService;
	
	//首页
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(){
		return INDEX_URL;
	}
	
	//列表查询
	@RequestMapping
	public @ResponseBody PageBean list(QueryParams queryParams){
		PageBean pageBean = JqGridUtil.getPageBean(queryParams);
		domainService.pageQuery(pageBean);
		return pageBean;
	}
	
	//新增
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Message create(Domain domain){
		domainService.save(domain);
		return MessageUtil.message("domain.create.success");
	}
	
	//更新
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody Message create(@PathVariable String id, Domain domain){
		domain.setId(id);
		domainService.update(domain);
		return MessageUtil.message("project.update.success");
	}
	
	//查看
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody Domain view(@PathVariable String id){
		return domainService.findById(id);
	}
	
	//删除
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Message delete(@PathVariable String id){
		domainService.deleteById(id);
		return MessageUtil.message("domain.delete.success");
	}
	
	//批量删除
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody Message batchDelete(@RequestParam("ids[]") String[] ids){
		domainService.deleteById(ids);
		return MessageUtil.message("domain.delete.success");
	}
	
	//查询全部数据
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<Domain> getAll(){
		return domainService.findAll(new Sort(Direction.DESC, "createDate"));
	}
	
	//新增行列信息
	@RequestMapping(value = "/addRowCol", method = RequestMethod.POST)
	public @ResponseBody Message addRowCol(String moduleId, Integer colNum){
		String rowColId = moduleRowColService.addRowCol(moduleId, colNum);
		return MessageUtil.message("domain.create.success", rowColId);
	}
	
	//删除行列信息
	@RequestMapping(value = "/delRowCol", method = RequestMethod.POST)
	public @ResponseBody Message delRowCol(String rowColId){
		moduleRowColService.deleteById(rowColId);
		return MessageUtil.message("domain.delete.success");
	}
	
	//展现出模块所配的行列信息
	@RequestMapping(value = "/listRowCol")
	public @ResponseBody List<ModuleRowColumn> listRowCol(String moduleId){
		return moduleRowColService.findByModuleId(moduleId);
	}
	
	//实体代码下载
	@RequestMapping(value = "/downloadCode", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<byte[]> downloadProjectScaffold(String domainId){
		return ExportUtil.getResponseEntityByFile(domainService.downloadCodeZip(domainId), "代码文件.zip");
	}
	
	
}
