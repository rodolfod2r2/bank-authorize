package org.framework.rodolfo.freire.git.bank.authorize.unit;

import org.framework.rodolfo.freire.git.bank.authorize.model.Card;
import org.framework.rodolfo.freire.git.bank.authorize.model.CardType;
import org.framework.rodolfo.freire.git.bank.authorize.repository.CardRepository;
import org.framework.rodolfo.freire.git.bank.authorize.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CardService} class.
 * <p>
 * This class tests various scenarios for the card management functionality in the {@link CardService}:
 * - Successful retrieval of an existing card.
 * - Successful saving of a card.
 * - Retrieval of a non-existing card.
 * - Saving a card with an existing ID.
 * - Behavior when attempting to save a null card.
 * - Behavior when attempting to retrieve a card with a null ID.
 * </p>
 */
public class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    /**
     * Initializes mocks before each test method.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests successful retrieval of an existing card by its ID.
     * <p>
     * This test verifies that retrieving a card by its ID returns the correct card and its type.
     * </p>
     */
    @Test
    void testGetCardSuccess() {
        Card card = new Card();
        card.setCardId(1L);
        card.setCardType(CardType.CREDIT);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        Optional<Card> result = cardService.getById(1L);

        assertTrue(result.isPresent(), "The card should be present.");
        assertEquals(CardType.CREDIT, result.get().getCardType(), "The card type is incorrect.");
    }

    /**
     * Tests successful saving of a new card.
     * <p>
     * This test verifies that saving a card returns the saved card with the correct type and that
     * the cardRepository's save method is called.
     * </p>
     */
    @Test
    void testSaveCard() {
        Card card = new Card();
        card.setCardId(1L);
        card.setCardType(CardType.DEBIT);
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        Card result = cardService.save(card);

        assertEquals(CardType.DEBIT, result.getCardType(), "The saved card type is incorrect.");
        verify(cardRepository).save(card);
    }

    /**
     * Tests retrieval of a card that does not exist.
     * <p>
     * This test verifies that retrieving a card by a non-existing ID returns an empty result.
     * </p>
     */
    @Test
    void testGetCardNotFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Card> result = cardService.getById(1L);

        assertFalse(result.isPresent(), "The card should not be present.");
    }

    /**
     * Tests saving a card with an existing ID.
     * <p>
     * This test verifies that saving a card with an ID that already exists updates the card correctly,
     * and that the cardRepository's save method is called with the new card details.
     * </p>
     */
    @Test
    void testSaveCardWithExistingId() {
        Card existingCard = new Card();
        existingCard.setCardId(1L);
        existingCard.setCardType(CardType.CREDIT);

        Card newCard = new Card();
        newCard.setCardId(1L);
        newCard.setCardType(CardType.DEBIT);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(existingCard));
        when(cardRepository.save(any(Card.class))).thenReturn(newCard);

        Card result = cardService.save(newCard);

        assertEquals(CardType.DEBIT, result.getCardType(), "The saved card type is incorrect.");
        assertEquals(1L, result.getCardId(), "The saved card ID is incorrect.");
        verify(cardRepository).save(newCard);
    }

    /**
     * Tests the behavior when attempting to save a null card.
     * <p>
     * This test verifies that attempting to save a null card returns null and does not call the
     * cardRepository's save method.
     * </p>
     */
    @Test
    void testSaveNullCard() {
        Card result = cardService.save(null);

        assertNull(result, "The result of saving a null card should be null.");
        verify(cardRepository, never()).save(any(Card.class));
    }

    /**
     * Tests the behavior when attempting to retrieve a card with a null ID.
     * <p>
     * This test verifies that retrieving a card with a null ID returns an empty result.
     * </p>
     */
    @Test
    void testGetNullCard() {
        when(cardRepository.findById(null)).thenReturn(Optional.empty());

        Optional<Card> result = cardService.getById(null);

        assertFalse(result.isPresent(), "Retrieving a card with a null ID should return empty.");
    }
}
