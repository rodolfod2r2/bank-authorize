package org.framework.rodolfo.freire.git.bank.authorize.integrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.framework.rodolfo.freire.git.bank.authorize.controller.CardController;
import org.framework.rodolfo.freire.git.bank.authorize.model.Card;
import org.framework.rodolfo.freire.git.bank.authorize.model.CardType;
import org.framework.rodolfo.freire.git.bank.authorize.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link CardController}.
 * <p>
 * This class contains integration tests for the {@code CardController}, focusing on card retrieval and saving operations.
 * The tests use MockMvc to perform HTTP requests and validate responses to ensure that the controller behaves as expected.
 * </p>
 */
@WebMvcTest(CardController.class)
public class CardControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CardService cardService;

    /**
     * Tests the retrieval of an existing card.
     * <p>
     * This test verifies that a card with a specific ID can be successfully retrieved. It mocks the card service to return
     * a predefined card object and then checks that the response status is OK and the response body contains the correct
     * card details, including a null card number and the card type.
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testGetCard() throws Exception {
        Card card = new Card();
        card.setCardId(1L);
        card.setCardNumber(null);
        card.setCardType(CardType.CREDIT);

        when(cardService.getById(1L)).thenReturn(Optional.of(card));

        mockMvc.perform(get("/api/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.card_id").value(1))
                .andExpect(jsonPath("$.number").value(nullValue()))
                .andExpect(jsonPath("$.type").value("CREDIT"));
    }

    /**
     * Tests the saving of a new card.
     * <p>
     * This test verifies that a new card can be successfully saved. It sends a POST request with a card object and expects
     * a successful response with status 200 OK. The card details include a valid card ID, number, and type.
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testSaveCard() throws Exception {
        Card card = new Card();
        card.setCardId(1L);
        card.setCardNumber("1234567890");
        card.setCardType(CardType.DEBIT);

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(card)))
                .andExpect(status().isOk()); // Expects a 200 OK status
    }

    /**
     * Tests the retrieval of a card that does not exist.
     * <p>
     * This test verifies that attempting to retrieve a non-existent card results in a "404 Not Found" status. It mocks
     * the card service to return an empty optional and then checks that the response status is "Not Found".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testGetCard_NotFound() throws Exception {
        given(cardService.getById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/cards/{cardId}", 1L))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests saving a card with invalid data.
     * <p>
     * This test verifies that attempting to save a card with invalid data (such as a null card ID and an invalid card type)
     * results in a "400 Bad Request" response. The test sends a POST request with an improperly constructed card object and
     * checks that the response status is "Bad Request".
     * </p>
     *
     * @throws Exception if an error occurs when making the request
     */
    @Test
    void testSaveCard_BadRequest() throws Exception {
        Card card = new Card();
        card.setCardId(null);
        card.setCardType(null); // Invalid card type

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(card)))
                .andExpect(status().isBadRequest());
    }
}
