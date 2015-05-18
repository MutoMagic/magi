package org.moebuff.magi.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.sun.media.codec.audio.mp3.JavaDecoder;

import javax.media.*;
import javax.media.bean.playerbean.MediaPlayer;
import java.io.File;
import java.net.URL;

/**
 * music控制器
 *
 * @author MuTo
 */
public class MusicController {
    private FileHandle handle;
    private Music music;
    private OnCompletionListener onCompletionListener;

    public MusicController(String path) {
        handle = Gdx.files.internal(path);
        music = Gdx.audio.newMusic(handle);
        music.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                if (onCompletionListener != null)
                    onCompletionListener.onCompletion(music);
                music.dispose();
            }
        });
    }

    public void play() {
        music.play();
    }

    public void loopPlay() {
        music.setLooping(true);
        play();
    }

    // Properties
    // -------------------------------------------------------------------------

    public FileHandle getHandle() {
        return handle;
    }

    public void setHandle(FileHandle handle) {
        this.handle = handle;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public OnCompletionListener getOnCompletionListener() {
        return onCompletionListener;
    }

    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    // Internal
    // -------------------------------------------------------------------------

    public interface OnCompletionListener {
        void onCompletion(Music music);
    }
}
