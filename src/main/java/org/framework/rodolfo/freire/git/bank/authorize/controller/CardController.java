package org.framework.rodolfo.freire.git.bank.authorize.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.framework.rodolfo.freire.git.bank.authorize.model.Card;
import org.framework.rodolfo.freire.git.bank.authorize.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Card} entities.
 * <p>
 * Provides endpoints for CRUD operations on card resources. Utilizes {@link CardService} to interact with
 * the data layer and perform operations on card entities.
 * </p>
 * <ul>
 *   <li>{@code GET /api/cards} - Retrieves all cards.</li>
 *   <li>{@code GET /api/cards/{id}} - Retrieves a specific card by its ID.</li>
 *   <li>{@code POST /api/cards} - Creates a new card.</li>
 *   <li>{@code PUT /api/cards/{id}} - Updates an existing card by its ID.</li>
 *   <li>{@code DELETE /api/cards/{id}} - Deletes a card by its ID.</li>
 * </ul>
 *
 * @see CardService
 * @see Card
 */
@Hidden
@RestController
@RequestMapping("/api/cards")
@Tag(name = "Card", description = "Endpoints for CRUD operations on card resources.")
public class CardController {

    private final CardService cardService;

    /**
     * Constructor to initialize {@link CardService}.
     *
     * @param cardService Service for handling card-related operations.
     */
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Retrieves a list of all {@link Card} entities.
     *
     * @return A {@link ResponseEntity} containing a list of all cards.
     */
    @GetMapping
    @Operation(summary = "Get all cards", description = "Retrieves a list of all card entities.")
    @ApiResponse(responseCode = "200", description = "List of cards retrieved")
    public ResponseEntity<List<Card>> getAll() {
        List<Card> cards = cardService.getAll();
        return ResponseEntity.ok(cards);
    }

    /**
     * Retrieves a specific {@link Card} by its ID.
     *
     * @param id The ID of the card to be retrieved.
     * @return A {@link ResponseEntity} containing the card if found, or a {@code 404 Not Found} response if not found.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get card by ID", description = "Retrieves a card entity by its ID.")
    @ApiResponse(responseCode = "200", description = "Card retrieved")
    @ApiResponse(responseCode = "404", description = "Card not found")
    public ResponseEntity<Card> getById(@PathVariable Long id) {
        Optional<Card> card = cardService.getById(id);
        return card.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Creates a new {@link Card}.
     *
     * @param card The card data to be created.
     * @return A {@link ResponseEntity} containing the created card and status.
     */
    @PostMapping
    @Operation(summary = "Create new card", description = "Creates a new card entity.")
    @ApiResponse(responseCode = "201", description = "Card created")
    @ApiResponse(responseCode = "400", description = "Invalid card details")
    public ResponseEntity<Card> save(@Valid @RequestBody Card card) {
        if (card.getCardId() == null || card.getCardNumber() == null || card.getCardType() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Card createdCard = cardService.save(card);
        return ResponseEntity.status(HttpStatus.OK).body(createdCard);
    }

    /**
     * Updates an existing {@link Card} specified by its ID.
     *
     * @param id   The ID of the card to be updated.
     * @param card The card data with updates.
     * @return A {@link ResponseEntity} containing the updated card.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update card", description = "Updates an existing card entity by its ID.")
    @ApiResponse(responseCode = "200", description = "Card updated")
    @ApiResponse(responseCode = "404", description = "Card not found")
    public ResponseEntity<Card> update(@PathVariable Long id, @Valid @RequestBody Card card) {
        card.setCardId(id);
        Card updatedCard = cardService.save(card);
        return updatedCard != null ? ResponseEntity.ok(updatedCard) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Deletes a {@link Card} specified by its ID.
     *
     * @param id The ID of the card to be deleted.
     * @return A {@link ResponseEntity} indicating the deletion status.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete card", description = "Deletes a card entity by its ID.")
    @ApiResponse(responseCode = "204", description = "Card deleted")
    @ApiResponse(responseCode = "404", description = "Card not found")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = cardService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
