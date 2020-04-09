package com.webshoprest.api.v1.util;

import com.webshoprest.api.v1.exceptions.EntityValidationException;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

import static com.webshoprest.api.v1.util.FieldNamePrettyFormatter.prettyFormatFieldName;

public class ValidationErrorInspector {

    public static void inspect(Errors errors){
        if(errors.getErrorCount() > 0){
            Map<String, String> errorsMap = new HashMap<>();

            errors.getFieldErrors()
                    .forEach(e -> {
                        errorsMap.put(prettyFormatFieldName(e.getField()), e.getDefaultMessage());
                    });
            throw new EntityValidationException(errorsMap);
        }
    }

}
