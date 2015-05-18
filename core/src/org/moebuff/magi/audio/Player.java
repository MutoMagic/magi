package org.moebuff.magi.audio;

import org.moebuff.magi.beatmap.MapLoader;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.protocol.DataSource;
import java.io.File;

/**
 * Created by MuTo on 2015/5/18.
 */
public class Player {
    public static void main(String[] args) {
        String path = MapLoader.SONGPATH + "72217 Zips - Heisei Cataclysm\\IA  Heisei Kataku.mp3";
        path = MapLoader.SONGPATH + "172900 Giga-P - Electric Angel\\gamu kun daisuki.mp3";

        try {
            //JavaDecoder.main(null);

            System.out.println(path);

            DataSource source = Manager.createDataSource(new File(path).toURL());
            System.out.println(source.getContentType());

            javax.media.Player p = Manager.createRealizedPlayer(source);
            p.start();
            //p.stop();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
