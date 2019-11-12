package com.intuit.interview.service;

import com.intuit.interview.db.UrlCache;
import com.intuit.interview.util.UrlShortenerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by venkatamunnangi on 11/12/19.
 */
@Service
public class UrlShortenerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlShortenerService.class);

    private final UrlCache urlRepository;

    @Autowired
    public UrlShortenerService(UrlCache urlRepository) {
        this.urlRepository = urlRepository;
    }


    public String generateShortenUrl(String localUrl, String longUrl) {
        LOGGER.info("Shortening {}", longUrl);
        Long id = urlRepository.incrementID();
        String uniqueID = UrlShortenerUtil.createUniqueID(id);
        urlRepository.saveUrl("url:"+id, longUrl);
        String baseString = formatLocalURLFromShortener(localUrl);
        return baseString + uniqueID;
    }

    public String retrieveLongURLFromID(String uniqueID) throws Exception {
        Long key = UrlShortenerUtil.INSTANCE.retrieveKeyFromId(uniqueID);
        String longUrl = urlRepository.getUrl(key);
        LOGGER.info("Converting shortened URL back to {}", longUrl);
        return longUrl;
    }

    private String formatLocalURLFromShortener(String localURL) {
        String[] addressComponents = localURL.split("/");
        // remove the endpoint (last index)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < addressComponents.length - 1; ++i) {
            sb.append(addressComponents[i]);
        }
        sb.append('/');
        return sb.toString();
    }

}
