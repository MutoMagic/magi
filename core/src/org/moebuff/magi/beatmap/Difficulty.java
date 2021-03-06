package org.moebuff.magi.beatmap;

import org.moebuff.magi.util.Reflect;
import org.moebuff.magi.util.StringUtil;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 谱面难度
 *
 * @author MuTo
 */
public class Difficulty extends ResolverKit<Difficulty> {
    public static final Difficulty kit = new Difficulty();

    @Section("General")//全局设置
    private String audioFilename;//歌曲文件名
    private String audioLeadIn;//歌曲开始延迟时间(0-3000)
    private String previewTime;//浏览歌曲时间（黄线）：-1=从头开始播放歌曲
    private String countdown;//倒计时：0=关闭，1=普通，2=慢速，3=快速
    private String sampleSet;//全局音效：Normal=普通音效，Soft=柔和音效，Drum=打击乐音效
    private String stackLeniency;//叠放物品最小时间差(0.2-1.0)
    private String mode;//0=所有模式（OSU!），1=太鼓，2=CTB，3=mania
    private String letterboxInBreaks;//休息时显示聊天窗口
    private String widescreenStoryboard;//宽屏故事模式
    @Section("Editor")//编辑器设置
    private String bookmarks;//时间标签（蓝线）
    private String distanceSpacing;//默认物品间距
    private String batDivisor;//音符长短：倒数 {1,2,3,4,6,8,12,16}
    private String gridSize;//网格大小{1,2,4,8}
    @Section("Metadata")//歌曲信息
    private String title;//标题
    private String titleUnicode;//Unicode标题
    private String artist;//作曲家
    private String artistUnicode;//Unicode作曲家
    private String creator;//作者
    private String version;//难度
    private String source;//歌曲来源来源
    private String tags;//标签（搜索歌曲时用）
    private String beatmapID;//谱面编号(http://osu.ppy.sh/b/编号)
    private String beatmapSetID;//歌曲编号(http://osu.ppy.sh/s/编号)
    @Section("Difficulty")//难度信息
    private String HPDrainRate;//掉血速度(0-10)
    private String circleSize;//物品大小：数字越小则大(2-7)
    private String overallDifficulty;//总体难度：数字越大则击打判断区间越小(0-10)
    private String approachRate;//出现物品速度：数字越大则出现速度越快(0-10)
    private String sliderMultiplier;//滑条速度
    private String sliderTickRate;//每拍滑条小点个数
    @Section(value = "Events", resolver = EventsResolver.class, keepNotes = true)//事件
    private List<String> bgAndVideo = new ArrayList();//背景图片和视频
    private List<String> beakPeriods = new ArrayList();//休息时间点
    //故事模式图层Storyboard Layer
    private List<String> sbLayer0 = new ArrayList();//Background
    private List<String> sbLayer1 = new ArrayList();//Fail
    private List<String> sbLayer2 = new ArrayList();//Pass
    private List<String> sbLayer3 = new ArrayList();//Foreground
    private List<String> sbSound = new ArrayList();//故事模式声效层
    private List<String> bgColor = new ArrayList();//背景颜色
    @Section("TimingPoints")
    private List<String> timingPoints = new ArrayList();//timing点
    private List<TimingPoints> timingPoints_obj = new ArrayList();
    @Section("Colours")
    private List<String> comboColours = new ArrayList();//combo颜色
    private List<int[]> comboColours_obj = new ArrayList();//rgb array
    @Section(value = "HitObjects")
    private List<String> hitObjects = new ArrayList();//打击物件
    private List<HitObject> hitObjects_obj = new ArrayList();

    public Difficulty() {
    }

    public Difficulty(Class type, Reflect reflect, HashMap sections, HashMap sectionFields) {
        super(type, reflect, sections, sectionFields);
    }

    public String[] getBg() {
        String[] bg = bgAndVideo.get(0).split(",");
        bg[2] = bg[2].substring(1, bg[2].length() - 1);
        return bg;
    }

    public String[] getBgVideo() {
        return bgAndVideo.get(1).split(",");
    }

    // Properties
    // -------------------------------------------------------------------------

    public String getAudioFilename() {
        return audioFilename;
    }

    public void setAudioFilename(String audioFilename) {
        this.audioFilename = audioFilename;
    }

    public String getAudioLeadIn() {
        return audioLeadIn;
    }

    public void setAudioLeadIn(String audioLeadIn) {
        this.audioLeadIn = audioLeadIn;
    }

    public String getPreviewTime() {
        return previewTime;
    }

