package com.bosssoft.pay.sdk.core.internal.util;

import com.bosssoft.pay.sdk.core.ThirdpayConstants;
import com.bosssoft.pay.sdk.core.exception.ThirdpayApiException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title 非对称秘钥工具类
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ThirdpaySignature {

    /**
     * 公钥体系中支持的(自定义的)秘钥类型
     */
    private static List publicKeyTypes = new ArrayList<String>();

    /**
     * (公钥体系中支持的自定义的)秘钥类型到秘钥长度的映射
     */
    private static Map<String, Integer> publicKeyType2LengthMap = new HashMap<String, Integer>();

    static {
        // 2048位的秘钥长度(即模值的长度)已够用, 必要时可再次扩展
        publicKeyType2LengthMap.put(ThirdpayConstants.RSA_TYPE_V1, 1024);
        publicKeyType2LengthMap.put(ThirdpayConstants.RSA_TYPE_V2, 2048);

        // 初始化秘钥类型
        publicKeyTypes.addAll(publicKeyType2LengthMap.keySet());
    }

    /**
     * 是否属于公钥体系中的(自定义的)秘钥类型
     * @param keyType
     * @return
     */
    public static boolean isBelongToPublicKeyInfrastructure(String keyType) {
        return publicKeyTypes.contains(keyType);
    }

    /**
     * 签名
     * @param content
     * @param privateKey
     * @param charset
     * @param signType
     * @return
     * @throws ThirdpayApiException
     */
    public static String sign(String content, String privateKey, String charset, String signType) throws ThirdpayApiException {
        if (ThirdpayConstants.SIGN_TYPE_RSA_V1.equals(signType)) {
            return rsaSign(content, privateKey, charset);
        } else if (ThirdpayConstants.SIGN_TYPE_RSA_V2.equals(signType)) {
            return rsa256Sign(content, privateKey, charset);
        } else {
            throw new ThirdpayApiException("does not support sign type, signType=" + signType);
        }
    }

    /**
     * rsa签名(使用SHA1WithRSA算法)
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws ThirdpayApiException
     */
    public static String rsaSign(String content, String privateKey, String charset) throws ThirdpayApiException {
        try {
            PrivateKey e = getPrivateKeyFromPKCS8(ThirdpayConstants.ALGORITHM_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance(ThirdpayConstants.SIGN_SHA1RSA_ALGORITHMS);
            signature.initSign(e);
            signature.update(content.getBytes(charset));
            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (InvalidKeySpecException e) {
            throw new ThirdpayApiException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", e);
        } catch (Exception e) {
            throw new ThirdpayApiException("rsa sign error, content = " + content + "; charset = " + charset, e);
        }
    }

    /**
     * rsa签名(使用SHA256WithRSA算法)
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws ThirdpayApiException
     */
    public static String rsa256Sign(String content, String privateKey, String charset) throws ThirdpayApiException {
        try {
            PrivateKey key = getPrivateKeyFromPKCS8(ThirdpayConstants.ALGORITHM_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance(ThirdpayConstants.SIGN_SHA256RSA_ALGORITHMS);
            signature.initSign(key);
            signature.update(content.getBytes(charset));
            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (Exception e) {
            throw new ThirdpayApiException("rsa2 sign error, content = " + content + "; charset = " + charset, e);
        }
    }

    /**
     * ras验签
     * @param content
     * @param sign
     * @param signType
     * @param publicKey
     * @param charset
     * @return
     * @throws ThirdpayApiException
     */
    public static boolean rsaCheck(String content, String sign, String signType, String publicKey, String charset) throws ThirdpayApiException {
        if (ThirdpayConstants.SIGN_TYPE_RSA_V1.equals(signType)) {
            return rsaCheckContent(content, sign, publicKey, charset);
        } else if (ThirdpayConstants.SIGN_TYPE_RSA_V2.equals(signType)) {
            return rsa256CheckContent(content, sign, publicKey, charset);
        } else {
            throw new ThirdpayApiException("Sign Type is Not Support : signType=" + signType);
        }
    }

    /**
     * rsa验签(算法为SHA1WithRSA)
     * @param content
     * @param sign
     * @param publicKey
     * @param charset
     * @return
     * @throws ThirdpayApiException
     */
    private static boolean rsaCheckContent(String content, String sign, String publicKey, String charset) throws ThirdpayApiException {
        try {
            PublicKey key = getPublicKeyFromX509(ThirdpayConstants.ALGORITHM_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));
            Signature signature = Signature.getInstance(ThirdpayConstants.SIGN_SHA1RSA_ALGORITHMS);
            signature.initVerify(key);
            signature.update(content.getBytes(charset));
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            throw new ThirdpayApiException("rsa check sign error! content = " + content + ",sign=" + sign + ",charset = " + charset, e);
        }
    }

    /**
     * rsa验签(算法为SHA256WithRSA)
     * @param content
     * @param sign
     * @param publicKey
     * @param charset
     * @return
     * @throws ThirdpayApiException
     */
    private static boolean rsa256CheckContent(String content, String sign, String publicKey, String charset) throws ThirdpayApiException {
        try {
            PublicKey key = getPublicKeyFromX509(ThirdpayConstants.ALGORITHM_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));
            Signature signature = Signature.getInstance(ThirdpayConstants.SIGN_SHA256RSA_ALGORITHMS);
            signature.initVerify(key);
            signature.update(content.getBytes(charset));
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            throw new ThirdpayApiException("rsa2 check sign error! content = " + content + ",sign=" + sign + ",charset = " + charset, e);
        }
    }

    /**
     * RSA加密
     * @param content
     * @param publicKey
     * @param charset
     * @param rsaType
     * @return
     * @throws ThirdpayApiException
     */
    public static String rsaEncrypt(String content, String publicKey, String charset, String rsaType) throws ThirdpayApiException {
        try {
            PublicKey e = getPublicKeyFromX509(ThirdpayConstants.ALGORITHM_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));
            Cipher cipher = Cipher.getInstance(ThirdpayConstants.ALGORITHM_TYPE_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, e);
            byte[] data = content.getBytes(charset);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            final int MAX_ENCRYPT_BLOCK_OF_RSA = getMaxEncryptBlockSizeForRsa(rsaType);

            for (int i = 0; inputLen - offSet > 0; offSet = i * MAX_ENCRYPT_BLOCK_OF_RSA) {
                byte[] cache;
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK_OF_RSA) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK_OF_RSA);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }

                out.write(cache, 0, cache.length);
                ++i;
            }

            byte[] encryptedData = Base64.encodeBase64(out.toByteArray());
            out.close();
            return new String(encryptedData, charset);
        } catch (Exception e) {
            throw new ThirdpayApiException("rsa encrypt error! content = " + content + ",charset = " + charset, e);
        }
    }

    /**
     * RSA解密
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws ThirdpayApiException
     */
    public static String rsaDecrypt(String content, String privateKey, String charset, String rsaType) throws ThirdpayApiException {
        try {
            PrivateKey e = getPrivateKeyFromPKCS8(ThirdpayConstants.ALGORITHM_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
            Cipher cipher = Cipher.getInstance(ThirdpayConstants.ALGORITHM_TYPE_RSA);
            cipher.init(Cipher.DECRYPT_MODE, e);
            byte[] encryptedData = Base64.decodeBase64(content.getBytes(charset));
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            final int DECRYPT_BLOCK_OF_RSA = getDecryptBlockSizeForRsa(rsaType);

            for (int i = 0; inputLen - offSet > 0; offSet = i * DECRYPT_BLOCK_OF_RSA) {
                byte[] cache;
                if (inputLen - offSet > DECRYPT_BLOCK_OF_RSA) {
                    cache = cipher.doFinal(encryptedData, offSet, DECRYPT_BLOCK_OF_RSA);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }

                out.write(cache, 0, cache.length);
                ++i;
            }

            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, charset);
        } catch (Exception e) {
            throw new ThirdpayApiException("rsa decrypt error! content = " + content + ",charset = " + charset, e);
        }
    }

    /**
     * 获取签名时的原始内容
     * @param params
     * @return
     */
    public static String getSignOriginalContent(Map params) {
        params = params == null ? new HashMap() : params;
        return ThirdpayUtils.getSortKV(params);
    }

    /**
     * 获取RSA加密时最大的数据块长度
     * @param rsaType
     * @return
     */
    private static int getMaxEncryptBlockSizeForRsa(String rsaType) {
        // 秘钥长度(即模值的长度)
        int length = publicKeyType2LengthMap.get(rsaType);
        // padding标准默认使用PKCS1Padding, 其占用11个字节
        return length/8 -11;

    }

    /**
     * 获取RSA解密时数据块长度
     * @param rsaType
     * @return
     */
    private static int getDecryptBlockSizeForRsa(String rsaType) {
        // 秘钥长度(即模值的长度)
        int length = publicKeyType2LengthMap.get(rsaType);
        return length/8;
    }

    /**
     * 获取公钥
     * @param algorithm
     * @param ins
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        StringWriter writer = new StringWriter();
        StreamUtil.io(new InputStreamReader(ins), writer);
        byte[] encodedKey = writer.toString().getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    /**
     * 获取私钥
     * @param algorithm
     * @param ins
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins != null && !StringUtils.isEmpty(algorithm)) {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            byte[] encodedKey = StreamUtil.readText(ins).getBytes();
            encodedKey = Base64.decodeBase64(encodedKey);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        } else {
            return null;
        }
    }
}
