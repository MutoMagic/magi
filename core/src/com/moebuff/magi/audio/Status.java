package com.moebuff.magi.audio;

/**
 * 播放状态；-1.dispose 0.stop 1.play 2.pause
 *
 * @author muto
 */
public enum Status {
    DISPOSE(-1), STOP(0), PLAY(1), PAUSE(2);

    private int value;

    Status(int value) {
        this.value = value;
    }

    public boolean equals(Status s) {
        return this.value == s.value;
    }

    /**
     * 是否已经播放，当状态为 play/pause 时返回true
     */
    public boolean isPlayed() {
        return equals(PLAY) || equals(PAUSE);
    }

    /**
     * 是否正在播放，当状态为 play 时返回true
     */
    public boolean isPlaying() {
        return equals(PLAY);
    }
}
