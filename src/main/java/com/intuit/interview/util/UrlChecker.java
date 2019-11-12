package com.intuit.interview.util;


import org.apache.commons.validator.routines.UrlValidator;

/**
 * Created by venkatamunnangi on 11/12/19.
 */
public class UrlChecker {
    public static final UrlChecker INSTANCE = new UrlChecker();
    private UrlValidator urlValidator;
    private String[] schemes = {"http"};
    private String TEST_SCHEME = "localhost:8080";

    private UrlChecker() {
        urlValidator = new UrlValidator(schemes);
    }

    public boolean validateURL(String url) {
        String finalUrl = "";
        if(!url.startsWith("http")) {
            finalUrl = "http://"+url;
        }

        return finalUrl.contains(TEST_SCHEME) || urlValidator.isValid(finalUrl);
    }
}
