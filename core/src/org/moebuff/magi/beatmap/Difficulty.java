package org.moebuff.magi.beatmap;

import org.moebuff.magi.util.Reflect;
import org.moebuff.magi.util.Stream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 谱面难度
 *
 * @author MuTo
 */
public class Difficulty {
    @Tag("[General]")//全局设置
    private String audioFilename;//歌曲文件名
    private String audioLeadIn;//歌曲开始延迟时间(0-3000)
    private String previewTime;//浏览歌曲时间（黄线）：-1=从头开始播放歌曲
    private String countdown;//倒计时：0=关闭，1=普通，2=慢速，3=快速
    private String sampleSet;//全局音效：Normal=普通音效，Soft=柔和音效，Drum=打击乐音效
    private String stackLeniency;//叠放物品最小时间差(0.2-1.0)
    private String mode;//0=所有模式（OSU!），1=太鼓，2=CTB，3=mania
    private String letterboxInBreaks;//休息时显示聊天窗口
    private String widescreenStoryboard;//宽屏故事模式
    @Tag("[Editor]")//编辑器设置
    private String bookmarks;//时间标签（蓝线）
    private String distanceSpacing;//默认物品间距
    private String batDivisor;//音符长短：倒数 {1,2,3,4,6,8,12,16}
    private String gridSize;//网格大小{1,2,4,8}
    @Tag("[Metadata]")//歌曲信息
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
    @Tag("[Difficulty]")//难度信息
    private String HPDrainRate;//掉血速度(0-10)
    private String circleSize;//物品大小：数字越小则大(2-7)
    private String overallDifficulty;//总体难度：数字越大则击打判断区间越小(0-10)
    private String approachRate;//出现物品速度：数字越大则出现速度越快(0-10)
    private String sliderMultiplier;//滑条速度
    private String sliderTickRate;//每拍滑条小点个数
    @Tag("[Events]")//事件
    private String bgAndVideo;//背景图片和视频
    private String beakPeriods;//休息时间点
    //故事模式图层Storyboard Layer
    private String sbLayer0;//Background
    private String sbLayer1;//Fail
    private String sbLayer2;//Pass
    private String sbLayer3;//Foreground
    private String sbSound;//故事模式声效层
    private String bgColor;//背景颜色
    @Tag("[TimingPoints]")
    private List<String> timingPoints = new ArrayList();//timing点
    @Tag("[Colours]")
    private List<String> comboColours = new ArrayList();//combo颜色
    @Tag("[HitObjects]")
    private List<String> hitObjects = new ArrayList();//打击物件

    public Difficulty(File diff) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(diff));

            Reflect ref = new Reflect(this.getClass());
            for (String line = ""; line != null; line = reader.readLine()) {
                String[] split = line.split(":");
                if (split.length == 2)
                    ref.invokeSet(split[0].trim(), this, split[1].trim());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Stream.close(reader);
        }
    }

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

    public String getBgAndVideo() {
        return bgAndVideo;
    }

    public void setBgAndVideo(String bgAndVideo) {
        this.bgAndVideo = bgAndVideo;
    }

    public String getBeakPeriods() {
        return beakPeriods;
    }

    public void setBeakPeriods(String beakPeriods) {
        this.beakPeriods = beakPeriods;
    }

    public String getSbLayer0() {
        return sbLayer0;
    }

    public void setSbLayer0(String sbLayer0) {
        this.sbLayer0 = sbLayer0;
    }

    public String getSbLayer1() {
        return sbLayer1;
    }

    public void setSbLayer1(String sbLayer1) {
        this.sbLayer1 = sbLayer1;
    }

    public String getSbLayer2() {
        return sbLayer2;
    }

    public void setSbLayer2(String sbLayer2) {
        this.sbLayer2 = sbLayer2;
    }

    public String getSbLayer3() {
        return sbLayer3;
    }

    public void setSbLayer3(String sbLayer3) {
        this.sbLayer3 = sbLayer3;
    }

    public String getSbSound() {
        return sbSound;
    }

    public void setSbSound(String sbSound) {
        this.sbSound = sbSound;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public List<String> getTimingPoints() {
        return timingPoints;
    }

    public void setTimingPoints(List<String> timingPoints) {
        this.timingPoints = timingPoints;
    }

    public List<String> getComboColours() {
        return comboColours;
    }

    public void setComboColours(List<String> comboColours) {
        this.comboColours = comboColours;
    }

    public List<String> getHitObjects() {
        return hitObjects;
    }

    public void setHitObjects(List<String> hitObjects) {
        this.hitObjects = hitObjects;
    }
}
