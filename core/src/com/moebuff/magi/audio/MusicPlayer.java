package com.moebuff.magi.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

import static com.moebuff.magi.audio.Status.*;

/**
 * 音乐播放器
 *
 * @author muto
 */
public class MusicPlayer implements Player, Music.OnCompletionListener {
    private Music music;
    private OnCompletionListener listener;
    private Status status = STOP;
    private int position;//毫秒

    public MusicPlayer(FileHandle handle) {
        music = Gdx.audio.newMusic(handle);
        music.setOnCompletionListener(this);
        music.setLooping(true);
    }

    @Override
    public void play() {
        music.play();
        status = PLAY;
        if (position > 0) {
            setPosition(position);
            position = 0;
        }
    }

    @Override
    public void play(int position) {
        setPosition(position);
        play();
    }

    @Override
    public void pause() {
        music.pause();
        status = PAUSE;
    }

    @Override
    public void stop() {
        music.stop();
        status = STOP;
    }

    @Override
    public void dispose() {
        music.dispose();
        status = DISPOSE;
        music.setOnCompletionListener(this);
        listener = null;
        position = 0;
    }

    @Override
    public int getVolume() {
        return (int) (music.getVolume() * 100);
    }

    @Override
    public void setVolume(int per) {
        music.setVolume(per / 100.0f);
    }

    @Override
    public int getPosition() {
        if (status.isPlayed()) {
            return (int) (music.getPosition() * 1000);
        }
        return position;
    }

    @Override
    public void setPosition(int position) {
        if (status.isPlayed()) {
            music.setPosition(position / 1000.0f);//仅在 play/pause 状态下有效
            return;
        }
        this.position = position;
    }

    @Override
    public void setListener(OnCompletionListener listener) {
        if (music.isLooping()) {
            music.setLooping(false);//loop状态下，listener是不会被执行的
        }
        this.listener = listener;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setLooping(boolean isLooping) {
        music.setLooping(isLooping);
    }

    @Override
    public boolean isLooping() {
        return music.isLooping();
    }

    @Override
    public boolean isPlaying() {
        return music.isPlaying();
    }

    @Override
    public void onCompletion(Music music) {
        status = STOP;//listener can change status
        if (listener != null) {
            boolean loop = listener.onCompletion(this);
            music.setLooping(loop);
        }
    }
}
