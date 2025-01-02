package com.example.d308.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatValidator implements Validator<String> {
    private final String dateFormat;
    private String errorMessage;

    public DateFormatValidator(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public boolean isValid(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            errorMessage = "Date must follow the format: " + dateFormat;
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}