package com.moebuff.magi.audio;

/**
 * 播放器接口
 *
 * @author muto
 */
public interface Player {
    /**
     * 播放音乐，若正在播放，则从头开始
     */
    void play();

    /**
     * 从指定的位置播放音乐，单位毫秒
     */
    void play(int position);

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 停止播放，下次播放将从头开始
     */
    void stop();

    /**
     * 解除播放器对文件的占用；本身自带stop()
     */
    void dispose();

    /**
     * 获取当前音量的百分比
     */
    int getVolume();

    /**
     * 用百分比设置音量大小
     */
    void setVolume(int per);

    /**
     * 以毫秒为单位返回当前播放位置
     */
    int getPosition();

    /**
     * 设置当前播放位置，单位毫秒，其中position小于0是没有任何意义的
     */
    void setPosition(int position);

    /**
     * 设置监听器，并在音乐结束后执行
     *
     * @param listener 回调函数
     */
    void setListener(OnCompletionListener listener);

    /**
     * @return 当前的播放状态
     */
    Status getStatus();

    /**
     * Sets whether the music stream is looping. This can be called at any time, whether the stream is playing.
     *
     * @param isLooping whether to loop the stream
     */
    void setLooping(boolean isLooping);

    /**
     * @return whether the music stream is looping.
     */
    boolean isLooping();

    /**
     * @return whether this music stream is playing
     */
    boolean isPlaying();
}
