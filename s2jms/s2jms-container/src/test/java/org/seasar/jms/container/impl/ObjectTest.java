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

import java.io.Serializable;

/**
 * @author Kenichiro Murata
 * 
 */
public class ObjectTest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message = "hello";

    public ObjectTest() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean equals(Object obj) {
        if (null != obj) {
            return obj.equals(this.message);
        } else if (null == this.message) {
            return true;
        }
        return super.equals(obj);
    }

}
