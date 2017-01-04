package com.moebuff.magi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.moebuff.magi.io.Catalog;

public class AndroidGdxHandle implements Catalog {
    @Override
    public FileHandle getRoot() {
        return Gdx.files.external("magi!csga");
    }

    @Override
    public FileHandle getAssets() {
        return Gdx.files.internal("");
    }
}
