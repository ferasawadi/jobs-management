package com.payoneer.JobManagement.api.response;

import com.payoneer.JobManagement.api.enums.ApiResponseCodes;

/**
 * High level API Response, this model represents the Generic response for our apis,
 * it will be a standard JSON Response across the system.
 * ex: {
 *     {
 *     "status": true,
 *     "code": "SUCCESS",
 *     "message": "simple api message",
 *     "results": {
 *              ...
 *     },
 *     "errors": {
 *         "errorMessage": " error message",
 *         "errorCode": "RESOURCE_NOT_FOUND"
 *     }
 * }
 *
 * @param <T> Results Parameter content Type, basically a Java Class or any Primitive type.
 * @author Feras E Alawadi
 * @version 1.0.101
 * @since 1.0.101
 */
public final class ApiResponse<T, R> {
    /**
     * status flag
     */
    private final boolean status;
    /**
     * response Code
     *
     * @see ApiResponseCodes
     */
        private final String code;
    /**
     * response message
     */
    private final String message;
    /**
     * response results
     */
    private final T results;
    /**
     * response Errors
     */
    private R errors;

    /**
     * constructor without errors
     *
     * @param status  boolean
     * @param code    enum
     * @param message String
     * @param results T Type
     */
    public ApiResponse(boolean status, String code, String message, T results) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.results = results;
    }

    /**
     * constructor with errors
     *
     * @param status  boolean
     * @param code    enum
     * @param message String
     * @param results T Type
     * @param errors  R Error Type
     * @see ErrorResponse
     */

    public ApiResponse(boolean status, String code, String message, T results, R errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.results = results;
        this.errors = errors;
    }

    /**
     * method to get the error response
     *
     * @return error object
     * @see ErrorResponse
     */
    public R getErrors() {
        return errors;
    }

    /**
     * method to get the status code
     *
     * @return boolean
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * to get the api code
     *
     * @return String
     */
    public String getCode() {
        return code;
    }


    /**
     * method to get the response message
     *
     * @return String message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Method to get the results.
     *
     * @return results
     */
    public T getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", results=" + results +
                ", errors=" + errors +
                '}';
    }
}
