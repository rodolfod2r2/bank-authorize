package org.framework.rodolfo.freire.git.bank.authorize.response;


/**
 * Represents the response of a transaction.
 * <p>
 * This class encapsulates the response code of a transaction, which can be used to indicate the result or status of the operation.
 * </p>
 */
public class TransactionResponse {

    /**
     * Transaction response code.
     */
    private String code;

    /**
     * Constructs a new instance of {@code TransactionResponse} with the given code.
     *
     * @param code the transaction response code
     */
    public TransactionResponse(String code) {
        this.code = code;
    }

    /**
     * Gets the transaction response code.
     *
     * @return the transaction response code
     */
    public String getCode() {
        return code;
    }

    /**
     * Defines the transaction response code.
     *
     * @param code the transaction response code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
}