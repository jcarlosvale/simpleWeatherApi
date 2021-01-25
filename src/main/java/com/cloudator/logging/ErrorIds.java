package com.cloudator.logging;

public enum ErrorIds {

    WEATHER_API_JSON_READER_MSG("Error reading JSON Locations To Monitor [filename = %s]: %s"),
    ;

    private final String message;

    ErrorIds(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
