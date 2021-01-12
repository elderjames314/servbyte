package com.servbyte.ecommerce.controllers.advice;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.servbyte.ecommerce.dtos.Error;
import com.servbyte.ecommerce.dtos.response.ApiResponse;
import com.servbyte.ecommerce.enums.ApiErrorCodes;
import com.servbyte.ecommerce.exceptions.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@SuppressWarnings (value="unchecked")
@ControllerAdvice(annotations = {RestController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
@ResponseBody
public class ApiAdvice {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
       // logger.error("{}", e.getLocalizedMessage());
        ApiResponse response = new ApiResponse();
        response.setCode(ApiErrorCodes.INVALID_REQUEST.getKey());
        response.setMessage(messageSource.getMessage("request.invalid", null, Locale.ENGLISH));
        Throwable cause = e.getMostSpecificCause();
        List<Error> errors = new ArrayList<>();
        if (cause instanceof InvalidFormatException ||
                cause instanceof InvalidEnumException) {
            Throwable t = ExceptionUtil.get(e, JsonMappingException.class);
            if (t != null) {
                List<JsonMappingException.Reference> reference = ((JsonMappingException) t).getPath();
                StringBuilder builder = new StringBuilder(reference.get(0).getFieldName());
                if (reference.size() > 1) {
                    reference
                            .subList(1, reference.size())
                            .forEach(r -> {
                                builder.append("[");
                                if (StringUtils.isNotBlank(r.getFieldName())){
                                    builder.append(r.getFieldName());
                                }});
                    builder.append(StringUtils.repeat("]", reference.size() - 1));
                }
                String field = builder.toString();
                Object value = cause instanceof InvalidFormatException ?
                        ((InvalidFormatException) cause).getValue() :
                        ((InvalidEnumException) cause).getRejected();
                Class type = cause instanceof InvalidFormatException ?
                        ((InvalidFormatException) cause).getTargetType() :
                        ((InvalidEnumException) cause).getEnumClass();
                String message;
                if (type.isEnum()) {
                    message = messageSource.getMessage("typeMismatch.java.lang.Enum",
                            new Object[]{value, type.getSimpleName(),
                                    StringUtils.join(EnumUtil.getEnumConstants(type), ", ")}, Locale.ENGLISH);
                } else {
                    message = messageSource.getMessage("typeMismatch", new Object[]{value}, Locale.ENGLISH);
                }
                errors.add(new Error(field, message));
            }
        } else if (cause instanceof PropertyBindingException) {
            PropertyBindingException ex = (PropertyBindingException) cause;
            String field = ex.getPropertyName();
            String expected = StringUtils.join(ex.getKnownPropertyIds(), ", ");
            String error = messageSource.getMessage("field.invalid", new Object[]{expected}, Locale.ENGLISH);
            errors.add(new Error(field, error));
        }
        response.setErrorsList(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException e) {
        ApiResponse response = new ApiResponse();
        response.setCode(e.getCode());
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(NotFoundException e) {
        ApiResponse response = new ApiResponse();
        response.setCode(e.getCode());
        response.setMessage(e.getMessage());
        //return response;
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        logger.error(String.format("error %s", e.getLocalizedMessage()), e);
        String message = e.getMessage();
        ApiResponse response = new ApiResponse();
        response.setCode(ApiErrorCodes.ACCESS_DENIED.getKey());
        response.setMessage(message);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}



