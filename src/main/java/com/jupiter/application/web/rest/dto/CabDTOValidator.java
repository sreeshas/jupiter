package com.jupiter.application.web.rest.dto;

import com.jupiter.application.domain.Cab;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by sreenidhisreesha on 10/22/14.
 */
public class CabDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CabDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "latitude", "latitude field cannot be empty");
        ValidationUtils.rejectIfEmpty(errors, "longitude", "longitude field cannot be empty");
        CabDTO cabDTO = (CabDTO) o;
        if ( cabDTO.getLatitude() < 0 ) {
            errors.rejectValue("latitude", "cannot be negative");
        }
    }
}
