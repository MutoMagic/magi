package com.moebuff.magi.utils;

import com.moebuff.magi.reflect.FieldExtension;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 取消后又可重复执行的任务
 *
 * @author muto
 */
public abstract class TimerTasks extends TimerTask {
    private Field state;
    private Object virgin;

    public TimerTasks() {
        state = FieldExtension.getDeclaredField(TimerTask.class, "state");
        virgin = FieldExtension.readStaticField(TimerTask.class, "VIRGIN");
    }

    /**
     * 在执行 {@link TimerTask#cancel()} 后，复位TimerTask
     *
     * @param t 执行该任务的Timer
     * @return 取消任务所给出的返回值
     */
    public boolean purge(Timer t) {
        boolean result = super.cancel();
        t.purge();//移除所有已取消的任务
        FieldExtension.writeField(state, this, virgin);//复位
        return result;
    }
}
