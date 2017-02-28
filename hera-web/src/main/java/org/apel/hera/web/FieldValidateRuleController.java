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
import org.apel.hera.biz.domain.FieldValidateRule;
import org.apel.hera.biz.service.FieldValidateRuleService;
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
@RequestMapping("/fieldValidateRule")
public class FieldValidateRuleController {
	
	@Autowired
	private FieldValidateRuleService fieldValidateRuleService;
	
	//列表查询
	@RequestMapping
	public @ResponseBody PageBean list(QueryParams queryParams){
		PageBean pageBean = JqGridUtil.getPageBean(queryParams);
		fieldValidateRuleService.pageQuery(pageBean);
		return pageBean;
	}
	
	//新增
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Message create(FieldValidateRule fieldValidateRule){
		fieldValidateRuleService.save(fieldValidateRule);
		return MessageUtil.message("fieldValidateRule.create.success", fieldValidateRule);
	}
	
	//更新
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody Message create(@PathVariable String id, FieldValidateRule fieldValidateRule){
		fieldValidateRule.setId(id);
		fieldValidateRuleService.update(fieldValidateRule);
		return MessageUtil.message("fieldValidateRule.update.success");
	}
	
	//查看
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody FieldValidateRule view(@PathVariable String id){
		return fieldValidateRuleService.findById(id);
	}
	
	//删除
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Message delete(@PathVariable String id){
		fieldValidateRuleService.deleteById(id);
		return MessageUtil.message("fieldValidateRule.delete.success");
	}
	
	//批量删除
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody Message batchDelete(@RequestParam("ids[]") String[] ids){
		fieldValidateRuleService.deleteById(ids);
		return MessageUtil.message("fieldValidateRule.delete.success");
	}
	
	//查询全部数据
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<FieldValidateRule> getAll(){
		return fieldValidateRuleService.findAll(new Sort(Direction.DESC, "createDate"));
	}
	
	@RequestMapping(value = "/findByFieldId", method = RequestMethod.GET)
	public @ResponseBody List<FieldValidateRule> findByFieldId(String fieldId){
		Condition c = new Condition();
		c.setPropertyName("fieldId");
		c.setPropertyValue(fieldId);
		c.setRelateType(RelateType.AND);
		c.setOperation(Operation.EQ);
		return fieldValidateRuleService.findByCondition(c);
	}
	
	
}
