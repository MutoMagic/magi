package com.moebuff.magi.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.moebuff.magi.io.Catalog;
import com.moebuff.magi.io.FileKit;
import com.moebuff.magi.reflect.MethodKit;
import com.moebuff.magi.reflect.Proxy;
import com.moebuff.magi.utils.UnhandledException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.File;
import java.lang.reflect.Method;

/**
 * 代理 {@link FileHandle}，由于 apk 文件结构不同，所以 Android 不支持 cglib。
 *
 * @author muto
 */
public class LwjglGdxHandle extends FileHandle implements MethodInterceptor, Proxy, Catalog {
    private File resource;
    private FileHandle su;

    protected LwjglGdxHandle(String path, FileType type, FileHandle su) {
        super(path, type);
        resource = FileKit.getResource(path);
        this.su = su;
    }

    protected boolean isLocation() {
        return FileType.Internal == type && !file().exists();
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

    public LwjglGdxHandle() {
        // Be used in Class.newInstance();
    }

    private LwjglGdxHandle(FileHandle su) {
        this.su = su;
    }

    /**
     * 重写的方法会被代理，未重写的则执行原有方法；原有表示被代理的对象。
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Class declaringClass = method.getDeclaringClass();
        if (declaringClass == LwjglGdxHandle.class) {
            return proxy.invokeSuper(obj, args);
        }
        Method original = MethodKit.getMethodFromSuper(su, method);
        return MethodKit.invoke(original, su, args);
    }

    @Override
    public <T> T getInstance(T obj) {
        FileHandle handle = (FileHandle) obj;
        Enhancer en = new Enhancer();
        en.setSuperclass(LwjglGdxHandle.class);
        en.setCallback(new LwjglGdxHandle(handle));
        //noinspection unchecked
        return (T) en.create(new Class[]{
                String.class, FileType.class, FileHandle.class
        }, new Object[]{
                handle.path(), handle.type(), handle
        });
    }

    @Override
    public FileHandle getRoot() {
        return Gdx.files.local(".csga");
    }

    @Override
    public FileHandle getAssets() {
        return getInstance(Gdx.files.internal(""));
    }
}
