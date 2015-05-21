package org.moebuff.magi.audio;

import org.moebuff.magi.beatmap.MapLoader;

import javax.media.*;
import javax.media.protocol.DataSource;
import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MuTo on 2015/5/18.
 */
public class Player {
    Object sync = new Object();

    public static void main(String[] args) {
        String path = MapLoader.SONGPATH + "72217 Zips - Heisei Cataclysm\\IA  Heisei Kataku.mp3";
        //path = MapLoader.SONGPATH + "38592 Iguchi Yuka - Mirai Shoot\\Mirai Shoot.mp3";
        path = MapLoader.SONGPATH + "53519 Shihori - Magic Girl !!\\07 - Magic Girl !!.mp3";
        //path = MapLoader.SONGPATH + "172900 Giga-P - Electric Angel\\gamu kun daisuki.mp3";
        //path = MapLoader.SONGPATH + "279053 Reol - Streaming Heart\\Streaming Heart.mp3";
        path = MapLoader.SONGPATH + "51065 Last Note - Setsuna Trip (Short Ver)\\Satsuna Trip (Short Ver.).mp3";

        try {
//            JavaDecoder.main(null);

            System.out.println(path);
            final javax.media.Player p = Manager.createRealizedPlayer(new File(path).toURL());
            //p.setTimeBase(new SystemTimeBase());


            final Thread t = new Thread() {
                long i = 0;
                long old = 0;
                long now = 0;

                @Override
                public void run() {
                    try {
                        old = new Date().getTime();
                        while (true) {
                            now = new Date().getTime();
                            i += now - old;
                            old = now + 1;
                            System.out.println(++i / 1000.0);
                            sleep(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            p.addControllerListener(new ControllerListener() {
                @Override
                public void controllerUpdate(ControllerEvent e) {
                    System.out.println(e);
                    if (e instanceof PrefetchCompleteEvent) {
                        System.out.println(p.setRate(1.0f));
                        p.start();
                        t.start();
                    }
                    if (e instanceof EndOfMediaEvent) {
                        p.close();
                        t.stop();;
                    }
                }
            });


            //p.setMediaTime(new Time(1D));

            p.prefetch();


//            p.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
