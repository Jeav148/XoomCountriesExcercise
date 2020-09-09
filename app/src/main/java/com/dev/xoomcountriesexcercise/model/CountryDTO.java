package com.dev.xoomcountriesexcercise.model;

public class CountryDTO {

    private final String code;
    private final String name;

    public CountryDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
