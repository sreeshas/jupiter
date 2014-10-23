package com.jupiter.application.web.rest.dto;

import com.jupiter.application.domain.Cab;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sreenidhisreesha on 10/22/14.
 */
public class CabDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CabDTO.class.equals(aClass);
    }


    /**
     * Matches a latitude in the range of -90 to 90 degrees, with between 1 and 6 trailing decimal places.
     *
     * Matches
     * -90.0 -77.284382 89.999999 1.0001
     * Non-Matches
     * -90.1 90.12345 91 -20.1234567 -90 90
     * @author Nick Floersch
     */
    String latitudeRegEx = "^-?([1-8]?[0-9]\\.{1}\\d{1,6}$|90\\.{1}0{1,6}$)";
    Pattern latPattern = Pattern.compile(latitudeRegEx);


    /**
     * Matches a longitude in the range of -180 to 180 degrees, with between 1 and 6 trailing decimal places.
     *
     * Matches
     * -180.0 180.0 -179.010293 1.123456 -45.012 0.12
     * Non-Matches
     * 180 -180 180.1 -180.1 0.1234567 190.1
     * @author Nick Floersch
     */

    String longitudeRegEx = "^-?((([1]?[0-7][0-9]|[1-9]?[0-9])\\.{1}\\d{1,6}$)|[1]?[1-8][0]\\.{1}0{1,6}$)";
    Pattern longPattern = Pattern.compile(longitudeRegEx);

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "latitude", "latitude field cannot be empty");
        ValidationUtils.rejectIfEmpty(errors, "longitude", "longitude field cannot be empty");

        CabDTO cabDTO = (CabDTO) o;
        // Validate  latitude.
        float latitude = cabDTO.getLatitude();
        Matcher latMatcher = latPattern.matcher(String.valueOf(cabDTO.getLatitude()));
        if (!latMatcher.matches()) {
            errors.rejectValue("latitude", "Invalid latitude value");
        }
        // Validate longitude
        float longitude = cabDTO.getLongitude();
        Matcher longMatcher = longPattern.matcher(String.valueOf(cabDTO.getLongitude()));
        if (!longMatcher.matches()) {
            errors.rejectValue("longitude", "Invalid longitude value");
        }

    }
}
