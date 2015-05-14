package org.moebuff.magi.beatmap;

/**
 * @author MuTo
 */
public class HitObject {
    private String line;

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
