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
package org.seasar.jms.core.message.text;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author koichik
 */
public class VelocityTextFormatterTest extends TestCase {
    public VelocityTextFormatterTest() {
    }

    public VelocityTextFormatterTest(String name) {
        super(name);
    }

    public void test() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register("Hoge", "hoge");
        container.register("FOO", "foo");
        container.register("BAR", "bar");
        container.register(new Dto("Yuri", 26), "dto");
        container.init();

        VelocityTextFormatter formatter = new VelocityTextFormatter();
        formatter.setContainer(container);

        formatter.setTemplateText("hello, world");
        assertEquals("1", "hello, world", formatter.getText());

        formatter.setTemplateText("hello, $hoge");
        assertEquals("2", "hello, Hoge", formatter.getText());

        formatter.setTemplateText("$hoge $foo $bar $baz");
        assertEquals("3", "Hoge FOO BAR $baz", formatter.getText());

        formatter.setTemplateText("$dto.name $dto.age $dto.birthday");
        assertEquals("4", "Yuri 26 $dto.birthday", formatter.getText());
    }

    public static class Dto {
        String name;
        int age;

        public Dto(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
