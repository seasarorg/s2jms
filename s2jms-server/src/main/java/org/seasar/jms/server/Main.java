/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
 * S2JMS-Containerを利用したアプリケーションをサーバプロセスとして起動するブートストラップを提供するクラスです。
 * <p>
 * このクラスはS2JMS-Serverの提供する実行可能JarにおいてMain-Clasｓに設定されることにより、
 * S2JMSサーバプロセスのエントリポイントとなります。
 * </p>
 * <p>
 * S2JMS-Serverはこのクラス自身を含むS2JMS-ServerのJarファイルと同じ位置にあるJarファイルをクラスパスに追加します。
 * </p>
 * <p>
 * S2JMS－Serverを起動する際にコマンドライン引数として以下の指定を行うことができます。
 * </p>
 * <dl>
 * <dt><code>--classpath <var>classpaths</var></code></dt>
 * <dd>追加のクラスパスを指定します。 <code><var>classpaths</var></code>はプラットフォームのパス区切り文字を使って複数のパスを指定することができます。
 * 追加されたクラスパスは標準のクラスパスより前に設定されます。<br>
 * 指定されたパスがディレクトリの場合、そのディレクトリ直下にJarファイルが含まれていればそれらのJarファイルが全てクラスパスに追加されます。
 * ディレクトリ直下にJarファイルが含まれていなければそのディレクトリがクラスパスに追加されます。
 * 指定されたパスがファイルの場合はそのファイルがクラスパスに追加されます。</dd>
 * <dt><code>--dicon <var>diconfile</var></code></dt>
 * <dd>ルートとなるdiconファイルを指定します。省略すると<code>app.dicon</code>になります。</dd>
 * </dl>
 * 
 * @author bowez
 */
public class Main {

    // constants
    /** ルートdiconファイルのデフォルト名 */
    protected static final String DEFAULT_DICON_FILE = "app.dicon";

    /** S2JMS-Containerの初期化クラス名 */
    protected static final String JMS_CONTAINER_INITIALIZER = "org.seasar.jms.container.impl.JMSContainerInitializer";

    /** このクラスのクラスファイル名 */
    protected static final String BOOTSTRAP_CLASS_FILE_NAME = Main.class.getName()
            .replace('.', '/')
            + ".class";

    // static fields
    private static final Logger logger = Logger.getLogger(Main.class.getName(),
            "JMS-SERVERMessages");

    /** シャットダウンフックと同期を取るためのラッチ */
    protected static CountDownLatch latch = new CountDownLatch(1);

    // instance fields
    /** S2コンテナ */
    protected Object s2container;

