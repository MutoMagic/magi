package com.moebuff.magi.io;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.moebuff.magi.reflect.MethodKit;
import com.moebuff.magi.utils.Log;
import com.moebuff.magi.utils.OS;
import com.moebuff.magi.utils.UnhandledException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.File;
import java.lang.reflect.Method;

/**
 * 代理 {@link FileHandle}。不支持 Android
 *
 * @author muto
 */
public class Handle extends FileHandle implements MethodInterceptor {
    private File resource;
    private FileHandle su;

    protected Handle(String path, FileType type, FileHandle su) {
        super(path, type);
        resource = FileKit.getResource(path);
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
            throw UnhandledException.format("Error copying source file: %s (%s)\nTo destination: %s (%s)",
                    ex, source.file(), source.type(), dest.file(), dest.type());
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

    private Handle(FileHandle su) {
        this.su = su;
    }

    /**
     * 重写的方法会被代理，未重写的则执行原有方法；原有表示被代理的对象。
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Class declaringClass = method.getDeclaringClass();
        Log.d(declaringClass.getName());
        if (declaringClass == Handle.class) {
            return proxy.invokeSuper(obj, args);
        }
        Method original = MethodKit.getMethodFromSuper(su, method);
        return MethodKit.invoke(original, su, args);
    }

    /**
     * 创建动态代理
     *
     * @param handle 被代理的对象
     * @return 代理后的对象
     */
    public static FileHandle getInstance(FileHandle handle) {
        Enhancer en = new Enhancer();
        en.setSuperclass(Handle.class);
        en.setCallback(new Handle(handle));
        return (FileHandle) en.create(new Class[]{
                String.class, FileType.class, FileHandle.class
        }, new Object[]{
                handle.path(), handle.type(), handle
        });
    }
}
