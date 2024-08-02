package org.framework.rodolfo.freire.git.bank.authorize.repository;

import org.framework.rodolfo.freire.git.bank.authorize.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for CRUD operations related to the entity {@link Customer}.
 */

@Repository
public interface ClientRepository extends JpaRepository<Customer, Long> {
}
