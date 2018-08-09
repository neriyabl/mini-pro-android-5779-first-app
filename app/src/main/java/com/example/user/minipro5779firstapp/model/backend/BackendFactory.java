package com.example.user.minipro5779firstapp.model.backend;

import com.example.user.minipro5779firstapp.model.datasource.Firebase_DBManager;

public final class BackendFactory {
    static Backend backend = null;

    public static Backend getBackend() {
        if (backend == null)
            backend = new Firebase_DBManager();
        return backend;
    }
}
