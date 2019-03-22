package com.scex.library;

import java.lang.reflect.Array;

public class ArraysUtils {

    /**
     * @param arrayMy 前数组（插队数组）自有
     * @param arraySys 后数组（已有数组） 系统
     */
    public static Object combineArrays(Object arrayMy, Object arraySys) {

        Class<?> localClass = arrayMy.getClass().getComponentType();
        //前数组长度
        int i = Array.getLength(arrayMy);
        //新数组长度=前数组长度+后数组长度
        int j = i + Array.getLength(arraySys);

        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayMy, k));
            } else {
                Array.set(result, k, Array.get(arraySys, k - i));
            }
        }
        return result;
    }
}
