package com.payoneer.JobManagement.api.utils;


import com.payoneer.JobManagement.api.response.ApiResponse;
import com.payoneer.JobManagement.api.enums.ApiResponseCodes;
import com.payoneer.JobManagement.api.response.ErrorResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.UUID;

/**
 * Helper methods to generate API response out of ApiResponse Wrapper.
 *
 * @author Feras E Alawadi
 * @version 1.0.101
 * @see ApiResponse
 * @see ApiResponseCodes
 * @see ErrorResponse
 * @see com.payoneer.JobManagement.api.response.DeleteResponse
 * @since 1.0.101
 */
public final class ApiUtils {

    /**
     * return success response
     *
     * @param status  boolean
     * @param message String
     * @param code    enum
     * @param body    the body type
     * @param <T>     type
     * @return api response
     */
    public static <T> ApiResponse<T, ErrorResponse> success(boolean status, String message, String code, T body) {
        return new ApiResponse<>(
                status,
                code,
                message,
                body
        );
    }


    /**
     * returns an api response with error body
     *
     * @param status  true or false
     * @param message error message
     * @param code    error code
     * @param error   error pojo
     * @param <T>     null
     * @param <R>     error pojo type
     * @return response
     */
    public static <T, R> ApiResponse<T, R> errorResponse(boolean status, String message, String code, R error) {
        return new ApiResponse<>(
                status,
                code,
                message,
                null,
                error
        );
    }

    /**
     * Method to validate UUID Strings format
     *
     * @param uuid string
     * @return boolean response
     */
    public static boolean validUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * method that takes a string and return an Instant Object.
     *
     * @param offset  the date as string
     * @param pattern format pattern  ex: "yyyy-MM-dd HH:mm:ss"
     * @return Instant object.
     */
    public static Instant stringToInstant(String offset, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern.trim());

        TemporalAccessor temporalAccessor = formatter.parse(offset);
        LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
        return Instant.from(zonedDateTime);
    }

}