package com.shorten.dolly.controller;

import com.shorten.dolly.biz.URLManager;
import com.shorten.dolly.config.IAppConfig;
import com.shorten.dolly.model.URL;
import com.shorten.dolly.repository.URLRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;

@WebMvcTest(ShortenController.class)
public class ShortenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private URLRepository repository;

    @MockBean
    private URLManager manager;

    @Test
    public void getLong_ShouldRedirectToLongUrl() throws Exception {
        // Setup
        URL url = new URL("mockHash", "mocklong");

        when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(url));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/url/mockHash"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        String mockLong = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals(url.getLongURL(), mockLong);
    }

    @Test
    public void getLong_ShouldReturnNotFound() throws Exception {
        when(repository.findById(Mockito.anyString())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/url/mockHash"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void createShortURL_ReturnsCorrectLocationHeader() throws Exception {
        String expectedLocation = "/url/NGVmZWQ";
        when(manager.createShortURL(Mockito.anyString())).thenReturn("NGVmZWQ");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/url")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content("https://www.reuters.com/article/urnidgns002570f3005978d8002576f60035a6bb/long-url-please-idUS98192761820100330"))
                .andExpect(status().isCreated())
                .andReturn();

        String locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals(expectedLocation, locationHeader);
    }
}
