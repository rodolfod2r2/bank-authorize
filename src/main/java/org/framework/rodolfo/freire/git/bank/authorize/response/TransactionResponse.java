package org.framework.rodolfo.freire.git.bank.authorize.response;


public class TransactionResponse {
    private String code;

    public TransactionResponse(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
