package org.seasar.jms.blank;

import org.seasar.jms.container.annotation.JMSPayload;
import org.seasar.jms.container.annotation.OnMessage;

public class Printer {

    @JMSPayload
    protected String text;

    public Printer() {
    }

    @OnMessage
    public void print() {
        System.out.println(text);
    }
}
