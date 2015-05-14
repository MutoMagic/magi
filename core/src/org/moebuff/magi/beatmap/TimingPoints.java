package org.moebuff.magi.beatmap;

/**
 * Timing点
 *
 * @author MuTo
 */
public class TimingPoints {
    private String line;

    public TimingPoints(String line) {
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
