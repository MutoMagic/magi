package com.moebuff.magi.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.moebuff.magi.MagiGame;
import com.moebuff.magi.utils.OS;
import com.moebuff.magi.utils.StringExtension;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = StringExtension.capitalize(OS.project);
        new LwjglApplication(new MagiGame(), config);
    }
}
