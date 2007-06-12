package org.seasar.jms.blank.listener;

import org.seasar.jms.container.annotation.JMSPayload;
import org.seasar.jms.container.annotation.OnMessage;

public class EchoListener {

    @JMSPayload
    protected String text;

    public EchoListener() {
    }

    @OnMessage
    public void echo() {
        System.out.println("★★★ " + text + " ★★★");
    }
}
