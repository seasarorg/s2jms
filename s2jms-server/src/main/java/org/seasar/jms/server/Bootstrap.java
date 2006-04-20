/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.jms.server;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bowez
 * 
 */
public class Bootstrap {
    static final String DEFAULT_DICON_FILE = "app.dicon";
    static final String S2_CONTAINER_FACTORY = "org.seasar.framework.container.factory.S2ContainerFactory";
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            new Bootstrap().run(args);
            System.exit(0);
        }
        catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    synchronized void run(final String[] args) throws Exception {
        final String dicon = getDicon(args);
        final String classpathArg = getClasspath(args);
        setupClasspath(classpathArg.split(File.pathSeparator));
        final Object s2container = createS2Container(dicon);
        final Method init = s2container.getClass().getDeclaredMethod("init");
        final Method destoroy = s2container.getClass().getDeclaredMethod("destroy");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    destoroy.invoke(s2container);
                } 
                catch (final IllegalAccessException e) {
                    e.printStackTrace();
                } 
                catch (final InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        init.invoke(s2container);
        try {
            wait();
        } 
        catch (final InterruptedException ignore) {
        }
    }
    
    Object createS2Container(String dicon) throws Exception {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final Class s2ContainerFactoryClass = classLoader.loadClass(S2_CONTAINER_FACTORY);
        final Method createMethod = s2ContainerFactoryClass.getDeclaredMethod("create", String.class);
        return createMethod.invoke(s2ContainerFactoryClass, dicon);
    }
    
    String getDicon(final String[] args) throws IllegalArgumentException {
        final String dicon = getArg("--dicon", args);
        return dicon.equals("") ? DEFAULT_DICON_FILE : dicon;
    }
    
    String getClasspath(final String[] args) throws IllegalArgumentException {
        final String classpath = getArg("--classpath", args);
        return classpath.equals("") ? "." : classpath;
    }
    
    String getArg(final String name, final String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(name)) {
                if (i + 1 < args.length) {
                    return args[i + 1];
                }
                throw new IllegalArgumentException(Arrays.toString(args));
            }
        }
        return "";
    }
    
    void setupClasspath(final String[] pathStrings) throws MalformedURLException {
        final List<URL> urls = new ArrayList<URL>();
        for (final String pathStr : pathStrings) {
            final File path = new File(pathStr);
            if (path.isDirectory()) {
                final File[] jarFiles = getJarFiles(path);
                if (0 < jarFiles.length) {
                    for (File jar : jarFiles) {
                        urls.add(new URL("jar:" + jar.toURL().toString() + "!/"));
                    }
                }
                else {
                    urls.add(path.toURL());
                }
            }
            else {
                if (isJar(path)) {
                    urls.add(path.toURL());
                }
            }
        }
        final ClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
        Thread.currentThread().setContextClassLoader(classLoader);
    }
    
    File[] getJarFiles(final File dir) {
        return dir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return isJar(pathname);
            }
        });
    }
    
    boolean isJar(final File pathname) {
        final int dot = pathname.getName().lastIndexOf('.');
        if (0 <= dot) {
            final String extention = pathname.getName().substring(dot + 1);
            return "jar".equalsIgnoreCase(extention);
        }
        return false;
    }
    
}
