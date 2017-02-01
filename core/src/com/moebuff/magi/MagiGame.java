package com.moebuff.magi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.moebuff.magi.audio.Controller;
import com.moebuff.magi.io.FF;
import com.moebuff.magi.io.ZipPackage;
import org.apache.commons.io.FilenameUtils;

public class MagiGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    @Override
    public void create() {
        String name = FF.OSZ_300034.name();
        String base = FilenameUtils.getBaseName(name);
        FileHandle dir = FF.SONGS.child(base);
        if (!dir.exists()) {
            FF.OSZ_300034.copyTo(FF.SONGS.child(name));
            ZipPackage.unpack(FF.SONGS.child(name).file(), dir.file());
            FF.SONGS.child(name).delete();
        }
        Controller c = new Controller(base + "/ml.mp3", 155, -28, 44);
//        c.play(1.5f, true);
        c.play(68);

        batch = new SpriteBatch();
//        img = new Texture("badlogic.jpg");
        img = new Texture(dir.child("wmw.png"));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
