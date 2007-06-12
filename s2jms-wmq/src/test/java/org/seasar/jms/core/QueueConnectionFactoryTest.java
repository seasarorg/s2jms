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
package org.seasar.jms.core;

import javax.transaction.TransactionManager;

import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * 
 * @author koichik
 */
public class QueueConnectionFactoryTest extends S2FrameworkTestCase {

    protected TransactionManager tm;
    protected MessageSender sender;
    protected MessageReceiver receiver;
    protected boolean recvSucceeded;

    public QueueConnectionFactoryTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("jms-wmq-outbound.dicon");
        Thread.sleep(500);
    }

    public void test() throws Exception {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    recv();
                    recvSucceeded = true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread.sleep(2000);
        send();
        thread.join();
        assertTrue(recvSucceeded);
    }

    protected void send() throws Exception {
        tm.begin();
        try {
            sender.send("Hoge");
        }
        finally {
            tm.commit();
        }
    }

    protected void recv() throws Exception {
        tm.begin();
        try {
            // Message msg = receiver.receive();
            // System.out.println(msg.getClass());
            // System.out.println(msg);
            assertEquals("Hoge", receiver.receiveText());
        }
        finally {
            tm.commit();
        }
    }

}
