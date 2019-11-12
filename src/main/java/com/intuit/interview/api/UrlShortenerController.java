package com.intuit.interview.api;

import com.intuit.interview.model.UrlShortenRequest;
import com.intuit.interview.service.UrlShortenerService;
import com.intuit.interview.util.UrlChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by venkatamunnangi on 11/12/19.
 */
@RestController
public class UrlShortenerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlShortenerController.class);
    private final UrlShortenerService urlConverterService;

    public UrlShortenerController(UrlShortenerService urlConverterService) {
        this.urlConverterService = urlConverterService;
    }

    @RequestMapping(value = "/shortener", method= RequestMethod.POST, consumes = {"application/json"})
    public String shortenUrl(@RequestBody @Valid final UrlShortenRequest urlShortenRequest, HttpServletRequest request) throws Exception {
        LOGGER.info(String.format("Received url to shorten: %s", urlShortenRequest.getUrl()));
        String longUrl = urlShortenRequest.getUrl();
        if (UrlChecker.INSTANCE.validateURL(longUrl)) {
            String localURL = request.getRequestURL().toString();
            String shortenedUrl = urlConverterService.generateShortenUrl(localURL, urlShortenRequest.getUrl());
            LOGGER.info(String.format("Url shortened to: %s", shortenedUrl));
            return shortenedUrl;
        }
        throw new Exception("Please enter a valid URL");
    }

    @RequestMapping(value = "/{id}", method=RequestMethod.GET)
    public RedirectView redirectUrl(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException, Exception {
        LOGGER.info("Received shortened url to redirect: " + id);
        String redirectUrlString = urlConverterService.retrieveLongURLFromID(id);
        LOGGER.info("Original URL: " + redirectUrlString);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://" + redirectUrlString);
        return redirectView;
    }
}


