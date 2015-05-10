package org.moebuff.magi.beatmap;

import java.lang.annotation.*;

/**
 * 标签
 *
 * @author MuTo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Tag {
    String value();
}
