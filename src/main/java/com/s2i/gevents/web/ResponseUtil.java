package com.s2i.gevents.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public interface ResponseUtil {
    static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, (HttpHeaders)null);
    }

    static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
        return (ResponseEntity)maybeResponse.map((response) -> {
            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(header)).body(response);
        }).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
    }

    static <X> ResponseEntity<X> created(String uri, X request) throws URISyntaxException {
        return ResponseEntity.created(new URI(uri)).body(request);
    }

    static <X> ResponseEntity<X> badRequest(X request) throws URISyntaxException {
        return ResponseEntity.badRequest().body(request);
    }
}