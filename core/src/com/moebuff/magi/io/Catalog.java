package com.moebuff.magi.io;

import com.badlogic.gdx.files.FileHandle;

/**
 * 与目录相关的操作，用于隔离不同平台的实现
 *
 * @author muto
 */
public interface Catalog {
    /**
     * 获取根目录，区别于内部资源，该目录可被用户访问
     */
    FileHandle getRoot();

    /**
     * @return 内部资源所在的根目录
     */
    FileHandle getAssets();
}
