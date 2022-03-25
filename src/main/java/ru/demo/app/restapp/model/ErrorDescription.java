package ru.demo.app.restapp.model;

import lombok.Data;

@Data
public class ErrorDescription {
    private final String field;
    private final String error;
}