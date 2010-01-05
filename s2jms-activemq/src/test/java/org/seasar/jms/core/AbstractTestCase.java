/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.jms.core;

import javax.transaction.TransactionManager;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 */
public abstract class AbstractTestCase extends S2TestCase {
    protected TransactionManager tm;
    protected MessageSender sender;
    protected MessageReceiver receiver;
    protected boolean recvSucceeded;

    public AbstractTestCase(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Thread.sleep(500);
    }

    public void doTest() throws Exception {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    recv();
                    recvSucceeded = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread.sleep(2000);
        send();
        thread.join();
        assertTrue("1", recvSucceeded);
    }

    protected void send() throws Exception {
        tm.begin();
        try {
            sender.send("Hoge");
        } finally {
            tm.commit();
        }
    }

    protected void recv() throws Exception {
        tm.begin();
        try {
            assertEquals("2", "Hoge", receiver.receiveText());
        } finally {
            tm.commit();
        }
    }
}