    /**
     * S2JMS-Serverプロセスを開始します。
     * 
     * @param args
     *            コマンドライン引数
     */
    public static void main(final String[] args) {
        logger.log(Level.INFO, "IJMS-SERVER3000");
        try {
            new Main().run(args);
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "EJMS-SERVER3005", e);
            System.exit(1);
        }
    }

    /**
     * S2JMS-Serverプロセスを停止します。
     * 
     */
    public static void stop() {
        latch.countDown();
    }

    /**
     * S2JMS-Serverプロセスのメインスレッドが待機中の場合は<code>true</code>を返します。
     * 
     * @return S2JMS-Serverプロセスのメインスレッドが待機中の場合は<code>true</code>
     */
    public static boolean isWaiting() {
        return latch.getCount() > 0;
    }

    /**
     * S2JMS-Serverプロセスを開始し、シャットダウンまで待機します。
     * 
     * @param args
     *            コマンドライン引数
     * @throws Exception
     *             S2JMS-Serverプロセスの初期化中に例外が発生した場合にスローされます
     */
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
        logger.log(Level.INFO, "IJMS-SERVER3001");

        try {
            latch.await();
        } catch (final InterruptedException ignore) {
            destoryS2Container();
        }
    }

    /**
     * S2コンテナを構築します。
     * 
     * @param dicon
     *            ルートとなるdiconファイルのパス名
     * @return S2コンテナ
     * @throws Exception
     *             S2コンテナの作成中に例外が発生した場合にスローされます
     */
    protected Object createS2Container(final String dicon) throws Exception {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final Class<?> clazz = classLoader.loadClass(JMS_CONTAINER_INITIALIZER);
        final Constructor<?> ctor = clazz.getConstructor(String.class);
        final Callable<?> jmsContainerInitializer = Callable.class.cast(ctor.newInstance(dicon));
        return jmsContainerInitializer.call();
    }

    /**
     * S2コンテナを破棄します。
     * 
     */
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

    /**
     * コマンドライン引数で指定されたdiconファイルのパス名を返します。
     * <p>
     * コマンドライン引数でパスが指定されなかった場合はデフォルトの<code>app.dicon</code>を返します。
     * </p>
     * 
     * @param args
     *            コマンドライン引数
     * @return diconファイルのパス名
     * @throws IllegalArgumentException
     *             コマンドライン引数が不正の場合にスローされます
     */
    protected String getDicon(final String[] args) throws IllegalArgumentException {
        final String dicon = getArg("--dicon", args);
        return dicon.equals("") ? DEFAULT_DICON_FILE : dicon;
    }

    /**
     * コマンドライン引数で指定された追加のクラスパスを返します。
     * <p>
     * コマンドライン引数で追加のクラスパスが指定されなかった場合はデフォルトの<code>.</code>を返します。
     * </p>
     * 
     * @param args
     *            コマンドライン引数
     * @return 追加のクラスパス
     * @throws IllegalArgumentException
     *             コマンドライン引数が不正の場合にスローされます
     */
    protected String getClasspath(final String[] args) throws IllegalArgumentException {
        final String classpath = getArg("--classpath", args);
        return classpath.equals("") ? "." : classpath;
    }

    /**
     * コマンドライン引数から指定されたキーに対応する値を返します。
     * 
     * @param name
     *            コマンドライン引数の名前
     * @param args
     *            コマンドライン引数
     * @return コマンドライン引数
     */
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

    /**
     * クラスパスを構築します。
     * 
     * @param pathStrings
     *            クラスパス文字列
     * @throws IOException
     *             IO処理中に例外が発生した場合にスローされます
     */
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

    /**
     * このクラスを含んでいるS2JMS-ServerのJarファイルを返します。
     * 
     * @return このクラスを含んでいるS2JMS-ServerのJarファイル
     */
    protected File getBootstrapJarFile() {
        try {
            final URL url = getClass().getClassLoader().getResource(BOOTSTRAP_CLASS_FILE_NAME);
            final JarURLConnection con = (JarURLConnection) url.openConnection();
            return new File(con.getJarFileURL().toURI());
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * クラスパスを表すURLの配列にパスを追加します。
     * <p>
     * パスがディレクトリの場合で、そのディレクトリ直下にJarファイルが存在する場合はそれらJarファイル全てがクラスパスに追加されます。
     * パスがディレクトリの場合で、そのディレクトリ直下にJarファイルが存在しない場合はそのディレクトリ自身が暮らすパスに追加されます。
     * パスがファイルの場合はそのファイルがクラスパスに追加されます。
     * </p>
     * 
     * @param urls
     *            クラスパスを表すURLの配列
     * @param path
     *            ディレクトリまたはファイルを表すパス
     * @throws IOException
     *             IO処理中に例外が発生した場合にスローされます
     */
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

    /**
     * 指定されたディレクトリ直下にあるJarファイルの配列を返します。
     * 
     * @param dir
     *            ディレクトリ
     * @return Jarファイルの配列
     */
    protected File[] getJarFiles(final File dir) {
        return dir.listFiles(new FileFilter() {

            public boolean accept(final File pathname) {
                return isJar(pathname);
            }
        });
    }

    /**
     * ファイルがJarなら<code>true</code>を返します。
     * 
     * @param pathname
     *            ファイル
     * @return ファイルがJarなら<code>true</code>
     */
    protected boolean isJar(final File pathname) {
        final int dot = pathname.getName().lastIndexOf('.');
        if (0 <= dot) {
            final String extention = pathname.getName().substring(dot + 1);
            return "jar".equalsIgnoreCase(extention);
        }
        return false;
    }

}
