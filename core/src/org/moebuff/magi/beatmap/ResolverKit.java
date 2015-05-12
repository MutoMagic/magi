package org.moebuff.magi.beatmap;

import org.moebuff.magi.util.PathUtil;
import org.moebuff.magi.util.Stream;
import org.moebuff.magi.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * ini文件工具
 *
 * @author MuTo
 */
public abstract class ResolverKit<T> {
    public static final int RESTYPE_ATTR = 0;
    public static final int RESTYPE_LINE = RESTYPE_ATTR + 1;

    private Class type = this.getClass();//当前对象类型
    private Map<String, Section> sections = new HashMap();
    private Map<String, List<Field>> sectionFields = new HashMap();
    private List<Field> noSectionFields = new ArrayList();
    private Map<String, String> attributes = new HashMap();

    public T read(String path) {
        return read(new File(path));
    }

    public T read(File path) {
        List<Field> fieldList = noSectionFields;
        for (Field f : type.getDeclaredFields()) {
            Section sec = f.getAnnotation(Section.class);
            if (sec != null) {
                sections.put(sec.value(), sec);
                sectionFields.put(sec.value(), fieldList = new ArrayList());
            }
            fieldList.add(f);
        }

        //read file
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));

            int num = 1;//计数
            Section sec = null;
            for (String line = null, sectionName = null; (line = reader.readLine()) != null; ) {
                if ((line = line.trim()).isEmpty())
                    continue;//排除空行

                if (line.matches("^\\[.+\\]$")) {
                    sectionName = line.substring(num = 1, line.length() - 1);
                    sec = sections.get(sectionName);
                } else if (sec != null && sec.type() == RESTYPE_ATTR) {
                    String[] split = line.split(":");
                    if (split.length == 2)
                        attributes.put(sectionName + split[0], split[1].trim());
                } else
                    attributes.put(StringUtil.arrayStyle(sectionName, num++), line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Stream.close(reader);
        }

        int nums = 0;
        for (String s : attributes.keySet()) {
            if (s.indexOf("HitObjects") != -1)
                nums++;
        }
        for (int i = 1; i <= nums; i++) {
            String s = StringUtil.arrayStyle("HitObjects", i);
            System.out.println(s + "=" + attributes.get(s));
        }

        return null;
    }

    // Properties
    // -------------------------------------------------------------------------

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Map<String, Section> getSections() {
        return sections;
    }

    public void setSections(Map<String, Section> sections) {
        this.sections = sections;
    }

    public Map<String, List<Field>> getSectionFields() {
        return sectionFields;
    }

    public void setSectionFields(Map<String, List<Field>> sectionFields) {
        this.sectionFields = sectionFields;
    }

    public List<Field> getNoSectionFields() {
        return noSectionFields;
    }

    public void setNoSectionFields(List<Field> noSectionFields) {
        this.noSectionFields = noSectionFields;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    // Internal
    // -------------------------------------------------------------------------

    /**
     * 节，部分
     *
     * @author MuTo
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Section {
        String value();

        int type() default RESTYPE_ATTR;
    }

    public static void main(String[] args) {
        Difficulty.kit.read(PathUtil.addSeparator(PathUtil.USERDIR, "songs/72217 Zips - Heisei Cataclysm/Zips - Heisei Cataclysm (Dark Fang) [0108].osu"));
    }
}
