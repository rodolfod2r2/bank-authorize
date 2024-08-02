package org.framework.rodolfo.freire.git.bank.authorize.repository;

import org.framework.rodolfo.freire.git.bank.authorize.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CRUD operations related to the {@link Merchant} entity.
 */

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    /**
     * Search for a merchant by MCC code.
     *
     * @param mcc The merchant's MCC code.
     * @return A {@link Optional} containing the merchant if found, otherwise {@link Optional#empty()}.
     */
    Optional<Merchant> findByMerchantMcc(String mcc);

}
