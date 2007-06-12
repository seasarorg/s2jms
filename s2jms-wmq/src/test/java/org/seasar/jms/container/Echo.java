package org.seasar.jms.container;

import javax.jms.Message;

import org.seasar.framework.log.Logger;

public class Echo {

    private static final Logger logger = Logger.getLogger(Echo.class);

    public void onMessage(Message message) throws Exception {
        logger.debug("msg received\n" + message + "\n");
    }

}
