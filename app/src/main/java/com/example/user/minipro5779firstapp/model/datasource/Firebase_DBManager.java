package com.example.user.minipro5779firstapp.model.datasource;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.user.minipro5779firstapp.model.backend.Backend;
import com.example.user.minipro5779firstapp.model.backend.BackendFactory;
import com.example.user.minipro5779firstapp.model.entities.ClientRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * FireBase manager this app just need to write data
 */
public class Firebase_DBManager implements Backend {
    //for the singleton just BackendFactory can create Firebase_DBManager
    public Firebase_DBManager(BackendFactory.Friend f){
        //if try to create Firebase_DBManager with null param
        //this throw NullPointerException
        f.hashCode();
    }
    //the database reference
    private DatabaseReference clientsRef = FirebaseDatabase.getInstance().getReference("clients");

    //the write function
    public void addRequest(ClientRequest clientRequest, final Context context) {
        /*
          push - to add new key
          setValue - add the clientRequest request
          addOnSuccessListener - make a toast if the write is Succeeded
          addOnFailureListener - make a toast if the write is failed
         */
        clientsRef.push().setValue(clientRequest).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(context, "Success to add the request", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Fail to add the request", Toast.LENGTH_LONG).show();
            }
        });
    }
}

