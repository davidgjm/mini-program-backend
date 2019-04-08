package com.davidgjm.oss.wechat.sandbox;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;

@Ignore
public class AESSandbox {
    private Cipher cipher;

    String rawSessionKey = "tybk/nn13v44Vx0qtHlONA==";
    SecretKeySpec secretKey;

    @BeforeClass
    public static void startup() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Before
    public void setUp() throws Exception {
        byte[] sessionKey = Base64Utils.decodeFromString(rawSessionKey);
        secretKey = new SecretKeySpec(sessionKey, "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    @Test
    public void testDecryptPhone() throws Exception {
        String rawIv = "5ROylZl3cvqqAZG01/FSpw==";
        byte[] iv = Base64Utils.decodeFromString(rawIv);

        String rawEncryptedData = "xMINPHs9kzgej6WOAnTP2qmjm2QmcOzpo6ZP+VnxU6A5esHlZUkXTGSRbxLbssVtaE6nklFo/36F44fTZpAzKNgICZMjiRg/GkpW3FKxE5sgofl8C3tgZENhZKMcLa00aIZX5Opxutube7DSI58a2I01fqg8+bfzcRgbOtB7lhPhIYJuR35Ek7eq+vXn9bMCOZiRX53n1inU7PyXPMixMA==";
        byte[] encryptedData = Base64Utils.decodeFromString(rawEncryptedData);


        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

        byte[] decrypted = cipher.doFinal(encryptedData);
        String decryptedStr = new String(decrypted, StandardCharsets.UTF_8);

        System.out.println(decryptedStr);
    }

    @Test
    public void testDecryptPhoneBC() throws Exception {
        String rawIv = "5ROylZl3cvqqAZG01/FSpw==";
        byte[] iv = Base64Utils.decodeFromString(rawIv);

        String rawEncryptedData = "xMINPHs9kzgej6WOAnTP2qmjm2QmcOzpo6ZP+VnxU6A5esHlZUkXTGSRbxLbssVtaE6nklFo/36F44fTZpAzKNgICZMjiRg/GkpW3FKxE5sgofl8C3tgZENhZKMcLa00aIZX5Opxutube7DSI58a2I01fqg8+bfzcRgbOtB7lhPhIYJuR35Ek7eq+vXn9bMCOZiRX53n1inU7PyXPMixMA==";
        byte[] encryptedData = Base64Utils.decodeFromString(rawEncryptedData);

        Cipher bcCipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

        IvParameterSpec ivspec = new IvParameterSpec(iv);
        bcCipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

        byte[] decrypted = bcCipher.doFinal(encryptedData);
        String decryptedStr = new String(decrypted, StandardCharsets.UTF_8);

        System.out.println(decryptedStr);
    }


}
