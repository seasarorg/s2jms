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

import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.impl.servlet.ServletRequestHeaderMapComponentDef;
import org.seasar.framework.container.impl.servlet.ServletRequestHeaderValuesMapComponentDef;
import org.seasar.framework.container.impl.servlet.ServletRequestMapComponentDef;
import org.seasar.framework.container.impl.servlet.ServletRequestParameterMapComponentDef;
import org.seasar.framework.container.impl.servlet.ServletRequestParameterValuesMapComponentDef;

/**
 * @author y-komori
 * 
 */
public class JMSExternalContextComponentDefRegister implements
        ExternalContextComponentDefRegister {

    public void registerComponentDefs(final S2Container container) {
        final S2ContainerImpl impl = (S2ContainerImpl) container;
        impl.register0(new JMSRequestComponentDef());
        impl.register0(new JMSMessageComponentDef());
        impl.register0(new ServletRequestMapComponentDef());
        impl.register0(new ServletRequestHeaderMapComponentDef());
        impl.register0(new ServletRequestHeaderValuesMapComponentDef());
        impl.register0(new ServletRequestParameterMapComponentDef());
        impl.register0(new ServletRequestParameterValuesMapComponentDef());
    }
}
