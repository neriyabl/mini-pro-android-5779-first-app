package com.example.user.minipro5779firstapp.model.backend;

import com.example.user.minipro5779firstapp.model.datasource.Firebase_DBManager;

public final class BackendFactory {
    public static final class Friend {private Friend(){}}

    private static Backend backend = null;

    public static Backend getBackend() {
        if (backend == null)
            backend = new Firebase_DBManager(new Friend());
        return backend;
    }
}
