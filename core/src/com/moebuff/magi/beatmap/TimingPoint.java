package com.moebuff.magi.beatmap;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 节奏点
 *
 * @author muto
 */
@Data
@AllArgsConstructor
public class TimingPoint {
    private float bpm;
    private float offset;
    private float speed;
}
