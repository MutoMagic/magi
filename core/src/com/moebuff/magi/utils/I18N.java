package com.moebuff.magi.utils;

import com.moebuff.magi.reflect.FieldKit;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化
 *
 * @author muto
 */
public class I18N {

    public static String FILE_MISSING;
    public static String MKDIRS_FAILED;

    //==========================================================================================

    /**
     * 加载指定地区的语言环境
     *
     * @param locale 特定的地理、政治和文化地区
     */
    public static void load(Locale locale) {
        String baseName = I18N.class.getSimpleName().toLowerCase();
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
        for (Field f : I18N.class.getFields()) {
            String key = f.getName();
            if (bundle.containsKey(key)) {
                FieldKit.writeStaticField(f, bundle.getString(key));
            }
        }
    }

    static {
        load(Locale.getDefault());//当前地区
    }

}
