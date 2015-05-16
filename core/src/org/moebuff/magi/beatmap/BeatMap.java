package org.moebuff.magi.beatmap;

import org.moebuff.magi.util.FileUtil;
import org.moebuff.magi.util.PathUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * BeatMap
 *
 * @author MuTo
 */
public class BeatMap {
    public static final String DIFFEXTNAME = "osu";

    private String path;//文件路径
    private String name;//beatmap名称
    private Set<Difficulty> diffs = new HashSet();//谱面难度
    private Map<Difficulty, String> musicMap = new HashMap();

    public BeatMap(String path) {
        this.path = path;

        File dir = new File(path);
        name = dir.getName();
        //筛选铺面难度
        File[] diffFiles = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (FileUtil.getExtensionName(name).equals(DIFFEXTNAME))
                    return true;
                return false;
            }
        });
        for (File f : diffFiles) {
            Difficulty diff = Difficulty.kit.read(f);
            diffs.add(diff);
            musicMap.put(diff, PathUtil.addSeparator(path, diff.getAudioFilename()));
        }
    }

    public String[] getBg(Difficulty d) {
        String[] bg = d.getBgAndVideo().get(0).split(",");
        bg[2] = bg[2].substring(1, bg[2].length() - 1);
        return bg;
    }

    public String[] getBgVideo(Difficulty d) {
        return d.getBgAndVideo().get(1).split(",");
    }

    // Properties
    // -------------------------------------------------------------------------

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Difficulty> getDiffs() {
        return diffs;
    }

    public void setDiffs(Set<Difficulty> diffs) {
        this.diffs = diffs;
    }

    public Map<Difficulty, String> getMusicMap() {
        return musicMap;
    }

    public void setMusicMap(Map<Difficulty, String> musicMap) {
        this.musicMap = musicMap;
    }
}
