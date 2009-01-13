/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.jms.container.filter.impl;

import javax.jms.Message;
import javax.transaction.TransactionManager;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.jms.container.filter.Filter;
import org.seasar.jms.container.filter.FilterChain;

/**
 * リスナコンポーネントまたはフィルタで例外が発生した場合にトランザクションをロールバックするフィルタです。
 * 
 * @author koichik
 */
public class RollBackFilter implements Filter {

    // static filelds
    private static final Logger logger = Logger.getLogger(RollBackFilter.class);

    // instance fields
    /** トランザクションマネージャ */
    @Binding(bindingType = BindingType.MAY)
    protected TransactionManager transactionManager;

    public void doFilter(final Message message, final FilterChain chain) throws Exception {
        try {
            chain.doFilter(message);
        } catch (final Exception e) {
            rollBack();
            throw e;
        } catch (final Error e) {
            rollBack();
            throw e;
        }
    }

    /**
     * トランザクションをロールバックします。
     * 
     */
    protected void rollBack() {
        try {
            if ((transactionManager != null) && (transactionManager.getTransaction() != null)) {
                logger.log("IJMS-CONTAINER2105", new Object[0]);
                transactionManager.setRollbackOnly();
            }
        } catch (final Exception e) {
            logger.log("EJMS-CONTAINER2106", new Object[0], e);
        }
    }

}
