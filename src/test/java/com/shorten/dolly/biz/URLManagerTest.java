package com.shorten.dolly.biz;

import com.shorten.dolly.api.BusinessException;
import com.shorten.dolly.config.IAppConfig;
import com.shorten.dolly.model.URL;
import com.shorten.dolly.repository.URLRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class URLManagerTest {
    @Mock
    private URLRepository repository;

    @InjectMocks
    private URLManager manager = new URLManager();

    @Test
    public void testCreateShortURLReturnsCorrectURL() throws BusinessException {
        // Setup
        URL mockURL = Mockito.mock(URL.class);
        String longURL = "https://www.reuters.com/article/urnidgns002570f3005978d8002576f60035a6bb/long-url-please-idUS98192761820100330";

        // Test
        when(repository.save(mockURL)).thenReturn(null);
        String result = null;
        try {
            result = manager.createShortURL(longURL);
        } catch (BusinessException e) {
           throw e;
        }

        // Verify
        String expected = "Tv7Rio9";
        assertEquals(expected, result);
    }
}
