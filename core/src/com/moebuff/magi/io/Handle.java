package com.moebuff.magi.io;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.moebuff.magi.reflect.FieldExtension;
import com.moebuff.magi.reflect.MethodExtension;
import com.moebuff.magi.utils.OS;
import com.moebuff.magi.utils.UnhandledException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 动态代理 {@link FileHandle}
 *
 * @author muto
 */
public class Handle extends FileHandle implements MethodInterceptor {
    private File resource;
    private FileHandle su;//被代理的对象

    protected Handle(String path, FileType type, FileHandle su) {
        super(path, type);
        resource = FileExtension.getResource(path);
        this.su = su;
    }

    protected boolean isLocation() {
        return OS.isDesktop && FileType.Internal == type && !file().exists();
    }

    @Override
    public FileHandle[] list() {
        if (isLocation()) {
            String[] paths = resource.list();
            if (paths == null) return new FileHandle[0];
            FileHandle[] result = new FileHandle[paths.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = child(paths[i]);
            }
            return result;
        }
        return su.list();
    }

    @Override
    public boolean isDirectory() {
        if (isLocation()) {
            return resource.isDirectory();
        }
        return su.isDirectory();
    }

    @Override
    public FileHandle child(String name) {
        return getInstance(su.child(name));
    }

    /**
     * 在 {@link FileHandle#copyTo(FileHandle)} 的基础上，对desktop的internal做了支持
     */
    @Override
    public void copyTo(FileHandle dest) {
        if (isLocation()) {
            if (isDirectory()) {
                copyDirectory(this, dest);
            } else {
                copyFile(this, dest);
            }
            return;
        }
        su.copyTo(dest);
    }

    private static void copyFile(FileHandle source, FileHandle dest) {
        try {
            dest.write(source.read(), false);
        } catch (Exception ex) {
            throw UnhandledException.format(ex, "Error copying source file: %s (%s)\nTo destination: %s (%s)",
                    source.file(), source.type(), dest.file(), dest.type());
        }
    }

    private static void copyDirectory(FileHandle sourceDir, FileHandle destDir) {
        destDir.mkdirs();
        for (FileHandle srcFile : sourceDir.list()) {
            FileHandle destFile = destDir.child(srcFile.name());
            if (srcFile.isDirectory()) {
                copyDirectory(srcFile, destFile);
                continue;
            }
            copyFile(srcFile, destFile);
        }
    }

    //---------------------------------------------------------------------------------------------

    private Field suf;

    private Handle() {
        suf = FieldExtension.getDeclaredField(this.getClass(), "su");
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Method target = MethodExtension.getMethod(this, method);
        if (target == null) {
            Object val = FieldExtension.readField(suf, obj);
            target = MethodExtension.getMethodFromSuper(val, method);
            return MethodExtension.invoke(target, val, args);
        }
        return proxy.invokeSuper(obj, args);
    }

    public static FileHandle getInstance(FileHandle handle) {
        Enhancer en = new Enhancer();
        en.setSuperclass(Handle.class);
        en.setCallback(new Handle());
        return (FileHandle) en.create(new Class[]{
                String.class, FileType.class, FileHandle.class
        }, new Object[]{
                handle.path(), handle.type(), handle
        });
    }
}
