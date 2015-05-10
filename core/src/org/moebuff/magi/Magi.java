package org.moebuff.magi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.moebuff.magi.beatmap.BeatMap;
import org.moebuff.magi.beatmap.MapLoader;
import org.moebuff.magi.util.FileUtil;
import org.moebuff.magi.util.PathUtil;

import java.io.File;

public class Magi extends ApplicationAdapter {
    SpriteBatch batch;//画笔
    Texture img;//纹理
    TextureRegion region;//截图

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("skin/badlogic.jpg");
        region = new TextureRegion(img, 0, 0, 160, 160);

        MapLoader loader = new MapLoader();
        String musicName = loader.getBaetMaps().iterator().next().getMusicMap().values().iterator().next();

        Music music = Gdx.audio.newMusic(Gdx.files.internal(musicName));
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //batch.draw(img, 100, 100);
        batch.draw(region, 100, 100);
        batch.end();
    }

}
