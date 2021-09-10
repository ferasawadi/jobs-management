package com.payoneer.JobManagement.api.response;


/**
 * Class To return Generic Delete Entity response
 *
 * @author Feras E Alawadi
 * @version 1.0.101
 * @since 1.0.101
 */
public class DeleteResponse {
    /**
     * string represents the status
     */
    protected String deleteCode;

    /**
     * default constructor
     */
    public DeleteResponse() {
    }

    /**
     * constructor with params
     *
     * @param deleteCode the code status
     */
    public DeleteResponse(String deleteCode) {
        this.deleteCode = deleteCode;
    }

    /**
     * get the error Code
     *
     * @return string
     */
    public String getDeleteCode() {
        return deleteCode;
    }

    /**
     * c setting the error code
     *
     * @param deleteCode String
     */
    public void setDeleteCode(String deleteCode) {
        this.deleteCode = deleteCode;
    }


    @Override
    public String toString() {
        return "DeleteResponse{" +
                "deleteCode='" + deleteCode + '\'' +
                '}';
    }
}