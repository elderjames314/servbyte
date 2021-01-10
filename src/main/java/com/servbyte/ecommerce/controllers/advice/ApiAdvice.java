package com.servbyte.ecommerce.controllers.advice;

import com.servbyte.ecommerce.dtos.response.ApiResponse;
import com.servbyte.ecommerce.enums.ApiErrorCodes;
import com.servbyte.ecommerce.exceptions.BadRequestException;
import com.servbyte.ecommerce.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;


@SuppressWarnings (value="unchecked")
@ControllerAdvice(annotations = {RestController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
@ResponseBody
public class ApiAdvice {

    private Logger logger = LoggerFactory.getLogger(getClass());

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



