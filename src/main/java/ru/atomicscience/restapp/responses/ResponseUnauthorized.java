package ru.atomicscience.restapp.responses;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUnauthorized extends ResponseEntity<Object> {
    private static HttpHeaders responseHeaders = new HttpHeaders();

    static {
        responseHeaders.add("WWW-Authenticate",
                "Basic realm='Access to the payment operations', charset='UTF-8'");
    }


    public ResponseUnauthorized() {
        super(responseHeaders, HttpStatus.UNAUTHORIZED);
    }
}
