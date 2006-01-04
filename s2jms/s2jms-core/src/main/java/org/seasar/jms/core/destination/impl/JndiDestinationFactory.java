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
package org.seasar.jms.core.destination.impl;

import java.util.Hashtable;

import javax.jms.Destination;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jndi.JndiContextFactory;

/**
 * @author koichik
 */
@Component(instance = InstanceType.PROTOTYPE)
public class JndiDestinationFactory extends AbstractDestinationFactory {
    protected Hashtable env = JndiContextFactory.ENVIRONMENT;
    protected String name;

    public JndiDestinationFactory() {
    }

    public JndiDestinationFactory(final Hashtable env, final String name) {
        this.env = env;
        this.name = name;
    }

    public Hashtable getEnv() {
        return this.env;
    }

    @Binding(bindingType = BindingType.MAY)
    public void setEnv(final Hashtable env) {
        this.env = env;
    }

    public String getName() {
        return this.name;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    protected Destination createDestination(final Session session) {
        try {
            final InitialContext context = new InitialContext(env);
            try {
                return (Destination) context.lookup(name);
            } finally {
                context.close();
            }
        } catch (final NamingException e) {
            throw new SJMSRuntimeException("EJMS0000", e);
        }
    }
}
