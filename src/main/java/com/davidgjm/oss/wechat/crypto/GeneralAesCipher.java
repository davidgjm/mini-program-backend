package com.davidgjm.oss.wechat.crypto;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

@Slf4j
public final class GeneralAesCipher {
    private final Cipher cipher;
    private SecretKeySpec secretKey;
    private AlgorithmParameterSpec parameterSpec;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public GeneralAesCipher(Algorithm algorithm) {
        try {
            this.cipher = Cipher.getInstance(algorithm.getAlgorithm(), "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public GeneralAesCipher() {
        this(Algorithm.CBC_PKCS5);
    }

    public void init(byte[] secretKey, byte[] iv) {
        if (secretKey == null || secretKey.length == 0) {
            throw new IllegalArgumentException("Secret key is required!");
        }
        if (iv == null || iv.length == 0) {
            throw new IllegalArgumentException("IV is required!");
        }

        this.secretKey = new SecretKeySpec(secretKey, "AES");
        this.parameterSpec = new IvParameterSpec(iv);
    }

    private void initCipher(boolean isEncryptionMode) {
        int mode = isEncryptionMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        try {
            cipher.init(mode, this.secretKey, this.parameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private synchronized byte[] doCrypt(byte[] input, boolean isEncryptionMode) {
        if (input == null || input.length == 0) {
            throw new IllegalArgumentException("The input data cannot be null or empty.");
        }
        initCipher(isEncryptionMode);
        try {
            return cipher.doFinal(input);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("Input byte sequence length {}", input.length);
            log.error("Cryptography error", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public byte[] encrypt(byte[] input) {
        return doCrypt(input, true);
    }

    public byte[] decrypt(byte[] input) {
        return doCrypt(input, false);
    }

    public String getAesAlgorithm() {
        return this.cipher.getAlgorithm();
    }

    public SecretKeySpec getSecretKey() {
        return secretKey;
    }

    public AlgorithmParameterSpec getParameterSpec() {
        return parameterSpec;
    }

    public enum Algorithm {
        CBC_PKCS5("AES/CBC/PKCS5Padding"),

        CBC_PKCS7("AES/CBC/PKCS7Padding"),;
        private String algorithm;

        Algorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return algorithm;
        }
    }
}
