package com.tone.ls4.core.compiler;

/**
 * 类加载器的树状组织结构
 */
public class ClassLoaderTree {
    public static void main(String[] args) {
        ClassLoader classLoaderTree = ClassLoaderTree.class.getClassLoader();
        while (classLoaderTree != null) {
            System.out.println(classLoaderTree);
            classLoaderTree = classLoaderTree.getParent();
        }
    }
}
