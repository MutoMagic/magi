package org.moebuff.magi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.moebuff.magi.audio.MusicController;
import org.moebuff.magi.beatmap.BeatMap;
import org.moebuff.magi.beatmap.MapLoader;

import java.util.Iterator;

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
        Iterator<BeatMap> bi = loader.getBaetMaps().iterator();
        bi.next();//play heisei
        String musicName = bi.next().getMusicMap().values().iterator().next();
        System.out.println(musicName);
        new MusicController(musicName).loopPlay();
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
