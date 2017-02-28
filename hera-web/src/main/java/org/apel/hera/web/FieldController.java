package org.apel.hera.web;

import java.util.List;

import org.apel.gaia.commons.i18n.Message;
import org.apel.gaia.commons.i18n.MessageUtil;
import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Operation;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.commons.pager.RelateType;
import org.apel.gaia.util.jqgrid.JqGridUtil;
import org.apel.hera.biz.domain.Field;
import org.apel.hera.biz.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/field")
public class FieldController {
	
	@Autowired
	private FieldService fieldService;
	
	//列表查询
	@RequestMapping
	public @ResponseBody PageBean list(QueryParams queryParams){
		PageBean pageBean = JqGridUtil.getPageBean(queryParams);
		fieldService.pageQuery(pageBean);
		return pageBean;
	}
	
	//新增
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Message create(Field field){
		fieldService.save(field);
		return MessageUtil.message("field.create.success", field);
	}
	
	//更新
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody Message create(@PathVariable String id, Field field){
		field.setId(id);
		fieldService.update(field);
		return MessageUtil.message("field.update.success");
	}
	
	//查看
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody Field view(@PathVariable String id){
		return fieldService.findById(id);
	}
	
	//删除
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Message delete(@PathVariable String id){
		fieldService.deleteById(id);
		return MessageUtil.message("field.delete.success");
	}
	
	//批量删除
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody Message batchDelete(@RequestParam("ids[]") String[] ids){
		fieldService.deleteById(ids);
		return MessageUtil.message("field.delete.success");
	}
	
	//查询全部数据
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<Field> getAll(){
		return fieldService.findAll(new Sort(Direction.DESC, "createDate"));
	}
	
	@RequestMapping(value = "/findByMark", method = RequestMethod.GET)
	public @ResponseBody Field findByMark(String mark){
		Condition c = new Condition();
		c.setRelateType(RelateType.AND);
		c.setOperation(Operation.EQ);
		c.setPropertyName("mark");
		c.setPropertyValue(mark);
		List<Field> fields = fieldService.findByCondition(c);
		if(fields.size() > 0){
			return fields.get(0);
		}else{
			return new Field();
		}
	}
	
}
