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
import com.moebuff.magi.utils.Log;
import org.apache.commons.io.FilenameUtils;

public class MagiGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    @Override
    public void create() {
        String name = FF.OSZ_404910.name();
        String base = FilenameUtils.getBaseName(name);
        FileHandle dir = FF.SONGS.child(base);
        Log.i("音频路径 %s", dir.path());
        if (!dir.exists()) {
            FF.OSZ_404910.copyTo(FF.SONGS.child(name));
            ZipPackage.unpack(FF.SONGS.child(name).file(), dir.file());
            FF.SONGS.child(name).delete();
        }
        Controller c = new Controller(base + "/Sana - Packet Hero.mp3", 236, 396, 44);
//        c.play(1.5f, true);
        c.play(64400);

        batch = new SpriteBatch();
//        img = new Texture("badlogic.jpg");
        img = new Texture(dir.child("PacketBG.jpg"));
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
