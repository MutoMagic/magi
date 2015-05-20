package org.moebuff.magi.audio;

import org.moebuff.magi.beatmap.MapLoader;

import javax.media.*;
import javax.media.protocol.DataSource;
import java.io.File;

/**
 * Created by MuTo on 2015/5/18.
 */
public class Player {
    public static void main(String[] args) {
        String path = MapLoader.SONGPATH + "72217 Zips - Heisei Cataclysm\\IA  Heisei Kataku.mp3";
        //path = MapLoader.SONGPATH + "38592 Iguchi Yuka - Mirai Shoot\\Mirai Shoot.mp3";
        //path = MapLoader.SONGPATH + "53519 Shihori - Magic Girl !!\\07 - Magic Girl !!.mp3";
        //path = MapLoader.SONGPATH + "172900 Giga-P - Electric Angel\\gamu kun daisuki.mp3";
        path = MapLoader.SONGPATH + "279053 Reol - Streaming Heart\\Streaming Heart.mp3";

        try {
//            JavaDecoder.main(null);

            System.out.println(path);
            javax.media.Player p = Manager.createRealizedPlayer(new File(path).toURL());
            //p.setTimeBase(new SystemTimeBase());
            //System.out.println(p.setRate(1.5f));

            p.addControllerListener(new ControllerListener() {
                @Override
                public void controllerUpdate(ControllerEvent e) {
                    System.out.println(e.toString());
                }
            });

            p.setMediaTime(new Time(999999999L));

            p.start();
            //p.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
