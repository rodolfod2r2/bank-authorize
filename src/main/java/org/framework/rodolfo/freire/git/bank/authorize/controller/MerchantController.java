package org.framework.rodolfo.freire.git.bank.authorize.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.framework.rodolfo.freire.git.bank.authorize.model.Merchant;
import org.framework.rodolfo.freire.git.bank.authorize.service.MerchantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link Merchant} entities.
 * <p>
 * Provides endpoints for CRUD operations on merchant resources using {@link MerchantService} to interact with the data layer.
 * </p>
 * <ul>
 *   <li>{@code GET /api/merchants} - Retrieves a list of all merchants.</li>
 *   <li>{@code GET /api/merchants/{merchantId}} - Retrieves a specific merchant by its ID.</li>
 *   <li>{@code GET /api/merchants/mcc/{mcc}} - Retrieves a specific merchant by its MCC (Merchant Category Code).</li>
 *   <li>{@code POST /api/merchants} - Creates a new merchant.</li>
 *   <li>{@code PUT /api/merchants/{merchantId}} - Updates an existing merchant specified by its ID.</li>
 *   <li>{@code DELETE /api/merchants/{merchantId}} - Deletes a merchant specified by its ID.</li>
 * </ul>
 *
 * @see MerchantService
 * @see Merchant
 */
@RestController
@RequestMapping("/api/merchants")
@Tag(name = "Merchant", description = "Endpoints for performing CRUD operations on merchant resources.")
public class MerchantController {

    private final MerchantService merchantService;

    /**
     * Constructs a new {@link MerchantController} with the specified {@link MerchantService}.
     *
     * @param merchantService Service for handling merchant-related operations.
     */
    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    /**
     * Retrieves a list of all {@link Merchant} entities.
     *
     * @return A {@link ResponseEntity} containing a list of all merchants.
     */
    @Hidden
    @GetMapping
    @Operation(summary = "Get all merchants", description = "Retrieves a list of all merchant entities.")
    @ApiResponse(responseCode = "200", description = "List of merchants retrieved successfully.")
    public ResponseEntity<List<Merchant>> getAll() {
        List<Merchant> merchants = merchantService.getAll();
        return ResponseEntity.ok(merchants);
    }

    /**
     * Retrieves a specific {@link Merchant} by its ID.
     *
     * @param merchantId The ID of the merchant to retrieve.
     * @return A {@link ResponseEntity} containing the merchant if found, or a {@code 404 Not Found} response if not found.
     */
    @Hidden
    @GetMapping("/{merchantId}")
    @Operation(summary = "Get merchant by ID", description = "Retrieves a merchant entity by its ID.")
    @ApiResponse(responseCode = "200", description = "Merchant retrieved successfully.")
    @ApiResponse(responseCode = "404", description = "Merchant not found.")
    public ResponseEntity<Merchant> getById(@PathVariable Long merchantId) {
        Optional<Merchant> merchant = merchantService.getById(merchantId);
        return merchant.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Retrieves a specific {@link Merchant} by its MCC (Merchant Category Code).
     *
     * @param mcc The MCC of the merchant to retrieve.
     * @return A {@link ResponseEntity} containing the merchant if found, or a {@code 404 Not Found} response if not found.
     */
    @GetMapping("/mcc/{mcc}")
    @Operation(summary = "Get merchant by MCC", description = "Retrieves a merchant entity by its MCC (Merchant Category Code).")
    @ApiResponse(responseCode = "200", description = "Merchant retrieved successfully.")
    @ApiResponse(responseCode = "404", description = "Merchant not found.")
    public ResponseEntity<Merchant> getByMcc(@PathVariable String mcc) {
        Optional<Merchant> merchant = merchantService.getByMcc(mcc);
        return merchant.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Creates a new {@link Merchant}.
     *
     * @param merchant The merchant data to be created.
     * @return A {@link ResponseEntity} containing the created merchant and status.
     */
    @PostMapping
    @Operation(summary = "Create new merchant", description = "Creates a new merchant entity.")
    @ApiResponse(responseCode = "201", description = "Merchant created successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid merchant data provided.")
    public ResponseEntity<Merchant> save(@Valid @RequestBody Merchant merchant) {
        if(Objects.equals(merchant.getMerchantName(), "") || Objects.equals(merchant.getMerchantMcc(), "")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Merchant createdMerchant = merchantService.save(merchant);
        return ResponseEntity.status(HttpStatus.OK).body(createdMerchant);
    }

    /**
     * Updates an existing {@link Merchant} specified by its ID.
     *
     * @param merchantId The ID of the merchant to be updated.
     * @param merchant   The merchant data with updates.
     * @return A {@link ResponseEntity} containing the updated merchant if successful, or {@code 404 Not Found} if the merchant does not exist.
     */
    @Hidden
    @PutMapping("/{merchantId}")
    @Operation(summary = "Update merchant", description = "Updates an existing merchant entity by its ID.")
    @ApiResponse(responseCode = "200", description = "Merchant updated successfully.")
    @ApiResponse(responseCode = "404", description = "Merchant not found.")
    public ResponseEntity<Merchant> update(@PathVariable Long merchantId, @Valid @RequestBody Merchant merchant) {
        merchant.setMerchantId(merchantId);
        Merchant updatedMerchant = merchantService.save(merchant);
        return updatedMerchant != null ? ResponseEntity.ok(updatedMerchant)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Deletes a {@link Merchant} specified by its ID.
     *
     * @param merchantId The ID of the merchant to be deleted.
     * @return A {@link ResponseEntity} indicating the deletion status.
     */
    @Hidden
    @DeleteMapping("/{merchantId}")
    @Operation(summary = "Delete merchant", description = "Deletes a merchant entity by its ID.")
    @ApiResponse(responseCode = "204", description = "Merchant deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Merchant not found.")
    public ResponseEntity<Void> delete(@PathVariable Long merchantId) {
        boolean deleted = merchantService.delete(merchantId);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
