package org.framework.rodolfo.freire.git.bank.authorize.service;

import org.framework.rodolfo.freire.git.bank.authorize.model.Merchant;
import org.framework.rodolfo.freire.git.bank.authorize.repository.MerchantRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling operations related to merchants.
 * <p>
 * The {@code MerchantService} class provides methods for managing merchant entities. This includes operations
 * for retrieving, creating, updating, and deleting merchants. It interacts with the {@code MerchantRepository}
 * to perform these operations.
 * </p>
 * <p>
 * The service class is annotated with {@code @Service}, marking it as a Spring service component that should be
 * managed by the Spring container.
 * </p>
 *
 * @see Merchant
 * @see MerchantRepository
 */
@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    /**
     * Constructs a {@code MerchantService} with the specified {@code MerchantRepository}.
     * <p>
     * This constructor initializes the service with the repository needed for performing merchant-related
     * operations.
     * </p>
     *
     * @param merchantRepository The repository used to access merchant data.
     */
    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    /**
     * Retrieves all merchants from the repository.
     *
     * @return A list of all merchants.
     */
    public List<Merchant> getAll() {
        return merchantRepository.findAll();
    }

    /**
     * Retrieves a merchant by its ID.
     *
     * @param merchantId The ID of the merchant to retrieve.
     * @return An {@code Optional} containing the merchant if found, or an empty {@code Optional} if not found.
     */
    public Optional<Merchant> getById(Long merchantId) {
        return merchantRepository.findById(merchantId);
    }

    /**
     * Retrieves a merchant by its Merchant Category Code (MCC).
     *
     * @param mcc The MCC of the merchant to retrieve.
     * @return An {@code Optional} containing the merchant if found, or an empty {@code Optional} if not found.
     */
    public Optional<Merchant> getByMcc(String mcc) {
        return merchantRepository.findByMerchantMcc(mcc);
    }

    /**
     * Creates a new merchant in the repository.
     *
     * @param merchant The merchant to create.
     * @return The created merchant.
     */
    public Merchant save(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    /**
     * Updates an existing merchant in the repository.
     * <p>
     * The merchant with the specified ID will be updated with the information from the provided merchant object.
     * If the merchant with the given ID does not exist, the method returns {@code null}.
     * </p>
     *
     * @param merchantId The ID of the merchant to update.
     * @param merchant   The merchant with updated information.
     * @return The updated merchant, or {@code null} if the merchant with the given ID does not exist.
     */
    public Merchant update(Long merchantId, Merchant merchant) {
        if (merchantRepository.existsById(merchantId)) {
            merchant.setMerchantId(merchantId);
            return merchantRepository.save(merchant);
        }
        return null;
    }

    /**
     * Deletes a merchant by its ID.
     *
     * @param merchantId The ID of the merchant to delete.
     */
    @Transactional
    public boolean delete(Long merchantId) {
        if (!merchantRepository.existsById(merchantId)) {
            throw new RuntimeException("Merchant not found with id: " + merchantId);
        }
        try {
            merchantRepository.deleteById(merchantId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
