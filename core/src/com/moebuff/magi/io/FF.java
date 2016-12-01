package com.moebuff.magi.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.moebuff.magi.reflect.FieldExtension;
import com.moebuff.magi.reflect.MemberUtils;
import com.moebuff.magi.utils.OS;
import com.moebuff.magi.utils.UnhandledException;

import java.lang.reflect.Field;

/**
 * File Folder
 *
 * @author muto
 */
public class FF {
    public static final FileHandle ROOT;

    public static FileHandle CACHE;
    public static FileHandle SONGS;
    public static FileHandle SKIN;

    static {
        if (OS.isAndroid) {
            ROOT = Gdx.files.external("magi!caga");
            ASSETS = Gdx.files.internal("");
        } else {
            ROOT = Gdx.files.local(".csga");
            ASSETS = internal("");
        }

        for (Field f : FF.class.getFields()) {
            if (MemberUtils.isFinal(f)) continue;

            FileHandle folder = ROOT.child(f.getName().toLowerCase());
            if (!folder.exists()) {
                UnhandledException.validate(mkdirs(folder), "mkdirs失败，无法创建 %s", folder.path());
            }
            //在运行android时会多出$change属性，这个报错会让你一脸蒙蔽，据说只会出现在IDEA中。目前已知的解决办法
            //是关掉Instant Run，具体位置在File->Settings->Build,Execution,Deployment->Instant Run
            FieldExtension.writeField(f, null, folder);
        }
    }

    // 内部资源
    //---------------------------------------------------------------------------------------------

    public static final FileHandle ASSETS;
    public static final FileHandle OSZ_404658 = ASSETS.child("404658 Giga - -BWW SCREAM-.osz");
    public static final FileHandle OSZ_420265 = ASSETS.child("420265 senya - Ondosa.osz");

    public static final FileHandle NC = ASSETS.child("nightcore");
    public static final FileHandle DRUM = NC.child("drum.wav");
    public static final FileHandle CLAP = NC.child("clap.wav");
    public static final FileHandle FINISH = NC.child("finish.wav");

    //---------------------------------------------------------------------------------------------

    /**
     * 创建指定的目录，包括所有必需但不存在的父目录。注意，此操作失败时也可能已经成功地创建了一部分必需的父目录。
     *
     * @param handle to mkdirs
     * @return 如果目录被成功创建则返回true；false表示失败或者目录已存在
     */
    public static boolean mkdirs(FileHandle handle) {
        if (handle.exists()) return false;
        handle.mkdirs();
        return handle.isDirectory();
    }

    public static FileHandle internal(String path) {
        return Handle.getInstance(Gdx.files.internal(path));
    }
}
