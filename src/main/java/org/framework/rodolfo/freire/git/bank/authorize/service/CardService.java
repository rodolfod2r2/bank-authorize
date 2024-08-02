package org.framework.rodolfo.freire.git.bank.authorize.service;

import org.framework.rodolfo.freire.git.bank.authorize.model.Card;
import org.framework.rodolfo.freire.git.bank.authorize.repository.CardRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling operations related to cards.
 * <p>
 * The {@code CardService} class provides methods for managing card entities. This includes operations for
 * retrieving, creating, updating, and deleting cards. It interacts with the {@code CardRepository} to perform
 * these operations.
 * </p>
 * <p>
 * The service class is annotated with {@code @Service}, marking it as a Spring service component that should be
 * managed by the Spring container.
 * </p>
 *
 * @see Card
 * @see CardRepository
 */
@Service
public class CardService {

    private final CardRepository cardRepository;

    /**
     * Constructs a {@code CardService} with the specified {@code CardRepository}.
     * <p>
     * This constructor initializes the service with the repository needed for performing card-related operations.
     * </p>
     *
     * @param cardRepository The repository used to access card data.
     */
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    /**
     * Retrieves all cards from the repository.
     *
     * @return A list of all cards.
     */
    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    /**
     * Retrieves a card by its ID.
     *
     * @param cardId The ID of the card to retrieve.
     * @return An {@code Optional} containing the card if found, or an empty {@code Optional} if not found.
     */
    public Optional<Card> getById(Long cardId) {
        return cardRepository.findById(cardId);
    }

    /**
     * Retrieves a card by its ID.
     *
     * @param cardId The ID of the card to retrieve.
     * @return An {@code Optional} containing the card if found, or an empty {@code Optional} if not found.
     */
    public Optional<Card> getByCardNumber(String cardId) {
        return cardRepository.getByCardNumber(cardId);
    }

    /**
     * Creates a new card in the repository.
     *
     * @param card The card to create.
     * @return The created card.
     */
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    /**
     * Updates an existing card in the repository.
     * <p>
     * The card with the specified ID will be updated with the information from the provided card object.
     * If the card with the given ID does not exist, the method returns {@code null}.
     * </p>
     *
     * @param cardId The ID of the card to update.
     * @param card   The card with updated information.
     * @return The updated card, or {@code null} if the card with the given ID does not exist.
     */
    public Card update(Long cardId, Card card) {
        if (cardRepository.existsById(cardId)) {
            card.setCardId(cardId);
            return cardRepository.save(card);
        }
        return null;
    }

    /**
     * Removes a card by its ID.
     *
     * @param cardId The ID of the card to remove.
     */
    @Transactional
    public boolean delete(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            return false;
        }
        try {
            cardRepository.deleteById(cardId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}

