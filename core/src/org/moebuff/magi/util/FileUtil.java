package org.moebuff.magi.util;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 文件工具
 *
 * @author MuTo
 */
public class FileUtil {
    /**
     * 按照文件名进行排序，文件夹顺序小于文件。
     *
     * @param files 需要排序的文件数组
     */
    public static void sort(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return StringUtil.compare(o1.getName(), o2.getName());
            }
        });
    }

    /**
     * 获取文件的后缀名
     *
     * @param name 文件名
     * @return 后缀名
     */
    public static String getExtensionName(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }
}
