package com.moebuff.magi.beatmap;

import com.moebuff.magi.graphics.PointF;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

/**
 * 滑条
 *
 * @author muto
 */
@Getter
@Setter
@ToString(callSuper = true)
public class Slider extends HitObject {
    private SliderType stype;
    private ArrayList<PointF> poss;
    private float rawLength;//原长度

    public Slider(int startTime, int endTime, PointF pos, TimingPoint tp, int repeat,
                  SliderType stype, ArrayList<PointF> poss, float rawLength) {
        super(startTime, endTime, ObjectType.Slider, pos, tp, repeat);
        this.stype = stype;
        this.poss = poss;
        this.rawLength = rawLength;
    }

    @Override
    public int getCombo(float sliderTick, float sliderSpeed) {
        return (int) Math.ceil(sliderTick * rawLength / (tp.getSpeed() * sliderSpeed) / 100.01f) * repeat + 1;
    }

    @Override
    public int length() {
        return super.length() * repeat;
    }
}
