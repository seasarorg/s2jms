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

import junit.framework.TestCase;

/**
 * @author bowez
 * 
 */
public class BootstrapTest extends TestCase {
    public void testGetDicon() {
        assertEquals("test.dicon", new Bootstrap().getDicon(new String[] {"--dicon", "test.dicon" }));
    }
    
    public void testGetDicon2() {
        assertEquals("test.dicon", 
                new Bootstrap().getDicon(new String[] {"--classpath", "/usr/local/foo/bar", "--dicon", "test.dicon"}));
    }
    
    public void testGetDiconDefault() {
        assertEquals("app.dicon", new Bootstrap().getDicon(new String[] {"--classpath", "/usr/local/foo/bar" }));
    }
    
    public void testGetDiconError() {
        try {
            new Bootstrap().getDicon(new String[] {"--dicon"});
            fail();
        } 
        catch (IllegalArgumentException e) {
        }
    }
    
    public void testIsJar() {
        assertTrue(new Bootstrap().isJar(new File("/foo/var/baz.jar")));
        assertTrue(new Bootstrap().isJar(new File("/foo/var/baz.JAR")));
        assertTrue(new Bootstrap().isJar(new File("/foo/var/baz.Jar")));
        assertTrue(new Bootstrap().isJar(new File("/foo/var/baz.Jar")));
    }
}