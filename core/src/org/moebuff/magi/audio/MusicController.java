package org.moebuff.magi.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

/**
 * music控制器
 *
 * @author MuTo
 */
public class MusicController {
    private FileHandle handle;
    private Music music;

    public MusicController(String path) {
        handle = Gdx.files.internal(path);
        music = Gdx.audio.newMusic(handle);
        music.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {

                music.dispose();
            }
        });
    }

    public void loopPlay() {
        music.setLooping(true);
        music.play();
    }
}
