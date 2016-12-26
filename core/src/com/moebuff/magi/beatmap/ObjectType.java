package com.moebuff.magi.beatmap;

import java.util.HashMap;
import java.util.Map;

/**
 * 物件类型
 * <p>
 * 这个 {@link #NewCombo} 是旧版的，新版是原有物件的标识+4，比如 {@link #Normal} 的nc就是 {@link #NormalNewCombo}。
 * nc 下一个颜色+4，跳过一个颜色+20，跳过两个颜色+36，跳过三个颜色+52，第n个颜色需要加4+(n-1)*16，一共八个颜色最多。
 * </p>
 *
 * @author muto
 */
public enum ObjectType {
    Normal(1),//圈圈
    Slider(2),//滑条
    NewCombo(4),
    NormalNewCombo(5),
    SliderNewCombo(6),
    Spinner(8),//转盘
    ColourHax(112),
    /**
     * 单个 {@link #Normal} 的长按版，iOS中出现过，目前pc版只做预留。
     * 与 {@link #ManiaLong} 的值相同，后者为骂娘的长按。
     * 参考视频：<a href="https://www.youtube.com/watch?v=fNXvQbbohho">油管</a>
     */
    Hold(128),
    ManiaLong(128);//面条

    private int value;

    ObjectType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    //---------------------------------------------------------------------------------------------

    private static final Map<Integer, ObjectType> types = new HashMap<>();

    static {
        for (ObjectType t : values()) {
            int key = t.value;
            if (types.containsKey(key)) continue;
            types.put(key, t);
        }
    }

    public static ObjectType valueOf(int value) {
        return types.containsKey(value) ? types.get(value) : Spinner;
    }
}
