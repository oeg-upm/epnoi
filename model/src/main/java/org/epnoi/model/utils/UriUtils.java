package org.epnoi.model.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by cbadenes on 29/02/16.
 */
public class UriUtils {

    private static final Logger LOG = LoggerFactory.getLogger(UriUtils.class);

    public static String getMD5(String text){
        String id;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(text.getBytes(),0,text.length());
            id = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            id = UUID.randomUUID().toString()
            LOG.warn("Error calculating MD5 from text. UUID will be used: " + id);
        }
        return id;
    }
}
