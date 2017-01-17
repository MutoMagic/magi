package com.moebuff.magi.beatmap;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 乐谱信息
 *
 * @author muto
 */
@Data
public class BeatmapInfo {
    private String title;
    private String titleUnicode;
    private String artist;
    private String artistUnicode;
    private String creator;

    private final List<TrackInfo> tracks = new ArrayList<>();

    private String path;
    private String source;
    private String tags;
    private String music;

    private int offset;
    private boolean favorite;

    public void addTrack(final TrackInfo track) {
        tracks.add(track);
    }

    public TrackInfo getTrack(final int index) {
        return tracks.get(index);
    }

    public int getCount() {
        return tracks.size();
    }
}
