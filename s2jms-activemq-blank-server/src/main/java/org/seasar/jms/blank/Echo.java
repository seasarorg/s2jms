package org.seasar.jms.blank;

import org.seasar.jms.container.annotation.JMSPayload;
import org.seasar.jms.container.annotation.OnMessage;

public class Echo {

    @JMSPayload
    protected String text;

    public Echo() {
    }

    @OnMessage
    public String echo() {
        return text;
    }
}
