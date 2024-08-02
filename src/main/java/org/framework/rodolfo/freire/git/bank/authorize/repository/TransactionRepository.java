package org.framework.rodolfo.freire.git.bank.authorize.repository;

import org.framework.rodolfo.freire.git.bank.authorize.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for CRUD operations related to the {@link Transaction} entity.
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
