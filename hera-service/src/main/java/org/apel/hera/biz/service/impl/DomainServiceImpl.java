package org.apel.hera.biz.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apel.gaia.commons.pager.Condition;
import org.apel.gaia.commons.pager.Operation;
import org.apel.gaia.commons.pager.RelateType;
import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.hera.biz.dao.DomainRepository;
import org.apel.hera.biz.domain.Domain;
import org.apel.hera.biz.domain.Field;
import org.apel.hera.biz.domain.ModuleRowColumn;
import org.apel.hera.biz.service.DomainService;
import org.apel.hera.biz.service.FieldService;
import org.apel.hera.biz.service.ModuleRowColService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DomainServiceImpl extends AbstractBizCommonService<Domain, String> implements DomainService{

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
	
}
