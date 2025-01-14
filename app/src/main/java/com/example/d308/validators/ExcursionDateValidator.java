package com.example.d308.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExcursionDateValidator {

    private final String dateFormat;

    public ExcursionDateValidator(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isValid(String vacationStartDate, String vacationEndDate, String excursionDate) {
        if (vacationStartDate == null || vacationEndDate == null || excursionDate == null ||
                vacationStartDate.isEmpty() || vacationEndDate.isEmpty() || excursionDate.isEmpty()) {
            return false;
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.US);

        try {
            Date startDate = dateFormatter.parse(vacationStartDate);
            Date endDate = dateFormatter.parse(vacationEndDate);
            Date exDate = dateFormatter.parse(excursionDate);

            if (startDate != null && endDate != null && exDate != null) {
                return !exDate.before(startDate) && !exDate.after(endDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    public String getErrorMessage() {
        return "Excursion date has to be between vacation start and end date.";
    }
}