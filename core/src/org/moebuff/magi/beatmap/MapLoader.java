package org.moebuff.magi.beatmap;

import org.moebuff.magi.util.PathUtil;
import org.moebuff.magi.util.StringUtil;

import java.io.File;
import java.util.*;


/**
 * beatmap加载器
 *
 * @author MuTo
 */
public class MapLoader {
    public static final String SONGPATH = PathUtil.addSeparator(PathUtil.USERDIR, "songs");
    private Set<BeatMap> baetMaps = new HashSet();

    public MapLoader() {
        for (String path : new File(SONGPATH).list())
            baetMaps.add(new BeatMap(SONGPATH + path));
    }

    public Set<BeatMap> getBaetMaps() {
        return baetMaps;
    }

    public void setBaetMaps(Set<BeatMap> baetMaps) {
        this.baetMaps = baetMaps;
    }
}
