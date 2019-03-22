package com.scex.library;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * @author bkw
 */
public class FixDexUtils {
    private static final String TAG = FixDexUtils.class.getSimpleName();
    /**
     * 存放需要修复的dex集合，可能不止一个dex
     */
    private static HashSet<File> dexlist = new HashSet<>();

    /**
     * 加载dex文件
     */
    public static void loadFileDex(Context context) {
        if (context == null) {
            return;
        }
        //添加之前清理已存在的dex文件
        dexlist.clear();
        //获取私有目录下的文件
        File fileDir = context.getDir(Constants.DEX_DIR, Context.MODE_PRIVATE);
        Log.d(TAG, fileDir.getAbsolutePath());
        File[] files = fileDir.listFiles();
        //遍历目录中所有文件
        for (File f : files) {
            if (f.getName().endsWith(Constants.DEX_END) && !"classes.dex".equals(f.getName())) {
                Log.d(TAG, f.getName());
                dexlist.add(f);
            }
        }
        //创建加载器
        createDexClassLoder(context, fileDir);
    }

    /**
     * 创建加载补丁自己的DexClassLoder(自有)
     */
    private static void createDexClassLoder(Context context, File fileDir) {
        //创建临时的解压目录/opt_dex(类修复，需要先解压，再加载java)
        String optimizeDir = fileDir.getAbsolutePath() + File.separator + "opt_dex";
        //目录不存在就创建
        File fopt = new File(optimizeDir);
        if (!fopt.exists()) {
            //创建多级目录
            fopt.mkdirs();
        }

        for (File dex : dexlist) {
            //每遍历一个要修复的dex文件，就需要插装一次
            DexClassLoader classLoader = new DexClassLoader(dex.getAbsolutePath(), optimizeDir, null, context.getClassLoader());
            hotfix(classLoader, context);
        }

    }

    /**
     * 热修复
     *
     * @param classLoader 自己的类加载器
     */
    private static void hotfix(DexClassLoader classLoader, Context context) {
        //获取系统PathClassLoder
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        try {
            //获取自有的dexElements数组对象
            Object myDexElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(classLoader));
            //获取系统的dexElements对象
            Object sysDexElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(pathClassLoader));
            //合并成新的dexElements对象
            Object dexElements = ArraysUtils.combineArrays(myDexElements, sysDexElements);
            //通过反射获取系统的pathList对象
            Object sysPathList = ReflectUtils.getPathList(pathClassLoader);
            //重新赋值给系统的pathList对象
            ReflectUtils.setField(sysPathList, sysPathList.getClass(), dexElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
