package org.framework.rodolfo.freire.git.bank.authorize.repository;

import org.framework.rodolfo.freire.git.bank.authorize.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CRUD operations related to the {@link Account} entity.
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> getByCardCardId(Long id);
}
