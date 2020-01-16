package com.bosssoft.pay.sdk.core.internal.util;

import com.bosssoft.pay.sdk.core.ThirdpayConstants;
import com.bosssoft.pay.sdk.core.exception.ThirdpayApiException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Title 对称秘钥工具类
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ThirdpayEncrypt {

    // 加密算法
    private static final String AES_ALG = "AES";

    // 算法/模式/补码方式
    private static final String AES_CBC_PCK_ALG = "AES/CBC/PKCS5Padding";

    // 向量(使用CBC模式时, 需要一个向量, 其可增加加密算法的强度)
    private static final byte[] AES_IV = initIv("AES/CBC/PKCS5Padding");

    /**
     * 生成对称秘钥
     * @param encryptType
     * @return
     * @throws ThirdpayApiException
     */
    public static String generateKey(String encryptType) throws ThirdpayApiException {
        if (ThirdpayConstants.ENCRYPT_TYPE_AES.equals(encryptType)) {
            return AesUtil.generateKey();
        }
        throw new ThirdpayApiException("does not support encrypt type:" + encryptType);
    }

    /**
     * 加密
     * @param content
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return
     * @throws ThirdpayApiException
     */
    public static String encrypt(String content, String encryptType, String encryptKey, String charset) throws ThirdpayApiException {
        if(AES_ALG.equals(encryptType)) {
            return aesEncrypt(content, encryptKey, charset);
        } else {
            throw new ThirdpayApiException("does not support encrypeType, encrypeType=" + encryptType);
        }
    }

    /**
     * 解密
     * @param content
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return
     * @throws ThirdpayApiException
     */
    public static String decrypt(String content, String encryptType, String encryptKey, String charset) throws ThirdpayApiException {
        if(AES_ALG.equals(encryptType)) {
            return aesDecrypt(content, encryptKey, charset);
        } else {
            throw new ThirdpayApiException("does not support encrypeType, encrypeType=" + encryptType);
        }
    }

    /**
     * aes加密
     * @param content
     * @param aesKey
     * @param charset
     * @return
     * @throws ThirdpayApiException
     */
    private static String aesEncrypt(String content, String aesKey, String charset) throws ThirdpayApiException {
        try {
            Cipher e = Cipher.getInstance(AES_CBC_PCK_ALG);
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            e.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(aesKey.getBytes()), AES_ALG), iv);
            byte[] encryptBytes = e.doFinal(content.getBytes(charset));
            return new String(Base64.encodeBase64(encryptBytes));
        } catch (Exception var6) {
            throw new ThirdpayApiException("aes encrypt error, aesContent = " + content + "; charset = " + charset, var6);
        }
    }

    /**
     * aes解密
     * @param content
     * @param key
     * @param charset
     * @return
     * @throws ThirdpayApiException
     */
    private static String aesDecrypt(String content, String key, String charset) throws ThirdpayApiException {
        try {
            Cipher e = Cipher.getInstance(AES_CBC_PCK_ALG);
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            e.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(key.getBytes()), AES_ALG), iv);
            byte[] cleanBytes = e.doFinal(Base64.decodeBase64(content.getBytes()));
            return new String(cleanBytes, charset);
        } catch (Exception var6) {
            throw new ThirdpayApiException("aes decrypt error, aesContent = " + content + "; charset = " + charset, var6);
        }
    }

    /**
     * 初始化向量
     * @param fullAlg
     * @return
     */
    private static byte[] initIv(String fullAlg) {
        byte[] iv;
        int i;
        try {
            Cipher e = Cipher.getInstance(fullAlg);
            int var6 = e.getBlockSize();
            iv = new byte[var6];
            for(i = 0; i < var6; ++i) {
                iv[i] = 0;
            }
            return iv;
        } catch (Exception var5) {
            byte blockSize = 16;
            iv = new byte[blockSize];
            for(i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;
        }
    }

//    public static void main(String[] args) throws Exception {
//        // 需要加密的字串
//        String cSrc = "{data:[{'name':'你好','age':20},{'name':'zd','age':18}]}";
//        System.out.println("需要加密的数据：" + cSrc);
//
//        String aesKey = AesUtil.generateKeyWith128();
//        System.out.println("随机生成的128位的密钥(base64)：" + aesKey);
//
//        // 加密
//        long startTime = System.currentTimeMillis();
//        String encryptedContent = aesEncrypt(cSrc, aesKey, "UTF-8");
//        long costTime = System.currentTimeMillis() - startTime;
//        System.out.println("加密后的数据是：" + encryptedContent);
//        System.out.println("加密耗时：" + costTime + "毫秒");
//
//        // 解密
//        startTime = System.currentTimeMillis();
//        String decryptContent = aesDecrypt(encryptedContent, aesKey, "UTF-8");
//        costTime = System.currentTimeMillis() - startTime;
//        System.out.println("解密后的数据是：" + decryptContent);
//        System.out.println("解密耗时：" + costTime + "毫秒");
//    }
}
