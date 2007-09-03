package org.seasar.jms.core.deploy.impl;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionFactory;

import org.seasar.jca.deploy.ResourceAdapterDeployer;
import org.seasar.jca.deploy.impl.ManagedConnectionFactoryDeployer;
import org.seasar.jms.core.exception.SJMSException;

/**
 * JMS用の{@link ManagedConnectionFactory}をデプロイするクラスです．
 * 
 * @author koichik
 */
public class JMSManagedConnectionFactoryDeployer extends ManagedConnectionFactoryDeployer implements
        ConnectionFactory {

    /**
     * インスタンスを構築します．
     * 
     * @param raDeployer
     *            リソースアダプタ・デプロイヤ
     */
    public JMSManagedConnectionFactoryDeployer(final ResourceAdapterDeployer raDeployer) {
        super(raDeployer);
    }

    /**
     * JMSのコネクションを作成して返します．
     * 
     * @return JMSコネクション
     */
    public Connection createConnection() throws JMSException {
        try {
            return ConnectionFactory.class.cast(createConnectionFactory()).createConnection();
        } catch (final ResourceException e) {
            throw new SJMSException("EJMS0000", e);
        }
    }

    /**
     * JMSのコネクションを作成して返します．
     * 
     * @param user
     *            ユーザ名
     * @param password
     *            パスワード
     * @return JMSコネクション
     */
    public Connection createConnection(final String user, final String password)
            throws JMSException {
        try {
            return ConnectionFactory.class.cast(createConnectionFactory()).createConnection(user,
                    password);
        } catch (final ResourceException e) {
            throw new SJMSException("EJMS0000", e);
        }
    }

}
