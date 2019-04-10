package com.davidgjm.oss.wechat.crypto;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

@Component
public class DigestUtil implements Function<String, String> {
    private final MessageDigest digest;

    public DigestUtil(String algorithm) {
        try {
            this.digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public DigestUtil() {
        this("SHA-256");
    }


    /**
     * Algorithm: SHA-1
     */
    public static DigestUtil HASH_SHA1 = new DigestUtil("SHA-1");

    /**
     * Algorithm: SHA-256
     */
    public static DigestUtil HASH_SHA256 = new DigestUtil();

    /**
     * Algorithm: SHA-512
     */
    public static DigestUtil HASH_SHA512 = new DigestUtil("SHA-512");

    /**
     * Algorithm: MD5
     */
    public static DigestUtil HASH_MD5 = new DigestUtil("MD5");


    @Override
    public String apply(String input) {
        return hash(input);
    }

    public String hash(String input) {
        byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return toHex(encodedHash);
    }

    private static String toHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte aHash : hash) {
            String hex = Integer.toHexString(0xff & aHash);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
