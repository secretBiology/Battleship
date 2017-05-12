package com.secretbiology.battleship.network;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.secretbiology.battleship.GameConstants;
import com.secretbiology.battleship.arrange.BoardItem;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class FirebaseMethods {

    public static void sendData(List<BoardItem> data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GameConstants.BASE_DATABASE_NODE);
        List<Integer> integers = new ArrayList<>();
        for (BoardItem item : data) {
            integers.add(item.getType());
        }
        myRef.setValue(integers);
    }

    public static void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GameConstants.BASE_DATABASE_NODE);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.inform(dataSnapshot);
                Log.inform(s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
