package org.moebuff.magi.util;

import java.security.MessageDigest;

/**
 * Created by MuTo on 2015/5/5.
 */
public class MD5Alg {
    public static final String ALGORITHM = "MD5";
    public static final char HEXDIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String encrypt64(String val) {
        StringBuffer r = new StringBuffer();
        byte[] d = digest(val);
        for (int i = 0; i < d.length; i++) {
            byte $d = d[i];
            r.append(HEXDIGITS[$d >>> 4 & 0xc + $d & 3]);
            r.append(HEXDIGITS[$d >>> 4 & 0xf]);
            r.append(HEXDIGITS[$d >>> 2 & 0xf]);
            r.append(HEXDIGITS[$d & 0xf]);
        }
        return r.toString();
    }

    public static String encrypt32(String val) {
        StringBuffer r = new StringBuffer();
        byte[] d = digest(val);
        for (int i = 0; i < d.length; i++) {
            byte $d = d[i];
            r.append(HEXDIGITS[$d >>> 4 & 0xf]);
            r.append(HEXDIGITS[$d & 0xf]);
        }
        return r.toString();
    }

    public static String encrypt(String val) {
        StringBuffer r = new StringBuffer();
        byte[] d = digest(val);
        for (int i = 0; i < d.length; i++)
            r.append(HEXDIGITS[d[i] & 0xf]);
        return r.toString();
    }

    private static byte[] digest(String val) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(val.getBytes(Encoding.DEFAULT));
            return md.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
