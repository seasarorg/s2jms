package org.seasar.jms.core.deploy.impl;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionFactory;

import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.jca.deploy.ResourceAdapterDeployer;
import org.seasar.jca.deploy.impl.ManagedConnectionFactoryDeployer;

/**
 * JMS用の{@link ManagedConnectionFactory}をデプロイするクラスです．
 * 
 * @author koichik
 */
public class JMSManagedConnectionFactoryDeployer extends ManagedConnectionFactoryDeployer implements
        ConnectionFactory {

    /** コネクションファクトリ */
    protected ConnectionFactory connectionFactory;

    /**
     * インスタンスを構築します．
     * 
     * @param raDeployer
     *            リソースアダプタ・デプロイヤ
     */
    public JMSManagedConnectionFactoryDeployer(final ResourceAdapterDeployer raDeployer) {
        super(raDeployer);
    }

    @InitMethod
    public void initialize() throws ResourceException {
        connectionFactory = ConnectionFactory.class.cast(createConnectionFactory());
    }

    /**
     * JMSのコネクションを作成して返します．
     * 
     * @return JMSコネクション
     */
    public Connection createConnection() throws JMSException {
        return connectionFactory.createConnection();
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
        return connectionFactory.createConnection(user, password);
    }

}
