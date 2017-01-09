package com.moebuff.magi.audio;

import com.badlogic.gdx.files.FileHandle;
import com.moebuff.magi.io.FCIV;
import com.moebuff.magi.io.FF;
import com.moebuff.magi.utils.Operation;
import org.apache.commons.io.FilenameUtils;

/**
 * 音频控制器
 *
 * @author muto
 */
public class Controller {
    private String path;
    private float bpm;
    private int offset;
    private int beat;
    private FileHandle song;
    private Player music;

    private Player variable;
    private Metronome meter;

    public Controller(String path, float bpm, int offset, int beat) {
        this.path = path;
        this.bpm = bpm;
        this.offset = offset;
        this.beat = beat;

        song = FF.SONGS.child(path);
        music = new MusicPlayer(song);
    }

    public void play(int previewTime) {
        music.play(previewTime);
    }

    /**
     * 伸缩与变调
     *
     * @param rate 变速倍率，正加负减
     * @param tone 是否变调
     */
    public void play(float rate, boolean tone) {
        String name = String.format("%s_%s_%b.%s",
                FCIV.md5Hex(song.file()),
                Operation.toHexString(rate),
                tone,
                FilenameUtils.getExtension(path)
        );
        FileHandle cache = FF.CACHE.child(name);
        if (cache.exists()) {
            variable = new MusicPlayer(cache);
        } else {
            variable = new VariablePlayer(song, rate, tone);
            song.copyTo(cache);//生成缓存
        }

        long delay = (long) (offset / rate);
        meter = new Metronome(bpm * rate, beat);//初始化节拍器
        variable.setListener(player -> {
            dispose();//结束后释放资源
            return false;
        });

        //启动节拍器并播放音乐
        if (tone) {
            meter.start(delay);
        }
        variable.play();
    }

    public void dispose() {
        music.dispose();
        if (variable != null) {
            variable.dispose();
            variable = null;
        }
        if (meter != null) {
            meter.dispose();
            meter = null;
        }
    }
}
