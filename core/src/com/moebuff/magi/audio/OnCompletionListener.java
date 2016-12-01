package com.moebuff.magi.audio;

import com.badlogic.gdx.audio.Music;

/**
 * Player改变了Music原有逻辑，接口 {@link Music.OnCompletionListener} 已过时
 *
 * @author muto
 */
@FunctionalInterface
public interface OnCompletionListener {
    /**
     * 在播放结束后调用
     *
     * @param player 控制音乐到达文件结尾的播放器
     * @return 是否循环播放，该设置将在下次播放时生效
     */
    boolean onCompletion(Player player);
}
