package org.apel.hera.biz.dao;

import java.util.List;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.hera.biz.domain.Domain;

public interface DomainRepository extends CommonRepository<Domain, String>{

	List<Domain> findByProjectId(String projectId);
	
}
