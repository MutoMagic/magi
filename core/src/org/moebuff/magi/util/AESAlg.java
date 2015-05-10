package org.moebuff.magi.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;

/**
 * Created by MuTo on 2015/5/6.
 */
public class AESAlg {
    public static final String ALGORITHM = "AES";
    private static final SecretKeySpec KEY = kgen(ALGORITHM);

    //加密
    public static String encrypt(String val) {
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, KEY);
            return Encoding.Base64Encode(c.doFinal(val.getBytes(Encoding.DEFAULT)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //解密
    public static String decrypt(String val) {
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, KEY);
            return new String(c.doFinal(Encoding.Base64Decode(val)), Encoding.DEFAULT);
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
