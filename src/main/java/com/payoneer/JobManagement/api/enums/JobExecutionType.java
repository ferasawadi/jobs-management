package com.payoneer.JobManagement.api.enums;

/**
 * execution types for Jobs
 *
 * @author Feras E Alawadi
 * @version 1.0.101
 * @since 1.0.101
 */
public enum JobExecutionType {
    /**
     * job to be queued
     */
    QUEUED,
    /**
     * job for instant execution
     */
    EXECUTE,
    /**
     * for testing only.
     */
    UNKNOWN
}
