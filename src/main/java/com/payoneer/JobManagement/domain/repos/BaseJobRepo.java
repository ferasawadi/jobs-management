package com.payoneer.JobManagement.domain.repos;

import com.payoneer.JobManagement.domain.entities.BaseJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * core database repository that is used to deal with database
 * every subclass of the super class repository must extend this to have a valid and working system.
 *
 * @param <T>  Pojo type that extends BaseJobEntity
 * @param <ID> UUID
 * @author Feras E Alawadi
 * @version 1.0.101
 * @see BaseJobEntity
 * @since 1.0.101
 */
@NoRepositoryBean
@Transactional
public interface BaseJobRepo<T extends BaseJobEntity, ID extends Serializable> extends JpaRepository<T, ID> {
}
