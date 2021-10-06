package com.shorten.dolly.controller;

import com.shorten.dolly.api.BusinessException;
import com.shorten.dolly.biz.URLManager;
import com.shorten.dolly.model.URL;
import com.shorten.dolly.repository.URLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
public class ShortenController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortenController.class);

    @Autowired
    private URLRepository repository;

    @Autowired
    private URLManager manager;

    @GetMapping(value = "/url/{hash}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getLongURL(@PathVariable(value = "hash") String hash) {
        try {
            Optional<URL> url = repository.findById(hash);
            if (url != null && url.isPresent()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.LOCATION, url.get().getLongURL());
                return new ResponseEntity<>(null, headers, HttpStatus.FOUND);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/url", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createShortURL(@RequestBody String longURL) {
        try {
            String hash = manager.createShortURL(longURL);
            URI location = URI.create("/url/" + hash);
            return ResponseEntity.created(location).build();
        } catch (BusinessException e) {
            LOGGER.error("Validation failed with error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
