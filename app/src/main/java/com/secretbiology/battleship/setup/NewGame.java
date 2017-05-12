package com.secretbiology.battleship.setup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.secretbiology.battleship.R;
import com.secretbiology.battleship.common.GameState;

import butterknife.ButterKnife;

public class NewGame extends AppCompatActivity {

    private GameState state = GameState.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        ButterKnife.bind(this);
        setTitle(getString(R.string.game_create));
    }
}
