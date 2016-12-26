package com.moebuff.magi.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.moebuff.magi.MagiGame;
import com.moebuff.magi.utils.OS;
import com.moebuff.magi.utils.StringKit;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = StringKit.capitalize(OS.project);
        new LwjglApplication(new MagiGame(), config);
    }
}
