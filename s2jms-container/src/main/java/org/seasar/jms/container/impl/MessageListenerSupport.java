/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jms.Message;

import org.seasar.framework.aop.javassist.AspectWeaver;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.StringUtil;
import org.seasar.jms.container.annotation.JMSBody;
import org.seasar.jms.container.annotation.JMSHeader;
import org.seasar.jms.container.annotation.JMSPayload;
import org.seasar.jms.container.annotation.JMSProperty;
import org.seasar.jms.container.annotation.OnMessage;
import org.seasar.jms.container.binder.Binder;
import org.seasar.jms.container.binder.impl.JMSBodyBinder;
import org.seasar.jms.container.binder.impl.JMSHeaderBinder;
import org.seasar.jms.container.binder.impl.JMSPropertiesBinder;
import org.seasar.jms.container.binder.impl.JMSPropertyBinder;
import org.seasar.jms.container.exception.IllegalMessageListenerException;
import org.seasar.jms.container.exception.MessageListenerNotFoundException;
import org.seasar.jms.container.exception.NotSupportedMessageException;
import org.seasar.jms.core.message.MessageHandler;
import org.seasar.jms.core.message.impl.MessageHandlerFactory;
import org.seasar.jms.core.util.MessageHandlerUtil;

/**
 * メッセージリスナコンポーネントへのJMSメッセージのバインドや、リスナメソッドの呼び出しを行うクラスです。
 * 
 * @author koichik
 */
public class MessageListenerSupport {

    // constants
    /** デフォルトのリスナメソッド名 */
    public static final String DEFAULT_MESSAGE_HANDLER_NAME = "onMessage";

    // static fields
    private static final Logger logger = Logger.getLogger(JMSContainerImpl.class);

    // instance fields
    /** リスナメソッドにJMSメッセージをバインドするコンポーネントの配列 */
    protected final List<Binder> binders = new ArrayList<Binder>();

    /** リスナコンポーネントのリスナメソッド */
    protected Method method;

    /** リスナメソッドの引数に対応したビルダ */
    protected ParameterBuilder parameterBuilder;

    /**
     * インスタンスを構築します。
     * 
     * @param clazz
     *            リスナコンポーネントのクラス
     */
    public MessageListenerSupport(final Class<?> clazz) {
        final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
        setupBinderFromField(beanDesc);
        setupBinderFromProperty(beanDesc);
        setupListenerMethod(clazz);
    }

    /**
     * リスナコンポーネントにJMSメッセージをバインドします。
     * 
     * @param listener
     *            リスナコンポーネント
     * @param message
     *            JMSメッセージ
     * @param payload
     *            JMSメッセージのペイロード
     */
    public void bind(final Object listener, final Message message, final Object payload) {
        for (final Binder binder : binders) {
            binder.bind(listener, message, payload);
        }
    }

