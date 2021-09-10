package com.payoneer.JobManagement.api.response;

/**
 * Error POJO class used to push a generic error response!
 *
 * @author Feras E Alawadi
 * @version 1.0.101
 * @since 1.0.101
 */
public class ErrorResponse {
    /**
     * String represents the error message
     */
    private String errorMessage;
    /**
     * the error Code
     */
    private String errorCode;

    /**
     * default constructor
     */
    public ErrorResponse() {
    }

    /**
     * constructor with params
     *
     * @param errorMessage the error message
     * @param errorCode    the error code
     */
    public ErrorResponse(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    /**
     * Getting the error Messgae
     *
     * @return String
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * setting the error message
     *
     * @param errorMessage the error message
     */

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * getting the error code
     *
     * @return String
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * setting the error Code.
     *
     * @param errorCode String
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }


    @Override
    public String toString() {
        return "ErrorResponse{" +
                "errorMessage='" + errorMessage + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}
