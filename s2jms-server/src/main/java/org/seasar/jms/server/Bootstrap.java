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
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bowez
 * 
 */
public class Bootstrap {
    static final String DEFAULT_DICON_FILE = "app.dicon";
    static final String JMS_CONTAINER_INITIALIZER = "org.seasar.jms.container.impl.JMSContainerInitializer";
    static final String BOOTSTRAP_CLASS_FILE_NAME = Bootstrap.class.getName().replace('.', '/')
            + ".class";

    private static final Logger logger = Logger.getLogger(Bootstrap.class.getName(),
            "JMS-ServerMessages");

    protected Object s2container;
    protected CountDownLatch latch = new CountDownLatch(1);

    public static void main(final String[] args) {
        logger.log(Level.INFO, "IJMS-SERVER3000");
        try {
            new Bootstrap().run(args);
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "EJMS-SERVER3001", e);
            System.exit(1);
        }
    }

    protected void run(final String[] args) throws Exception {
        final String dicon = getDicon(args);
        final String classpathArg = getClasspath(args);
        setupClasspath(classpathArg.split(File.pathSeparator));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                destoryS2Container();
                latch.countDown();
            }
        });
        s2container = createS2Container(dicon);

        try {
            latch.await();
        } catch (final InterruptedException ignore) {
            destoryS2Container();
        }
    }

    protected Object createS2Container(final String dicon) throws Exception {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final Class clazz = classLoader.loadClass(JMS_CONTAINER_INITIALIZER);
        final Constructor ctor = clazz.getConstructor(String.class);
        final Callable jmsContainerInitializer = Callable.class.cast(ctor.newInstance(dicon));
        return jmsContainerInitializer.call();
    }

    protected void destoryS2Container() {
        logger.log(Level.INFO, "IJMS-SERVER3002");
        try {
            if (s2container != null) {
                final Method destoroy = s2container.getClass().getMethod("destroy");
                destoroy.invoke(s2container);
            }
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "EJMS-SERVER3003", e);
        }
    }

    protected String getDicon(final String[] args) throws IllegalArgumentException {
        final String dicon = getArg("--dicon", args);
        return dicon.equals("") ? DEFAULT_DICON_FILE : dicon;
    }

    protected String getClasspath(final String[] args) throws IllegalArgumentException {
        final String classpath = getArg("--classpath", args);
        return classpath.equals("") ? "." : classpath;
    }

    protected String getArg(final String name, final String[] args) {
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

    protected void setupClasspath(final String[] pathStrings) throws IOException {
        final List<URL> urls = new ArrayList<URL>();

        for (final String pathStr : pathStrings) {
            addPath(urls, new File(pathStr));
        }

        final File bootstrapJarFile = getBootstrapJarFile();
        if (bootstrapJarFile != null) {
            for (final File file : getJarFiles(bootstrapJarFile.getParentFile())) {
                addPath(urls, file);
            }
        }

        final ClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    protected File getBootstrapJarFile() {
        try {
            final URL url = getClass().getClassLoader().getResource(BOOTSTRAP_CLASS_FILE_NAME);
            final JarURLConnection con = (JarURLConnection) url.openConnection();
            return new File(con.getJarFileURL().toURI());
        } catch (final Exception e) {
            return null;
        }
    }

    protected void addPath(final List<URL> urls, final File path) throws IOException {
        if (path.isDirectory()) {
            final File[] jarFiles = getJarFiles(path);
            if (0 < jarFiles.length) {
                for (final File jar : jarFiles) {
                    urls.add(new URL("jar:" + jar.toURL().toExternalForm() + "!/"));
                    logger.log(Level.INFO, "IJMS-SERVER3004", path.getCanonicalPath());
                }
            } else {
                urls.add(path.toURL());
                logger.log(Level.INFO, "IJMS-SERVER3004", path.getCanonicalPath());
            }
        } else {
            if (isJar(path)) {
                urls.add(path.toURL());
                logger.log(Level.INFO, "IJMS-SERVER3004", path.getCanonicalPath());
            }
        }
    }

    protected File[] getJarFiles(final File dir) {
        return dir.listFiles(new FileFilter() {
            public boolean accept(final File pathname) {
                return isJar(pathname);
            }
        });
    }

    protected boolean isJar(final File pathname) {
        final int dot = pathname.getName().lastIndexOf('.');
        if (0 <= dot) {
            final String extention = pathname.getName().substring(dot + 1);
            return "jar".equalsIgnoreCase(extention);
        }
        return false;
    }

}
