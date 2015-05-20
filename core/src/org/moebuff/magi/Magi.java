package org.moebuff.magi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.apache.log4j.Logger;
import org.moebuff.magi.audio.MusicController;
import org.moebuff.magi.beatmap.BeatMap;
import org.moebuff.magi.beatmap.Difficulty;
import org.moebuff.magi.beatmap.MapLoader;

import java.util.Iterator;

public class Magi extends ApplicationAdapter {
    Logger logger = Logger.getLogger(Magi.class);
    SpriteBatch batch;//画笔
    Texture img;//纹理

    @Override
    public void create() {
        MapLoader loader = new MapLoader();
        Iterator<BeatMap> bi = loader.getBaetMaps().iterator();
        BeatMap map = bi.next();
        String musicName = map.getMusicMap().values().iterator().next();
        new MusicController(musicName).start();
        Difficulty diff = map.getDiffs().iterator().next();

        batch = new SpriteBatch();
        img = new Texture(map.getPath() + "/" + diff.getBg()[2]);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();

    }

}
