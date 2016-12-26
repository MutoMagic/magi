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

    private static Class<I18N> ic = I18N.class;
    private static String baseName = ic.getSimpleName().toLowerCase();
    private static Field[] fields = ic.getFields();

    /**
     * 加载指定地区的语言环境
     *
     * @param locale 特定的地理、政治和文化地区
     */
    public static void load(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
        for (Field f : fields) {
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