    /**
     * リスナコンポーネントのリスナメソッドを呼び出します。
     * 
     * @param listener
     *            リスナコンポーネント
     * @param message
     *            JMSメッセージ
     * @throws Exception
     *             リスナコンポーネントで例外が発生した場合にスローされます
     */
    public void invoke(final Object listener, final Message message) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.log("DJMS-CONTAINER2103", new Object[] { method });
        }
        try {
            final Object[] parameters = parameterBuilder.build(message);
            method.invoke(listener, parameters);
        } finally {
            if (logger.isDebugEnabled()) {
                logger.log("DJMS-CONTAINER2104", new Object[] { method });
            }
        }
    }

    /**
     * リスナメソッドの名前を返します。
     * 
     * @return リスナメソッドの名前
     */
    public String getListenerMethodName() {
        return method.getName();
    }

    /**
     * リスナコンポーネントのフィールドからバインダを準備します。
     * 
     * @param beanDesc
     *            リスナコンポーネントの{@link BeanDesc}
     */
    protected void setupBinderFromField(final BeanDesc beanDesc) {
        for (int i = 0; i < beanDesc.getFieldSize(); ++i) {
            final Field field = beanDesc.getField(i);

            final JMSHeader header = field.getAnnotation(JMSHeader.class);
            if (header != null) {
                final BindingType bindingType = header.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(header.name()) ? field.getName()
                            : header.name();
                    binders.add(new JMSHeaderBinder(name, bindingType, field));
                }
                continue;
            }

            final JMSProperty property = field.getAnnotation(JMSProperty.class);
            if (property != null) {
                final BindingType bindingType = property.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(property.name()) ? field.getName()
                            : property.name();
                    if (field.getType() == Map.class) {
                        binders.add(new JMSPropertiesBinder(name, bindingType, field));
                    } else {
                        binders.add(new JMSPropertyBinder(name, bindingType, field));
                    }
                }
                continue;
            }

            final JMSBody body = field.getAnnotation(JMSBody.class);
            if (body != null) {
                final BindingType bindingType = body.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(body.name()) ? field.getName() : body
                            .name();
                    binders.add(new JMSBodyBinder(name, bindingType, field));
                }
                continue;
            }

            final JMSPayload payload = field.getAnnotation(JMSPayload.class);
            if (payload != null) {
                final BindingType bindingType = payload.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(payload.name()) ? field.getName()
                            : payload.name();
                    binders.add(new JMSBodyBinder(name, bindingType, field));
                }
                continue;
            }
        }
    }

    /**
     * リスナコンポーネントのプロパティからバインダを準備します。
     * 
     * @param beanDesc
     *            リスナコンポーネントの{@link BeanDesc}
     */
    protected void setupBinderFromProperty(final BeanDesc beanDesc) {
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            final PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            if (!propertyDesc.hasWriteMethod()) {
                continue;
            }
            final Method method = propertyDesc.getWriteMethod();

            final JMSHeader header = method.getAnnotation(JMSHeader.class);
            if (header != null) {
                final BindingType bindingType = header.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(header.name()) ? propertyDesc
                            .getPropertyName() : header.name();
                    binders.add(new JMSHeaderBinder(name, bindingType, propertyDesc));
                }
                continue;
            }

            final JMSProperty property = method.getAnnotation(JMSProperty.class);
            if (property != null) {
                final BindingType bindingType = property.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(property.name()) ? propertyDesc
                            .getPropertyName() : property.name();
                    if (propertyDesc.getPropertyType() == Map.class) {
                        binders.add(new JMSPropertiesBinder(name, bindingType, propertyDesc));
                    } else {
                        binders.add(new JMSPropertyBinder(name, bindingType, propertyDesc));
                    }
                }
                continue;
            }

            final JMSBody body = method.getAnnotation(JMSBody.class);
            if (body != null) {
                final BindingType bindingType = body.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(body.name()) ? propertyDesc
                            .getPropertyName() : body.name();
                    binders.add(new JMSBodyBinder(name, bindingType, propertyDesc));
                }
                continue;
            }

            final JMSPayload payload = method.getAnnotation(JMSPayload.class);
            if (payload != null) {
                final BindingType bindingType = payload.bindingType();
                if (bindingType != BindingType.NONE) {
                    final String name = StringUtil.isEmpty(payload.name()) ? propertyDesc
                            .getPropertyName() : payload.name();
                    binders.add(new JMSBodyBinder(name, bindingType, propertyDesc));
                }
                continue;
            }
        }
    }

    /**
     * リスナコンポーネントのリスナメソッドを準備します。
     * 
     * @param clazz
     *            リスナコンポーネントのクラス
     */
    protected void setupListenerMethod(final Class<?> clazz) {
        final Class<?> targetClass = clazz.getName().contains(AspectWeaver.SUFFIX_ENHANCED_CLASS) ? clazz
                .getSuperclass()
                : clazz;
        for (final Method method : targetClass.getMethods()) {
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final OnMessage annotation = method.getAnnotation(OnMessage.class);
            if (annotation == null) {
                continue;
            }
            parameterBuilder = getParameterBuilder(method);
            this.method = method;
            return;
        }

        try {
            final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(targetClass);
            final Method[] methods = beanDesc.getMethods(DEFAULT_MESSAGE_HANDLER_NAME);
            for (int i = 0; i < methods.length; ++i) {
                final Method method = methods[i];
                final Class<?>[] parameters = method.getParameterTypes();
                if (parameters.length <= 1) {
                    parameterBuilder = getParameterBuilder(method);
                    this.method = method;
                    return;
                }
            }
        } catch (final MethodNotFoundRuntimeException ignore) {
        }
        throw new MessageListenerNotFoundException(targetClass);
    }

    /**
     * リスナメソッドの引数に対応したビルダを返します。
     * 
     * @param method
     *            リスナメソッド
     * @return リスナメソッドの引数に対応したビルダ
     */
    protected ParameterBuilder getParameterBuilder(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            return new EmptyBuilder();
        }
        if (parameterTypes.length == 1) {
            final Class<?> parameterType = parameterTypes[0];
            if (Message.class.isAssignableFrom(parameterType)) {
                return new MessageBuilder();
            }
            return new PayloadBuilder(parameterType);
        }
        throw new IllegalMessageListenerException(method);
    }

    /**
     * JMSメッセージからリスナメソッドの引数を構築するビルダのインタフェースです。
     * 
     * @author koichik
     */
    public interface ParameterBuilder {

        /**
         * JMSメッセージからリスナメソッドの引数の配列を構築して返します。
         * <p>
         * 配列の長さはリスナメソッドの引数の数と同じで0または1のみサポートしています。
         * </p>
         * 
         * @param message
         *            JMSメッセージ
         * @return リスナメソッドの引数の配列
         */
        Object[] build(Message message);

    }

    /**
     * リスナメソッドに引数がない場合に使われるビルダです。
     * 
     * @author koichik
     */
    public static class EmptyBuilder implements ParameterBuilder {

        /** 空の配列 */
        protected static final Object[] EMPTY_ARRAY = new Object[0];

        public Object[] build(final Message message) {
            return EMPTY_ARRAY;
        }

    }

    /**
     * リスナメソッドの引数が{@link Message}の場合に使われるビルダです。
     * 
     * @author koichik
     */
    public static class MessageBuilder implements ParameterBuilder {

        public Object[] build(final Message message) {
            return new Object[] { message };
        }

    }

    /**
     * リスナメソッドの引数が{@link Message}以外の場合に使われるビルダです。
     * <p>
     * リスナメソッドの引数型はJMSメッセージのペイロード型を代入可能でなくてはなりません。
     * </p>
     * 
     * @author koichik
     */
    public static class PayloadBuilder implements ParameterBuilder {

        /** JMSメッセージのペイロード型 */
        protected Class<?> payloadType;

        /**
         * インスタンスを構築します。
         * 
         * @param payloadType
         *            JMSメッセージのペイロード型
         */
        public PayloadBuilder(final Class<?> payloadType) {
            this.payloadType = payloadType;
        }

        public Object[] build(final Message message) {
            final MessageHandler<? extends Message, ?> handler = MessageHandlerFactory
                    .getMessageHandlerFromPayloadType(payloadType);
            if (handler == null) {
                throw new NotSupportedMessageException(message);
            }
            return new Object[] { MessageHandlerUtil.getPayload(handler, message) };
        }

    }

}
