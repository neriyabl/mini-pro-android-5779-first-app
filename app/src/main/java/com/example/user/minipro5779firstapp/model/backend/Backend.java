package com.example.user.minipro5779firstapp.model.backend;

import android.content.Context;

import com.example.user.minipro5779firstapp.model.entities.ClientRequest;

public interface Backend {
    void addRequest(ClientRequest clientRequest, final Context context);
}
