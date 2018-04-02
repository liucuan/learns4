package com.tone.ls4.core.compiler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.tools.*;

public class CompilerTest {
    public static void main(String[] args) throws Exception {
        String source = "public class Main { public static void main(String[] args) {System.out.println(\"Hello World!\");} }";
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        StringSourceJavaObject sourceObject = new CompilerTest.StringSourceJavaObject("Main", source);
        Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(sourceObject);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, fileObjects);
        boolean result = task.call();
        if(result) {
            System.out.println("编译成功。");
            ClassLoader loader = CompilerTest.class.getClassLoader();
            try {
                Class<?> clazz = loader.loadClass("Main");
                Method method = clazz.getMethod("main", new Class<?>[] {});
                Object value = method.invoke(null, new Object[] {});
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class StringSourceJavaObject extends SimpleJavaFileObject {
        private String content = null;

        public StringSourceJavaObject(String name, String content) throws URISyntaxException {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return content;
        }
    }
}