package com.moebuff.magi.beatmap;

import com.moebuff.magi.graphics.PointF;
import lombok.ToString;

/**
 * 圈圈
 *
 * @author muto
 */
@ToString(callSuper = true)
public class HitCircle extends HitObject {
    @Override
    public int getCombo(float sliderTick, float sliderSpeed) {
        return 1;
    }

    public HitCircle(int startTime, PointF pos, TimingPoint tp) {
        super(startTime, startTime, ObjectType.Normal, pos, tp, 1);
    }
}