    public void setPreviewTime(String previewTime) {
        this.previewTime = previewTime;
    }

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }

    public String getSampleSet() {
        return sampleSet;
    }

    public void setSampleSet(String sampleSet) {
        this.sampleSet = sampleSet;
    }

    public String getStackLeniency() {
        return stackLeniency;
    }

    public void setStackLeniency(String stackLeniency) {
        this.stackLeniency = stackLeniency;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLetterboxInBreaks() {
        return letterboxInBreaks;
    }

    public void setLetterboxInBreaks(String letterboxInBreaks) {
        this.letterboxInBreaks = letterboxInBreaks;
    }

    public String getWidescreenStoryboard() {
        return widescreenStoryboard;
    }

    public void setWidescreenStoryboard(String widescreenStoryboard) {
        this.widescreenStoryboard = widescreenStoryboard;
    }

    public String getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(String bookmarks) {
        this.bookmarks = bookmarks;
    }

    public String getDistanceSpacing() {
        return distanceSpacing;
    }

    public void setDistanceSpacing(String distanceSpacing) {
        this.distanceSpacing = distanceSpacing;
    }

    public String getBatDivisor() {
        return batDivisor;
    }

    public void setBatDivisor(String batDivisor) {
        this.batDivisor = batDivisor;
    }

    public String getGridSize() {
        return gridSize;
    }

    public void setGridSize(String gridSize) {
        this.gridSize = gridSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleUnicode() {
        return titleUnicode;
    }

    public void setTitleUnicode(String titleUnicode) {
        this.titleUnicode = titleUnicode;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistUnicode() {
        return artistUnicode;
    }

    public void setArtistUnicode(String artistUnicode) {
        this.artistUnicode = artistUnicode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBeatmapID() {
        return beatmapID;
    }

    public void setBeatmapID(String beatmapID) {
        this.beatmapID = beatmapID;
    }

    public String getBeatmapSetID() {
        return beatmapSetID;
    }

    public void setBeatmapSetID(String beatmapSetID) {
        this.beatmapSetID = beatmapSetID;
    }

    public String getHPDrainRate() {
        return HPDrainRate;
    }

    public void setHPDrainRate(String HPDrainRate) {
        this.HPDrainRate = HPDrainRate;
    }

    public String getCircleSize() {
        return circleSize;
    }

    public void setCircleSize(String circleSize) {
        this.circleSize = circleSize;
    }

    public String getOverallDifficulty() {
        return overallDifficulty;
    }

    public void setOverallDifficulty(String overallDifficulty) {
        this.overallDifficulty = overallDifficulty;
    }

    public String getApproachRate() {
        return approachRate;
    }

    public void setApproachRate(String approachRate) {
        this.approachRate = approachRate;
    }

    public String getSliderMultiplier() {
        return sliderMultiplier;
    }

    public void setSliderMultiplier(String sliderMultiplier) {
        this.sliderMultiplier = sliderMultiplier;
    }

    public String getSliderTickRate() {
        return sliderTickRate;
    }

    public void setSliderTickRate(String sliderTickRate) {
        this.sliderTickRate = sliderTickRate;
    }

    public List<String> getBgAndVideo() {
        return bgAndVideo;
    }

    public void setBgAndVideo(List<String> bgAndVideo) {
        this.bgAndVideo = bgAndVideo;
    }

    public List<String> getBeakPeriods() {
        return beakPeriods;
    }

    public void setBeakPeriods(List<String> beakPeriods) {
        this.beakPeriods = beakPeriods;
    }

    public List<String> getSbLayer0() {
        return sbLayer0;
    }

    public void setSbLayer0(List<String> sbLayer0) {
        this.sbLayer0 = sbLayer0;
    }

    public List<String> getSbLayer1() {
        return sbLayer1;
    }

    public void setSbLayer1(List<String> sbLayer1) {
        this.sbLayer1 = sbLayer1;
    }

    public List<String> getSbLayer2() {
        return sbLayer2;
    }

    public void setSbLayer2(List<String> sbLayer2) {
        this.sbLayer2 = sbLayer2;
    }

    public List<String> getSbLayer3() {
        return sbLayer3;
    }

    public void setSbLayer3(List<String> sbLayer3) {
        this.sbLayer3 = sbLayer3;
    }

    public List<String> getSbSound() {
        return sbSound;
    }

    public void setSbSound(List<String> sbSound) {
        this.sbSound = sbSound;
    }

    public List<String> getBgColor() {
        return bgColor;
    }

    public void setBgColor(List<String> bgColor) {
        this.bgColor = bgColor;
    }

    public List<String> getTimingPoints() {
        return timingPoints;
    }

    public void setTimingPoints(List<String> timingPoints) {
        this.timingPoints = timingPoints;
    }

    public List<TimingPoints> getTimingPoints_obj() {
        return timingPoints_obj;
    }

    public void setTimingPoints_obj(List<TimingPoints> timingPoints_obj) {
        this.timingPoints_obj = timingPoints_obj;
    }

    public List<String> getComboColours() {
        return comboColours;
    }

    public void setComboColours(List<String> comboColours) {
        this.comboColours = comboColours;
    }

    public List<int[]> getComboColours_obj() {
        return comboColours_obj;
    }

    public void setComboColours_obj(List<int[]> comboColours_obj) {
        this.comboColours_obj = comboColours_obj;
    }

    public List<String> getHitObjects() {
        return hitObjects;
    }

    public void setHitObjects(List<String> hitObjects) {
        this.hitObjects = hitObjects;
    }

    public List<HitObject> getHitObjects_obj() {
        return hitObjects_obj;
    }

    public void setHitObjects_obj(List<HitObject> hitObjects_obj) {
        this.hitObjects_obj = hitObjects_obj;
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    @Override
    protected void endOfAssignment(Difficulty t) {
        for (String line : t.timingPoints)
            t.timingPoints_obj.add(new TimingPoints(line));
        for (String line : t.comboColours)
            t.comboColours_obj.add(StringUtil.toInt(line.split(":")[1].trim().split(",")));
        for (String line : t.hitObjects)
            t.hitObjects_obj.add(new HitObject(line));
    }

    // Internal
    // -------------------------------------------------------------------------

    public static class EventsResolver extends DefaultResolver {
        @Override
        public void analyze(Object obj, String name, List<Field> fields, Map<String, String> attrs) throws Exception {
            for (int i = 0, j = -1; ; i++) {
                String line = attrs.get(StringUtil.arrayStyle(name, i));
                if (line == null)
                    break;

                if (line.matches("^//.*$"))
                    j++;
                else if (j != -1)
                    getFieldList(fields.get(j), obj).add(line);
            }
        }
    }
}
