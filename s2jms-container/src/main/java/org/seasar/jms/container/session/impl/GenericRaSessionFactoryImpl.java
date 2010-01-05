/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.jms.container.session.impl;

import java.lang.reflect.Field;

import javax.jms.JMSException;
import javax.jms.Session;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.FieldUtil;
import org.seasar.jca.work.WorkWrapper;
import org.seasar.jms.core.exception.SJMSRuntimeException;
import org.seasar.jms.core.session.SessionFactory;
import org.seasar.jms.core.session.SessionHandler;

/**
 * Java.netで公開されている<a href="https://genericjmsra.dev.java.net/">Generic
 * Resource Adapter for JMS</a>を利用している場合に，現在処理中のメッセージを受信した{@link javax.jms.Session}を提供する
 * {@link org.seasar.jms.core.session.SessionFactory}の実装クラスです．
 * <p>
 * このクラスは，Generic Resource Adapter for JMSとIBM WebSphere MQを組み合わせた場合に
 * メッセージリスナーからWebSphere MQへメッセージを送信する場合に使うことを意図しています．
 * </p>
 * <p>
 * IBM WebSphere MQは，メッセージを受信した{@link javax.jms.Session}に対するXAトランザクションが
 * 開始されている状態で，メッセージを送信するために別の{@link javax.jms.Session}に対するXAトランザクションを
 * {@link javax.transaction.xa.XAResource#TMJOIN}で開始しようとするとWebSphere
 * MQのJMS実装ライブラリ内でロック待ちとなって固まってしまいます．そのため，メッセージリスナーからメッセージを
 * 送信するには現在処理中のメッセージを受信した{@link javax.jms.Session}を利用する必要があります．
 * </p>
 * <p>
 * そのために本クラスでは，現在処理中の{@link javax.resource.spi.work.Work}（実体は Generic RA
 * の実装クラス)からリフレクションを使って{@link javax.jms.Session}を取得します．
 * </p>
 * <p>
 * この実装はGeneric RA 1.5を対象にしています．
 * </p>
 * 
 * @author koichik
 * 
 */
public class GenericRaSessionFactoryImpl implements SessionFactory {

    public void operateSession(final SessionHandler handler) {
        final Object work = WorkWrapper.getCurrentWork();
        final BeanDesc workDesc = BeanDescFactory.getBeanDesc(work.getClass());
        final Field jmsResourceField = workDesc.getField("jmsResource");
        final Object jmsResource = FieldUtil.get(jmsResourceField, work);
        final BeanDesc jmsResourceDesc = BeanDescFactory.getBeanDesc(jmsResource.getClass());
        final Field sessionField = jmsResourceDesc.getField("session");
        final Session session = Session.class.cast(FieldUtil.get(sessionField, jmsResource));
        try {
            handler.handleSession(session);
        } catch (final JMSException e) {
            throw new SJMSRuntimeException("EJMS0001", new Object[] { e }, e);
        }
    }

}
