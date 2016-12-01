package com.moebuff.magi.utils;

import com.moebuff.magi.reflect.FieldExtension;
import org.apache.commons.lang3.StringUtils;

/**
 * 扩充 StringUtils
 *
 * @author muto
 */
public class StringExtension {
    /**
     * 首字母大写
     */
    public static String capitalize(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        char[] ca = str.toCharArray();
        if (Character.isTitleCase(ca[0]) || !Character.isLetter(ca[0])
                ) {
            return str;
        }

        ca[0] -= 32;
        String result = new String();//对象必须是全新的
        FieldExtension.writeConstant("value", result, ca);
        return result;
    }
}
