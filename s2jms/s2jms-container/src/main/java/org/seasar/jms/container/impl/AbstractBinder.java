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
package org.seasar.jms.container.impl;

import javax.jms.Message;

import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.jms.container.Binder;
import org.seasar.jms.container.BindingSupport;
import org.seasar.jms.container.exception.NotBoundException;

public abstract class AbstractBinder implements Binder {
    private static final Logger logger = Logger.getLogger(JMSHeaderBinder.class);

    protected final String name;
    protected final BindingType bindingType;
    protected final BindingSupport bindingSupport;

    public AbstractBinder(final String name, final BindingType bindingType,
            final BindingSupport bindingSupport) {
        this.name = name;
        this.bindingType = bindingType;
        this.bindingSupport = bindingSupport;
    }

    public void bind(final Object target, final Message message, final Object payload) {
        if (doBind(target, message, payload)) {
            return;
        }
        if (bindingType == BindingType.MUST) {
            throw new NotBoundException(target.getClass().getName(), name);
        } else if (bindingType == BindingType.SHOULD) {
            logger.log("WJMS2003", new Object[] { target.getClass().getName(), name });
        }
    }

    protected abstract boolean doBind(final Object target, final Message message,
            final Object payload);
}
