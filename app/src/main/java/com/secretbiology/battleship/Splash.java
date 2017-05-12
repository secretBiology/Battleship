package com.secretbiology.battleship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.secretbiology.battleship.setup.Login;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // new TestGame().initializeTestGame();
        startActivity(new Intent(this, Login.class));
    }
}
