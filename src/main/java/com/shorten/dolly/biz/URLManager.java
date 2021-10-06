package com.shorten.dolly.biz;

import com.shorten.dolly.api.BusinessException;
import com.shorten.dolly.config.IAppConfig;
import com.shorten.dolly.model.URL;
import com.shorten.dolly.repository.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class URLManager {
    @Autowired
    private URLRepository repository;

    public String createShortURL(String longURL) throws BusinessException {
        if (longURL.isEmpty()) return null;

        byte[] shortURLBytes = new byte[7];

        try {
            // MD5 Hash
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(longURL.getBytes());
            byte[] digest = md.digest();

            // Base64 encoding
            byte[] encoding = Base64.getEncoder().withoutPadding().encode(digest);

            // There 21 bytes in Base64 decoding of MD5 Hash
            // We choose bytes in groups of 7
            for (int i = 0; i < 7; i++) {
                shortURLBytes[i] = encoding[i];
            }

            String hash = new String(shortURLBytes, StandardCharsets.UTF_8);
            URL url = new URL(hash, longURL);

            // Attempt write to DB
            repository.save(url);

            return hash;
        } catch (Exception e) {
            throw new BusinessException("Failed to create short url: " + e.getMessage());
        }
    }
}
