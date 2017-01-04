package com.moebuff.magi.io;

import com.badlogic.gdx.files.FileHandle;
import com.moebuff.magi.reflect.ClassKit;
import com.moebuff.magi.reflect.FieldKit;
import com.moebuff.magi.reflect.LoaderUtil;
import com.moebuff.magi.reflect.MemberUtils;
import com.moebuff.magi.utils.UnhandledException;

import java.lang.reflect.Field;

/**
 * File Folder
 *
 * @author muto
 */
public class FF {
    public static FileHandle CACHE;
    public static FileHandle SONGS;
    public static FileHandle SKIN;

    private static final String LWJGL_GDX_HANDLE_PKG = "com.moebuff.magi.desktop.LwjglGdxHandle";
    private static final String ANDROID_GDX_HANDLE_PKG = "com.moebuff.magi.AndroidGdxHandle";

    public static final FileHandle ROOT;//根目录

    static {
        Class<Catalog> gdxHandleClass;
        try {
            gdxHandleClass = LoaderUtil.loadClass(LWJGL_GDX_HANDLE_PKG);
        } catch (ClassNotFoundException ignored) {
            gdxHandleClass = LoaderUtil.loadClass(ANDROID_GDX_HANDLE_PKG, true);
        }
        Catalog dir = ClassKit.newInstance(gdxHandleClass);
        ROOT = dir.getRoot();
        ASSETS = dir.getAssets();

        for (Field f : FF.class.getFields()) {
            if (MemberUtils.isFinal(f)) continue;

            FileHandle folder = ROOT.child(f.getName().toLowerCase());
            if (!folder.exists()) {
                UnhandledException.validate(mkdirs(folder), "mkdirs失败，无法创建 %s", folder.path());
            }
            //在运行android时会多出$change属性，这个报错会让你一脸蒙蔽，据说只会出现在IDEA中。目前已知的解决办法
            //是关掉Instant Run，具体位置在File->Settings->Build,Execution,Deployment->Instant Run
            FieldKit.writeField(f, null, folder);
        }
    }

    // 内部资源
    //---------------------------------------------------------------------------------------------

    public static final FileHandle ASSETS;
    public static final FileHandle OSZ_404910 = ASSETS.child("404910 sana - Packet Hero.osz");
    public static final FileHandle OSZ_420265 = ASSETS.child("420265 senya - Ondosa.osz");

    public static final FileHandle NC = ASSETS.child("nightcore");
    public static final FileHandle DRUM = NC.child("drum.wav");
    public static final FileHandle CLAP = NC.child("clap.wav");
    public static final FileHandle FINISH = NC.child("finish.wav");

    //---------------------------------------------------------------------------------------------

    /**
     * 创建指定的目录，包括所有必需但不存在的父目录。注意，此操作失败时也可能已经成功地创建了一部分必需的父目录。
     *
     * @param handle 需要创建的目录
     * @return 如果目录被成功创建则返回true；false表示失败或者目录已存在
     */
    public static boolean mkdirs(FileHandle handle) {
        if (handle.exists()) return false;
        handle.mkdirs();
        return handle.isDirectory();
    }
}
