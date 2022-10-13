/**
 * Cloned and extended from {@link io.swagger.oas.inflector.models.ApiError} in
 * order to add stacktrace to the output.
 */
package io.swagger.petstore.utils;

import com.google.common.base.Throwables;

public class ApiError {
    private int code;
    private String message;
    private String stacktrace;

    public ApiError() {
    }

    public ApiError(Exception exception) {
        this.stacktrace = Throwables.getStackTraceAsString(exception);
    }

    public ApiError code(int code) {
        this.code = code;
        return this;
    }

    public ApiError message(String message) {
        this.message = message;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getStacktrace() {
        return stacktrace;
    }
}

