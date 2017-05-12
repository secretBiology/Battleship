package com.secretbiology.battleship.setup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.secretbiology.battleship.GameConstants;
import com.secretbiology.battleship.R;
import com.secretbiology.battleship.arrange.ArrangeShips;
import com.secretbiology.battleship.common.GameDetails;
import com.secretbiology.battleship.common.GameState;
import com.secretbiology.battleship.common.Player;
import com.secretbiology.helpers.general.General;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.string.ok;

public class NewGame extends AppCompatActivity implements ValueEventListener {

    public static final String ACTION_JOIN = "join";
    public static final String ACTION_CREATE = "create";


    @BindViews({R.id.gs_p1_name, R.id.gs_p2_name})
    List<TextView> names;
    @BindViews({R.id.gs_p1_btn, R.id.gs_p2_btn})
    List<Button> buttons;
    @BindView(R.id.gs_gameid)
    TextView gameID;
    @BindViews({R.id.gs_p1_email, R.id.gs_p2_email})
    List<TextView> emails;
    @BindViews({R.id.gs_player1, R.id.gs_player2})
    List<ImageView> icons;


    static boolean active = false;
    private GameState state = GameState.getInstance();
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        ButterKnife.bind(this);
        setTitle(getString(R.string.game_create));
        myRef = FirebaseDatabase.getInstance().getReference(GameConstants.GAME_LOBBY);
        myRef.child(state.getGameDetails().getGameID()).addValueEventListener(this);
        updateUI();
    }

    private void updateUI() {
        gameID.setText(state.getGameDetails().getGameID());
        setForPlayer(state.getPlayer());
        setForPlayer(state.getEnemy());
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        if (active) {
            if (dataSnapshot.getValue() != null) {
                GameDetails details = dataSnapshot.getValue(GameDetails.class);
                state.updatePlayerStatus(details);
                updateUI();
                if (state.getPlayer().isReady() && state.getEnemy().isReady()) {
                    myRef.child(state.getGameDetails().getGameID()).removeEventListener(this);
                    myRef.child(state.getGameDetails().getGameID()).removeValue();
                    startActivity(new Intent(this, ArrangeShips.class));
                    finish();
                }
            } else {
                new AlertDialog.Builder(NewGame.this)
                        .setTitle("Oops")
                        .setMessage(getString(R.string.game_cancelled))
                        .setCancelable(false)
                        .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void setForPlayer(Player player) {
        names.get(player.getNo()).setText(player.getName());
        emails.get(player.getNo()).setText(player.getEmail());
        buttons.get(player.getNo()).setBackgroundColor(General.getColor(getBaseContext(), R.color.colorPrimary));
        if (state.getEnemy().getNo() == player.getNo()) {
            buttons.get(player.getNo()).setText(getString(R.string.waiting_player));
        }
        if (player.isReady()) {
            buttons.get(player.getNo()).setText(getString(R.string.is_ready));
            buttons.get(player.getNo()).setBackgroundColor(General.getColor(getBaseContext(), R.color.green));
        }
    }

    @OnClick({R.id.gs_p1_btn, R.id.gs_p2_btn})
    public void ready(View v) {
        if (v == buttons.get(state.getPlayer().getNo())) {
            state.getPlayer().setReady(!state.getPlayer().isReady());
            myRef.child(state.getGameDetails().getGameID()).child(readyNode()).setValue(state.getPlayer().isReady());
            updateUI();
        }
    }

    private String readyNode() {
        if (state.getPlayer().getNo() == 0) {
            return GameConstants.NETWORK_PLAYER_1 + "/ready";
        } else {
            return GameConstants.NETWORK_PLAYER_2 + "/ready";
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
        myRef.child(state.getGameDetails().getGameID()).removeEventListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.child(state.getGameDetails().getGameID()).removeEventListener(this);
        if (!isGameStarted()) {
            if (getIntent().getAction().equals(ACTION_JOIN)) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("full", false);
                map.put("player2", null);
                myRef.child(state.getGameDetails().getGameID()).updateChildren(map);
            } else {
                myRef.child(state.getGameDetails().getGameID()).removeValue();
            }
        }
    }

    private boolean isGameStarted() {
        return state.getPlayer().isReady() && state.getEnemy().isReady();
    }

}
