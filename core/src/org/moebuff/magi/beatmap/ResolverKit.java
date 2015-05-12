package org.moebuff.magi.beatmap;

import org.moebuff.magi.util.PathUtil;
import org.moebuff.magi.util.Reflect;
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
public abstract class ResolverKit<T extends ResolverKit> {
    public static final int RESTYPE_ATTR = 0;
    public static final int RESTYPE_LINE = 1;
    public static final int RESTYPE_EXEC = 2;

    private Class type = this.getClass();//当前对象类型
    private Reflect reflect = new Reflect(type);//type的反射工具
    //for copy
    private Map<String, Section> sections;//<SectionName,Section>
    private Map<String, Field> sectionFields;//<SectionName.FieldName,Field>
    private Map<String, String> attributes;//<SectionName.属性名,属性值>

    public T read(String path) {
        return read(new File(path));
    }

    public T read(File f) {
        try {
            T t = (T) type.newInstance();

            //copy attr
            t.setSections(sections = new HashMap());
            t.setSectionFields(sectionFields = new HashMap());
            t.setAttributes(attributes = new HashMap());

            analyzeClass();
            anaylzeFile(f, t);//解析文件

            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void analyzeClass() {
        String sectionName = "";
        for (Field f : type.getDeclaredFields()) {
            Section sec = f.getAnnotation(Section.class);
            if (sec != null) {
                sectionName = sec.value();
                sections.put(sec.value(), sec);
            }
            sectionFields.put(StringUtil.cmdStyle(sectionName, f.getName()), f);
        }
    }

    private void anaylzeFile(File f, T t) throws Exception {
        BufferedReader reader = null;
        try {
            int num = 1;
            Section sec = null;
            String sectionName = "";
            reader = new BufferedReader(new FileReader(f));
            for (String line = null; (line = reader.readLine()) != null; ) {
                if ((line = line.trim()).isEmpty())
                    continue;//排除空行
                if (line.matches("^\\[.+\\]$")) {
                    sectionName = line.substring(num = 1, line.length() - 1);
                    sec = sections.get(sectionName);
                } else if (sec != null && sec.type() == RESTYPE_ATTR) {
                    String[] split = line.split(":");
                    if (split.length == 2) {
                        String name = split[0].trim();
                        String value = split[1].trim();
                        attributes.put(StringUtil.cmdStyle(sectionName, name), value);
                        reflect.invokeSet(name, t, value);
                    }
                } else {
                    if (sec != null && sec.type() == RESTYPE_LINE) {
                        String fieldName = StringUtil.lowerInitial(sectionName);
                        Field field = sectionFields.get(StringUtil.cmdStyle(sectionName, fieldName));
                        field.setAccessible(true);
                        List list = (List) field.get(t);
                        list.add(line);
                    } else if (sec != null && sec.type() == RESTYPE_EXEC)
                        ;
                    attributes.put(StringUtil.arrayStyle(sectionName, num++), line);
                }

            }
        } finally {
            Stream.close(reader);
        }
    }

    // Properties
    // -------------------------------------------------------------------------

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Reflect getReflect() {
        return reflect;
    }

    public void setReflect(Reflect reflect) {
        this.reflect = reflect;
    }

    public Map<String, Section> getSections() {
        return sections;
    }

    public void setSections(Map<String, Section> sections) {
        this.sections = sections;
    }

    public Map<String, Field> getSectionFields() {
        return sectionFields;
    }

    public void setSectionFields(Map<String, Field> sectionFields) {
        this.sectionFields = sectionFields;
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
}
