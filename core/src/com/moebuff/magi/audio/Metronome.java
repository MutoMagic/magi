package com.moebuff.magi.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.moebuff.magi.io.FF;
import com.moebuff.magi.reflect.MethodKit;
import com.moebuff.magi.utils.RepetitiveTask;

import java.lang.reflect.Method;
import java.util.Timer;

/**
 * 节拍器
 *
 * @author muto
 */
public class Metronome extends RepetitiveTask {
    private float bpm;
    private int beat;//节拍 43=3/4
    private int section;//小节
    private int count;
    private int sum;
    private Timer timer = new Timer(true);
    private Method model;

    private Sound bassDrun;//大鼓 动
    private Sound hiHat;//踩镲 次
    private Sound snareDrun;//小鼓 打
    private float volume = 1.0f;

    public Metronome(float bpm, int beat) {
        bassDrun = Gdx.audio.newSound(FF.DRUM);
        hiHat = Gdx.audio.newSound(FF.CLAP);
        snareDrun = Gdx.audio.newSound(FF.FINISH);
        setup(bpm, beat);
    }

    protected void setup(float bpm, int beat) {
        reset();
        this.bpm = bpm;
        if (this.beat != beat) {
            String name = String.format("m%s", beat);
            model = MethodKit.getMethod(Metronome.class, name);
            section = beat % 10 * 2;//beat对10取余得到个位数，乘以2将计数精确到原来的两倍
            this.beat = beat;
        }
    }

    protected void reset() {
        count = 1;
        sum = 0;
    }

    /**
     * 立即启动
     */
    public void start() {
        start(0);
    }

    /**
     * 延迟指定毫秒后启动
     */
    public void start(long delay) {
        float period = 30000 / bpm;//1分钟等于6w毫秒，除以2是为了双倍计数
        timer.scheduleAtFixedRate(this, delay, (long) period);
    }

    public void refresh(float bpm, int beat) {
        purge(timer);
        setup(bpm, beat);
        start();
    }

    /**
     * 保持节拍不变的情况下，更新bpm
     */
    public void refresh(float bpm) {
        refresh(bpm, beat);
    }

    public void dispose() {
        purge(timer);
        bassDrun.dispose();
        hiHat.dispose();
        snareDrun.dispose();
        reset();
    }

    public void setVolume(int per) {
        volume = per / 100;
    }

    @Override
    public void run() {
        if (sum % 4 == 0 && count == 1) {
            play(snareDrun);//每4小节为一组，第一个音是finish
        } else {
            MethodKit.invoke(model, this);
        }

        //计数器
        if (count < section) {
            count++;
        } else {
            count = 1;
            sum++;
        }
    }

    private void play(Sound s) {
        s.play(volume);
    }

    private void m43() {
        if (count == 1) play(bassDrun);
        if (count == 3) play(hiHat);
        if (count == 4) play(bassDrun);
        if (count == 6) play(hiHat);
    }

    private void m44() {
        if (count == 1) play(bassDrun);
        if (count == 3) play(hiHat);
        if (count == 5) play(bassDrun);
        if (count == 7) play(hiHat);
    }

    private void m45() {
        if (count == 1) play(bassDrun);
        if (count == 3) play(hiHat);
        if (count == 5) play(bassDrun);
        if (count == 6) play(bassDrun);
        if (count == 8) play(hiHat);
        if (count == 10) play(bassDrun);
    }

    private void m46() {
        if (count == 1) play(bassDrun);
        if (count == 3) play(hiHat);
        if (count == 5) play(bassDrun);
        if (count == 7) play(bassDrun);
        if (count == 9) play(hiHat);
        if (count == 11) play(bassDrun);
    }

    private void m47() {
        if (count == 1) play(bassDrun);
        if (count == 3) play(hiHat);
        if (count == 5) play(bassDrun);
        if (count == 7) play(hiHat);
        if (count == 8) play(bassDrun);
        if (count == 10) play(hiHat);
        if (count == 12) play(bassDrun);
        if (count == 14) play(hiHat);
    }
}
