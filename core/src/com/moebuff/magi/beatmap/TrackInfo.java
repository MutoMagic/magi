package com.moebuff.magi.beatmap;

import lombok.Data;

/**
 * 音轨信息
 *
 * @author muto
 */
@Data
public class TrackInfo {
    private String filename;
    private BeatmapInfo beatmap;

    private String publicName;
    private String mode;
    private String creator;
    private String background;
    private int beatmapID;
    private int beatmapSetID;
    private float difficulty;
    private float hpDrain;
    private float overallDifficulty;
    private float approachRate;
    private float circleSize;
    private float bpmMax;
    private float bpmMin = Float.MAX_VALUE;
    private long musicLength;
    private int hitCircleCount;
    private int sliderCount;
    private int spinerCount;
    private int totalHitObjectCount;
    private int maxCombo;
}
