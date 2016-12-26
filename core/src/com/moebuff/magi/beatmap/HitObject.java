package com.moebuff.magi.beatmap;

import com.moebuff.magi.graphics.PointF;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 打击物件
 *
 * @author muto
 */
@Data
@AllArgsConstructor
public abstract class HitObject {
    protected int startTime;
    protected int endTime;
    protected ObjectType type;
    protected PointF pos;
    protected TimingPoint tp;
    protected int repeat;

    public abstract int getCombo(float sliderTick, float sliderSpeed);

    public int length() {
        return endTime - startTime;
    }
}
