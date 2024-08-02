package org.framework.rodolfo.freire.git.bank.authorize.unit;

import org.framework.rodolfo.freire.git.bank.authorize.model.Merchant;
import org.framework.rodolfo.freire.git.bank.authorize.repository.MerchantRepository;
import org.framework.rodolfo.freire.git.bank.authorize.service.MerchantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link MerchantService} class.
 * <p>
 * This class tests various scenarios for the merchant management functionality in the {@link MerchantService}:
 * - Successful retrieval of an existing merchant.
 * - Successful saving of a merchant.
 * - Retrieval of a non-existing merchant.
 * - Behavior when attempting to save a null merchant.
 * - Updating an existing merchant.
 * - Deleting an existing merchant.
 * - Attempting to delete a non-existing merchant.
 * </p>
 */
public class MerchantServiceTest {

    @InjectMocks
    private MerchantService merchantService;

    @Mock
    private MerchantRepository merchantRepository;

    /**
     * Initializes mocks before each test method.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests successful retrieval of an existing merchant by its ID.
     * <p>
     * This test verifies that retrieving a merchant by its ID returns the correct merchant and its name.
     * </p>
     */
    @Test
    void testGetMerchantSuccess() {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);
        merchant.setMerchantName("Merchant A");
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));

        Optional<Merchant> result = merchantService.getById(1L);

        assertTrue(result.isPresent(), "The merchant should be present.");
        assertEquals("Merchant A", result.get().getMerchantName(), "The merchant name is incorrect.");
    }

    /**
     * Tests successful saving of a merchant.
     * <p>
     * This test verifies that saving a merchant returns the saved merchant with the correct name and that
     * the merchantRepository's save method is called.
     * </p>
     */
    @Test
    void testSaveMerchant() {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(1L);
        merchant.setMerchantName("Merchant B");
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        Merchant result = merchantService.save(merchant);

        assertEquals("Merchant B", result.getMerchantName(), "The saved merchant name is incorrect.");
        verify(merchantRepository).save(merchant);
    }

    /**
     * Tests retrieval of a merchant that does not exist.
     * <p>
     * This test verifies that retrieving a merchant by a non-existing ID returns an empty result.
     * </p>
     */
    @Test
    void testGetMerchantNotFound() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Merchant> result = merchantService.getById(1L);

        assertFalse(result.isPresent(), "The merchant should not be present.");
    }

    /**
     * Tests the behavior when attempting to save a null merchant.
     * <p>
     * This test verifies that saving a null merchant returns null and does not call the
     * merchantRepository's save method.
     * </p>
     */
    @Test
    void testSaveNullMerchant() {
        Merchant result = merchantService.save(null);

        assertNull(result, "The result of saving a null merchant should be null.");
        verify(merchantRepository, never()).save(any(Merchant.class));
    }

    /**
     * Tests updating an existing merchant.
     * <p>
     * This test verifies that updating a merchant correctly updates the merchant's details and that the
     * merchantRepository's save method is called with the updated merchant.
     * </p>
     */
    @Test
    void testUpdateMerchant() {
        Merchant existingMerchant = new Merchant();
        existingMerchant.setMerchantId(1L);
        existingMerchant.setMerchantName("Merchant A");

        Merchant updatedMerchant = new Merchant();
        updatedMerchant.setMerchantId(1L);
        updatedMerchant.setMerchantName("Merchant C");

        when(merchantRepository.findById(1L)).thenReturn(Optional.of(existingMerchant));
        when(merchantRepository.save(any(Merchant.class))).thenReturn(updatedMerchant);

        Merchant result = merchantService.save(updatedMerchant);

        assertEquals("Merchant C", result.getMerchantName(), "The updated merchant name is incorrect.");
        verify(merchantRepository).save(updatedMerchant);
    }

    /**
     * Tests deleting an existing merchant.
     * <p>
     * This test verifies that deleting a merchant correctly invokes the delete method on the merchantRepository.
     * </p>
     */
    @Test
    void testDeleteMerchant() {
        when(merchantRepository.existsById(1L)).thenReturn(true);
        doNothing().when(merchantRepository).deleteById(1L);

        merchantService.delete(1L);

        verify(merchantRepository).deleteById(1L);
    }

    /**
     * Tests attempting to delete a merchant that does not exist.
     * <p>
     * This test verifies that attempting to delete a non-existing merchant throws a runtime exception and that
     * the merchantRepository's delete method is not called.
     * </p>
     */
    @Test
    void testDeleteMerchantNotFound() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> merchantService.delete(1L),
                "Should throw an exception when attempting to delete a non-existing merchant.");

        verify(merchantRepository, never()).deleteById(any(Long.class));
    }
}
