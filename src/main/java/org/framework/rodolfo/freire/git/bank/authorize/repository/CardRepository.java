package org.framework.rodolfo.freire.git.bank.authorize.repository;

import org.framework.rodolfo.freire.git.bank.authorize.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CRUD operations related to the {@link Card} entity.
 */

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> getByCardNumber(String cardNUmber);
}
