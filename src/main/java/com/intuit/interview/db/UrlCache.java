package com.intuit.interview.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by venkatamunnangi on 11/12/19.
 */
@Repository
public class UrlCache {
    private final int MAX_ATTEMPTS = 10;
    private final Jedis jedis;
    private final String idKey;
    private final String urlKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlCache.class);

    public UrlCache() {
        this.jedis = new Jedis();
        this.idKey = "id";
        this.urlKey = "url:";
    }

    public UrlCache(Jedis jedis, String idKey, String urlKey, Map<String, Object> urlData) {
        this.jedis = jedis;
        this.idKey = idKey;
        this.urlKey = urlKey;
    }

    public Long incrementID() {
        Long id = jedis.incr(idKey);
        LOGGER.info("ID incremented to: {}", id-1);
        return id - 1;
    }

    public void saveUrl(String key, String longUrl) {
        LOGGER.info("Saving: {} at {}", longUrl, key);

        Map<String, String> data = new HashMap<>();
        data.put("key", longUrl);
        data.put("longUrl", longUrl);
        data.put("count", String.valueOf(0));

        jedis.hmset(key, data);
    }

    public String getUrl(Long id) throws Exception {
        LOGGER.info("Retrieving longUrl stored at {}", id);

        String key = "url:"+id;
        String longUrl = jedis.hget(key, "longUrl");
        String urlCountString = jedis.hget(urlKey+id, "count");
        int urlCount = Integer.parseInt(urlCountString);

        if(urlCount > MAX_ATTEMPTS) {
            throw new Exception("URL key for " + id + " is invalid as it exceeded 10 attempts.");
        }

        urlCount++;
        jedis.hset(key, "count", String.valueOf(urlCount));

        LOGGER.info("Retrieved longUrl stored at %s %s", longUrl ,id);
        if (longUrl == null) {
            throw new Exception("URL key for" + id + " does not exist");
        }
        return longUrl;
    }
}
