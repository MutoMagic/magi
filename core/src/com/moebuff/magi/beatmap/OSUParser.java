package com.moebuff.magi.beatmap;

import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;
import java.util.Map;

/**
 * OSU 文件语法分析器
 *
 * @author muto
 */
public class OSUParser {
    private final Map<String, BeatmapSection> sections = new HashMap<>();

    private FileHandle folder;

    public OSUParser(FileHandle folder) {
        this.folder = folder;
    }


}
