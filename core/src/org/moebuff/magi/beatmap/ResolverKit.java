package org.moebuff.magi.beatmap;

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
 * 类ini文件解析工具
 * <p/>
 * 约定：
 * 1.属性必须是{@link String} 或{@link List}
 * 2.String保存键值对的值，List保存行。
 * 3.键值对的赋值符号为SYMBOL_ASSIGNMENT
 * 4.解析类型默认为RESTYPE_ATTR
 * 5.解析类型为RESTYPE_LINE时，需手动声明解析方法。方法名：analyze+SectionName
 * 6.继承后必须重写构造方法。
 * 注意：
 * 1.创建新对象会初始化工具类，此过程重复执行会降低效率。若能重用请调用带参构造方法。
 * 2.不要尝试使用{@link #read(File)} 来更新原有数据，即使事后调用{@link #sync()}，
 * 这样操作是不安全的，原有数据更新请执行{@link #reload()} 方法。
 *
 * @author MuTo
 */
public abstract class ResolverKit<T extends ResolverKit> {
    public static final String SYMBOL_ASSIGNMENT = ":";
    public static final int RESTYPE_ATTR = 0;
    public static final int RESTYPE_LINE = 1;

    private Class type;//当前对象类型
    private Reflect<T> reflect;//type的反射工具
    private Map<String, Section> sections;//<SectionName,Section>
    private Map<String, List<Field>> sectionFields;//<SectionName,Fields>
    private File document;//当前文件对象
    private Map<String, String> attributes;//<SectionName.属性名|SectionName[index],属性值|行>

    ResolverKit() {
        type = this.getClass();
        reflect = new Reflect(type);
        sections = new HashMap();
        sectionFields = new HashMap();

        List<Field> fieldList = null;
        for (Field f : type.getDeclaredFields()) {
            Class type = f.getType();
            if (type != String.class && type != List.class)
                continue;

            Section sec = f.getAnnotation(Section.class);
            if (sec != null) {
                sections.put(sec.value(), sec);
                sectionFields.put(sec.value(), fieldList = new ArrayList());
            }
            if (fieldList != null)
                fieldList.add(f);
        }
    }

    ResolverKit(Class c, Reflect r, HashMap sec, HashMap secfield) {
        type = c;
        reflect = r;
        sections = sec;
        sectionFields = secfield;
    }

    public T reload() {
        attributes = new HashMap();
        anaylzeFile(document);
        sync();
        return (T) this;
    }

    //更新原有数据，不推荐外部调用
    @Deprecated
    public void sync() {
        for (Field f : type.getDeclaredFields())
            if (f.getType() == List.class)
                reflect.invokeSet(f.getName(), this, new ArrayList());
        crossFile(this);
    }

    public T read(String path) {
        return read(new File(path));
    }

    public T read(File f) {
        T t = reflect.newInstance(type, reflect, sections, sectionFields);
        t.setDocument(document = f);
        t.setAttributes(attributes = new HashMap());
        anaylzeFile(f);
        crossFile(t);
        return t;
    }

    //解析文件
    private void anaylzeFile(File f) {
        BufferedReader reader = null;
        try {
            int num = 1;
            String sectionName = "";
            Section sec = null;
            boolean lineAttr = false;//是否保留一行
            reader = new BufferedReader(new FileReader(f));
            for (String line = null; (line = reader.readLine()) != null; ) {
                if ((line = line.trim()).isEmpty())
                    continue;//排除空行

                if (line.matches("^\\[.+\\]$")) {
                    sectionName = line.substring(num = 1, line.length() - 1);
                    sec = sections.get(sectionName);

                    List<Field> fieldList = sectionFields.get(sectionName);
                    if (fieldList.size() == 1 && fieldList.get(0).getType() == List.class)
                        lineAttr = true;
                } else if (sec != null && sec.type() == RESTYPE_ATTR && !lineAttr) {
                    if (line.matches("^//.*$"))
                        continue;//跳过注释

                    String[] split = line.split(SYMBOL_ASSIGNMENT);
                    String key = StringUtil.cmdStyle(sectionName, split[0].trim());
                    if (split.length == 2)
                        attributes.put(key, split[1].trim());
                    else
                        attributes.put(key, "");
                } else
                    attributes.put(StringUtil.arrayStyle(sectionName, num++ - 1), line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Stream.close(reader);
        }
    }

    //关联属性
    private void crossFile(Object obj) {
        for (Iterator<String> secKeyI = sections.keySet().iterator(); secKeyI.hasNext(); ) {
            String sectionName = secKeyI.next();
            Section sec = sections.get(sectionName);
            List<Field> fieldList = sectionFields.get(sectionName);
            if (sec.type() == RESTYPE_LINE) {
                reflect.invoke("analyze" + StringUtil.upperInitial(sectionName), obj);
            } else {
                Field firstField = fieldList.get(0);
                if (fieldList.size() == 1 && firstField.getType() == List.class) {
                    List list = (List) reflect.invokeGet(firstField.getName(), obj);
                    String line = null;
                    for (int i = 0; ; i++) {
                        line = attributes.get(StringUtil.arrayStyle(sectionName, i));
                        if (line == null)
                            break;
                        list.add(line);
                    }
                    continue;
                }
                for (int i = 0; i < fieldList.size(); i++) {
                    String fieldName = fieldList.get(i).getName();
                    String attrName = StringUtil.cmdStyle(sectionName, StringUtil.upperInitial(fieldName));
                    reflect.invokeSet(fieldName, obj, attributes.get(attrName));
                }
            }
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

    public Reflect<T> getReflect() {
        return reflect;
    }

    public void setReflect(Reflect<T> reflect) {
        this.reflect = reflect;
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

    public File getDocument() {
        return document;
    }

    public void setDocument(File document) {
        this.document = document;
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
        Difficulty.kit.read(MapLoader.SONGPATH + "72217 Zips - Heisei Cataclysm/Zips - Heisei Cataclysm (Dark Fang) [0108].osu");
    }
}
