package org.framework.rodolfo.freire.git.bank.authorize.repository;

import org.framework.rodolfo.freire.git.bank.authorize.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for CRUD operations related to the {@link Balance} entity.
 */

@Repository
public interface AccountBalanceRepository extends JpaRepository<Balance, Long> {
}
