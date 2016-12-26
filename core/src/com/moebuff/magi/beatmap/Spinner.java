package com.moebuff.magi.beatmap;

import com.moebuff.magi.graphics.PointF;
import lombok.ToString;

/**
 * 转盘
 *
 * @author muto
 */
@ToString(callSuper = true)
public class Spinner extends HitObject {
    @Override
    public int getCombo(float sliderTick, float sliderSpeed) {
        return 1;
    }

    public Spinner(int startTime, int endTime, PointF pos, TimingPoint tp) {
        super(startTime, endTime, ObjectType.Spinner, pos, tp, 1);
    }
}
