package org.apel.hera.biz.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Operation;
import org.apel.gaia.commons.pager.RelateType;
import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.hera.biz.domain.Field;
import org.apel.hera.biz.domain.FieldValidateRule;
import org.apel.hera.biz.service.FieldService;
import org.apel.hera.biz.service.FieldValidateRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FieldServiceImpl extends AbstractBizCommonService<Field, String> implements FieldService{

	@Autowired
	private FieldValidateRuleService fieldValidateRuleService;
	
	@Override
	public void deleteById(String... ids) {
		for (String fieldId : ids) {
			Condition c = new Condition();
			c.setPropertyName("fieldId");
			c.setPropertyValue(fieldId);
			c.setRelateType(RelateType.AND);
			c.setOperation(Operation.EQ);
			List<FieldValidateRule> validateRules = fieldValidateRuleService.findByCondition(c);
			String[] ruleIds = validateRules.stream().map(e -> e.getId()).collect(Collectors.toList()).toArray(new String[]{});
			fieldValidateRuleService.deleteById(ruleIds);
		}
		super.deleteById(ids);
	}

	

	
}
