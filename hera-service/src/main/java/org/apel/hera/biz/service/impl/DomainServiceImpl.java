package org.apel.hera.biz.service.impl;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.hera.biz.domain.Domain;
import org.apel.hera.biz.service.DomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DomainServiceImpl extends AbstractBizCommonService<Domain, String> implements DomainService{

	@Override
	protected String getPageQl() {
		return "select d.id,d.domainName,d.className,d.tableName,d.createDate as createDate,p.id as projectId,p.projectName from Domain d left join d.project p where 1=1";
	}

	
	
}
