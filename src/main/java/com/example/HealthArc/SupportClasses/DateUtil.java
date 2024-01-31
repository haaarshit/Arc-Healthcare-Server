package com.example.HealthArc.SupportClasses;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public  Date convertStringToDate(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }
}
