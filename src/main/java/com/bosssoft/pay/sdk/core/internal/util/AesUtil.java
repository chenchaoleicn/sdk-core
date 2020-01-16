package com.bosssoft.pay.sdk.core.internal.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Title aes工具类
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class AesUtil {

    private static final String AES_ALG = "AES";

    /**
     * 生成AES密钥
     * @return
     */
    public static String generateKey() {
        return generateKeyWith128();
    }

    /**
     * 生成AES密钥
     * @param length    仅支持128, 192或256
     * @param seed
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String generateKey(int length, String seed) {
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(AES_ALG);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        if (seed == null) {
            kg.init(length);
        } else {
            // SecureRandom是生成安全随机数序列, seed.getBytes()是种子, 只要种子相同, 序列就一样, 生成的秘钥就一样
            kg.init(length, new SecureRandom(seed.getBytes()));
        }
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        return Base64.encodeBase64String(b);
    }

    /**
     * 随机生成128位的密钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String generateKeyWith128() {
        return generateKey(128, null);
    }

    /**
     * 随机生成192位的密钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String generateKeyWith192() {
        return generateKey(192, null);
    }

    /**
     * 随机生成256位的密钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String generateKeyWith256() {
        return generateKey(256, null);
    }
}
