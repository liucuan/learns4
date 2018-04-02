package com.tone.ls4.core.compiler;

import java.lang.reflect.Method;

/**
 * @author zhaoxiang.liu
 * @date 2018/3/28
 */
public class SampleTest {
    public static void main(String[] args) {
        String classDataRootPath = "D:\\dev\\github\\learns4\\target";
        FileSystemClassLoader fscl1 = new FileSystemClassLoader(classDataRootPath);
        FileSystemClassLoader fscl2 = new FileSystemClassLoader(classDataRootPath);
        String className = "com.tone.ls4.core.compiler.Sample";
        try {
            Class<?> class1 = fscl1.loadClass(className);
            Object obj1 = class1.newInstance();
            Class<?> class2 = fscl2.loadClass(className);
            Object obj2 = class2.newInstance();
            Method setSampleMethod = class1.getMethod("setSample", java.lang.Object.class);
            setSampleMethod.invoke(obj1, obj2);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
