package com.example.d308.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateRangeValidator implements Validator<String[]> {
    private final String dateFormat;
    private String errorMessage;

    public DateRangeValidator(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public boolean isValid(String[] dates) {
        if (dates.length != 2) {
            errorMessage = "Invalid input: Start and end dates required.";
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        try {
            Date startDate = sdf.parse(dates[0]);
            Date endDate = sdf.parse(dates[1]);
            if (endDate.after(startDate)) {
                return true;
            } else {
                errorMessage = "End date must be after start date.";
                return false;
            }
        } catch (ParseException e) {
            errorMessage = "Dates must follow the format: " + dateFormat;
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}