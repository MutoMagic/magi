package org.moebuff.magi.beatmap;

/**
 * 打击物件
 *
 * @author MuTo
 */
public class HitObject {
    private String line;//[数字1],[数字2],[数字3],[数字4],[数字5],<滑条描述><转盘描述>[数字6]:[数字7]:[数字8]:[数字9]:
    private String x;//数字1：物品（起始）横坐标
    private String y;//数字2：物品（起始）纵坐标
    private String startTime;//数字3：物品（起始）时间
    private String type;//数字4：物品类型（请整除16——商：跳过新颜色数量，余数：1=圆圈，2=滑条，8=转盘，其余值为新颜色并请-4按1,2,8处理）
    private String soundEffect;//数字5：总体音效（请整除2——余数：1357=哨声，2367=击钹，4567=拍手）
    private String soundType;//数字6：音效类别（0=默认，1=Normal，2=Soft，3=Drum）
    private String num7;//数字7：自定义音效（0=默认）
    private String num8;//数字8
    private String num9;//数字9

    public HitObject(String line) {
        this.line = line;
    }

    // Properties
    // -------------------------------------------------------------------------

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
