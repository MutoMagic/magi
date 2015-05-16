package org.moebuff.magi.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES-128
 *
 * @author MuTo
 */
public class AESAlg {
    public static final String ALGORITHM = "AES";
    private static final SecretKeySpec KEY = kgen(ALGORITHM);

    //加密
    public static String encrypt(String val) {
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, KEY);
            return Base64.encodeBase64String(c.doFinal(val.getBytes(Encoding.DEFAULT)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //解密
    public static String decrypt(String val) {
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, KEY);
            return new String(c.doFinal(Base64.decodeBase64(val)), Encoding.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //生成密封
    private static SecretKeySpec kgen(String seed) {
        try {
            byte[] k = new byte[16],//key
                    b = MD5Alg.encrypt32(seed).getBytes(Encoding.DEFAULT);
            for (int i = 0; i < k.length && i < b.length; i++)
                k[i] = b[i];
            return new SecretKeySpec(k, ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
