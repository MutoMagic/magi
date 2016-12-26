package com.moebuff.magi.beatmap;

import java.util.HashMap;
import java.util.Map;

/**
 * 滑条类型
 *
 * @author muto
 */
public enum SliderType {
    Catmull,
    Bezier,
    Linear,
    PerfectCurve;

    private static final Map<Character, SliderType> types = new HashMap<>();

    static {
        for (SliderType t : values()) {
            types.put(t.name().charAt(0), t);
        }
    }

    public static SliderType parse(char value) {
        return types.containsKey(value) ? types.get(value) : Bezier;
    }
}
