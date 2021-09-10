package com.payoneer.JobManagement.system;

import com.payoneer.JobManagement.domain.entities.BaseJobEntity;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * demonstration for new updates that comes to the system
 *
 * @param <T>  new Type
 * @param <ID> new type
 * @author Feras E Alawadi
 * @version 1.0.101
 * @since 1.0.101
 */
@NoRepositoryBean
@Transactional
public interface JobUpdated<T extends BaseJobEntity, ID extends Serializable> extends Job<T, ID> {

    // future method.
    void futureMethod();
}
