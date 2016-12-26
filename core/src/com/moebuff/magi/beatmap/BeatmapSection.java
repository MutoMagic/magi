package com.moebuff.magi.beatmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 谱面节点
 *
 * @author muto
 */
public class BeatmapSection {
    private String name;//节点名称
    private Map<String, String> attr = new HashMap<>();
    private List<String> data = new ArrayList<>();

    public BeatmapSection(String name) {
        this.name = name;
    }

    public void add(String key, String val) {
        attr.put(key, val);
    }

    public boolean add(String line) {
        return data.add(line);
    }

    public String get(String key) {
        return attr.containsKey(key) ? attr.get(key) : "";
    }

    public String[] get() {
        return data.toArray(new String[data.size()]);
    }
}
