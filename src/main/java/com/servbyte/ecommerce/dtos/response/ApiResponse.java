package com.servbyte.ecommerce.dtos.response;

import com.servbyte.ecommerce.dtos.Error;

import java.util.Arrays;
import java.util.List;

public class ApiResponse {
        private String code;
        private String message;
        private List<Error> errorsList;
        private List<String> errors;

        public ApiResponse(String code, String message, List<String> errors) {
            this.code = code;
            this.message = message;
            this.errors = errors;
        }

        public ApiResponse(String code, String message, String error) {
            this.code = code;
            this.message = message;
            this.errors = Arrays.asList(error);
        }

        public ApiResponse() {
        }


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }

        public List<Error> getErrorsList() {
            return errorsList;
        }

        public void setErrorsList(List<Error> errorsList) {
            this.errorsList = errorsList;
        }
}
