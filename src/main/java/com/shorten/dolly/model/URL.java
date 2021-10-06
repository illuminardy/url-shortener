package com.shorten.dolly.model;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class URL {
    @Id
    private String hash;
    private String longURL;

    public URL(String hash, String longURL) {
        this.hash = hash;
        this.longURL = longURL;
    }
}
