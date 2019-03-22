package com.scex.library;

import java.lang.reflect.Field;

/**
 * 反射工具类
 *
 * @author bkw
 */
public class ReflectUtils {

    /**
     * 通过反射获取某对象，并设置私有可访问
     *
     * @param object 该属性所属类的对象
     * @param clazz  该属性所属类
     * @param field  属性名
     */
    private static Object getField(Object object, Class<?> clazz, String field) throws NoSuchFieldException, IllegalAccessException {
        Field localField = clazz.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(object);
    }

    /**
     * 给某属性赋值，并设置私有可访问
     *
     * @param object 该属性所属类的对象
     * @param clazz  该属性所属类
     * @param value  值
     */
    public static void setField(Object object, Class<?> clazz, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField("dexElements");
        field.setAccessible(true);
        field.set(object, value);
    }

    /**
     * 通过反射获取BaseDexClassLoder对象中的pathList对象
     *
     * @param baseDexClassLoder BaseDexClassLoder对象
     */
    public static Object getPathList(Object baseDexClassLoder) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return getField(baseDexClassLoder, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    /**
     * 通过反射获取BaseDexClassLoder对象中的PathList对象，再获取pathList中的dexElement对象
     *
     * @param o pathList对象
     */
    public static Object getDexElements(Object o) throws NoSuchFieldException, IllegalAccessException {
        return getField(o, o.getClass(), "dexElements");
    }


}
