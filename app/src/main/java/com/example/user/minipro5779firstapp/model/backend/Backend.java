package com.example.user.minipro5779firstapp.model.backend;

import android.content.Context;

import com.example.user.minipro5779firstapp.model.entities.Client;

public interface Backend {
    void addRequest(Client client, final Context context);
}
