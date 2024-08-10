/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.zaxxer.hikari.pool;

import com.zaxxer.hikari.pool.ProxyConnection;
import com.zaxxer.hikari.pool.ProxyPreparedStatement;
import java.sql.CallableStatement;

public abstract class ProxyCallableStatement
extends ProxyPreparedStatement
implements CallableStatement {
    protected ProxyCallableStatement(ProxyConnection connection, CallableStatement statement) {
        super(connection, statement);
    }
}

