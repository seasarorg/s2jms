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
package org.seasar.jms.core.interceptor;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Map;

import javax.jms.Message;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.exception.SIllegalStateException;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;
import org.seasar.jms.core.MessageSender;
import org.seasar.jms.core.message.MessageFactory;
import org.seasar.jms.core.message.impl.BytesMessageFactory;
import org.seasar.jms.core.message.impl.MapMessageFactory;
import org.seasar.jms.core.message.impl.ObjectMessageFactory;
import org.seasar.jms.core.message.impl.TextMessageFactory;

/**
 * ターゲットメソッドの戻り値をJMSメッセージとして送信するインターセプタです。
 * <p>
 * ターゲットメソッドが例外をスローすることなく終了した場合、{@link MessageSender}を使用してJMSメッセージを送信します。
 * </p>
 * <p>
 * 送信するJMSメッセージは{@link #addMessageFactory}によって登録された{@link MessageFactory}によって作成されます。
 * {@link MessageFactory}はターゲットメソッドの戻り値型に対応付けることができます。 デフォルトでは次の{@link MessageFactory}が登録されています。
 * </p>
 * <table border="1">
 * <tr>
 * <th>戻り値の型</th>
 * <th>{@link MessageFactory}の実装クラス</th>
 * </tr>
 * <tr>
 * <td>{@link java.lang.String}</td>
 * <td>{@link TextMessageFactory}</td>
 * </tr>
 * <tr>
 * <td>{@code byte[]}</td>
 * <td>{@link BytesMessageFactory}</td>
 * </tr>
 * <tr>
 * <td>{@link java.util.Map}</td>
 * <td>{@link MapMessageFactory}</td>
 * </tr>
 * <tr>
 * <td>{@link java.io.Serializable}</td>
 * <td>{@link ObjectMessageFactory}</td>
 * </tr>
 * </table>
 * <p>
 * どの{@link MessageFactory}が使用されるかは登録される順序に依存します。<br>
 * ターゲットメソッドの戻り値を代入可能な戻り値型に対応付けられている{@link MessageFactory}を
 * 登録順に探し、最初に見つかったものが選択されます。
 * </p>
 * <p>
 * デフォルトで登録されているマッピングの場合、ターゲットメソッドが{@link java.lang.String}を返すと
 * {@link TextMessageFactory}と{@link ObjectMessageFactory}のどちらでもJMSメッセージを作成できますが、
 * 先に登録されている{@link TextMessageFactory}が選択されます。
 * </p>
 * 
 * @author koichik
 */
@Component
public class SendReturnValueInterceptor extends AbstractSendMessageInterceptor {

    // instance fields
    /** {@link MessageFactory}の{@link Map} */
    protected Map<Class<?>, Class<? extends MessageFactory<? extends Message>>> factories = CollectionsUtil
            .newLinkedHashMap();

    /**
     * インスタンスを構築します。
     */
    public SendReturnValueInterceptor() {
        factories.put(String.class, TextMessageFactory.class);
        factories.put(byte[].class, BytesMessageFactory.class);
        factories.put(Map.class, MapMessageFactory.class);
        factories.put(Serializable.class, ObjectMessageFactory.class);
    }

    /**
     * 登録されている{@link MessageFactory}をクリアします。
     * 
     */
    public void clearFactories() {
        factories.clear();
    }

    /**
     * ターゲットメソッドの戻り値型に対応するJMSメッセージのファクトリのクラスを追加します。
     * 
     * @param returnType
     *            ターゲットメソッドの戻り値型
     * @param messageFactoryClass
     *            戻り値型に対応したJMSメッセージのファクトリ
     */
    public void addMessageFactory(final Class<?> returnType,
            final Class<? extends MessageFactory<? extends Message>> messageFactoryClass) {
        factories.put(returnType, messageFactoryClass);
    }

    /**
     * ターゲットメソッドが例外をスローすることなく終了した後にJMSメッセージを送信します。
     * 
     * @param invocation
     *            ターゲットメソッドの呼び出しを表現するオブジェクト
     * @return ターゲットメソッドの戻り値
     * @throws Throwable
     *             ターゲットメソッドの実行時あるいはJMSメッセージの送信時に例外が発生した場合にスローされます
     */
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object result = proceed(invocation);
        if (result != null) {
            getMessageSender().send(createMessageFactory(result));
        }
        return result;
    }

    /**
     * ターゲットメソッドの戻り値型に対応する{@link MessageFactory}を作成して返します。
     * <p>
     * ターゲットメソッドの戻り値を代入可能な戻り値型に対応付けられている{@link MessageFactory}を
     * 登録順に探し、最初に見つかった{@link MessageFactory}をインスタンス化します。
     * </p>
     * 
     * @param returnValue
     *            ターゲットメソッドの戻り値
     * @return ターゲットメソッドの戻り値型に対応する{@link MessageFactory}
     * @throws SIllegalStateException
     *             ターゲットメソッドの戻り値型に対応する{@link MessageFactory}が見つからない場合にスローされます
     */
    protected MessageFactory<?> createMessageFactory(final Object returnValue) {
        final Class<?> returnType = returnValue.getClass();
        for (final Class<?> payloadType : factories.keySet()) {
            if (payloadType.isAssignableFrom(returnType)) {
                final Class<? extends MessageFactory<? extends Message>> factoryClass = factories
                        .get(payloadType);
                final Constructor<? extends MessageFactory<? extends Message>> ctor = ReflectionUtil
                        .getConstructor(factoryClass, payloadType);
                return ReflectionUtil.newInstance(ctor, returnValue);
            }
        }
        throw new SIllegalStateException("EJMS-CORE1003", new Object[] { returnType });
    }

}
