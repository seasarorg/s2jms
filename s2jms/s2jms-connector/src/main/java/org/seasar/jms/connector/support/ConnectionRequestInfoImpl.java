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
package org.seasar.jms.connector.support;

import javax.resource.spi.ConnectionRequestInfo;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author koichik
 */
public class ConnectionRequestInfoImpl implements ConnectionRequestInfo {
    protected final String user;
    protected final String password;

    public ConnectionRequestInfoImpl(final String user, final String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("user", user).append("password", password)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1979, 1003).append(user).append(password).toHashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof ConnectionRequestInfoImpl)) {
            return false;
        }

        final ConnectionRequestInfoImpl rhs = (ConnectionRequestInfoImpl) object;
        return new EqualsBuilder().append(user, rhs.getUser()).append(password, rhs.getPassword())
                .isEquals();
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
