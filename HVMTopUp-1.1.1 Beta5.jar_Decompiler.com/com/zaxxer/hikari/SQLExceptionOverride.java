/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.zaxxer.hikari;

import java.sql.SQLException;

public interface SQLExceptionOverride {
    default public Override adjudicate(SQLException sqlException) {
        return Override.CONTINUE_EVICT;
    }

    public static enum Override {
        CONTINUE_EVICT,
        DO_NOT_EVICT;

    }
}

