package com.secretbiology.battleship.game;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.secretbiology.battleship.GameConstants;
import com.secretbiology.battleship.R;
import com.secretbiology.battleship.arrange.BoardAdapter;
import com.secretbiology.battleship.arrange.BoardItem;
import com.secretbiology.battleship.common.GameState;
import com.secretbiology.battleship.common.GameStatus;
import com.secretbiology.battleship.common.Helper;
import com.secretbiology.battleship.common.Player;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static android.R.string.ok;

public class PlayGame extends AppCompatActivity implements ValueEventListener {

    @BindView(R.id.game_recycler)
    RecyclerView recyclerView;
    @BindViews({R.id.p1_back_image, R.id.p2_back_image})
    List<ImageView> backImage;
    @BindViews({R.id.p1_icon, R.id.p2_icon})
    List<ImageView> icons;
    @BindViews({R.id.p1_name, R.id.p2_name})
    List<TextView> names;
    @BindViews({R.id.p1_score, R.id.p2_score})
    List<TextView> score;
    @BindView(R.id.game_title)
    TextView gameTitle;

    private GameState state;
    static boolean active = false;
    private boolean isMyTurn;
    private List<BoardItem> itemList;
    private BoardAdapter adapter;
    private DatabaseReference myRef;
    private Vibrator vibrator;
    private int maxScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_game);
        ButterKnife.bind(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        state = GameState.getInstance();

        maxScore = Helper.getMaxScore();

        itemList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            itemList.add(new BoardItem(GameConstants.CELL_EMPTY));
        }
        adapter = new BoardAdapter(itemList);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 10));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClick(new BoardAdapter.OnItemClick() {
            @Override
            public void selected(int position) {
                if (isMyTurn) {
                    fired(position);
                }
            }
        });
        updatePlayerInfo(state.getPlayer());
        updatePlayerInfo(state.getEnemy());
        updateTurn();
        updateGame();
        myRef = FirebaseDatabase.getInstance().getReference(GameConstants.GAME_RUN);
        myRef.child(state.getGameDetails().getGameID()).addValueEventListener(this);
        if (state.getPlayer().getNo() == 0) {
            FirebaseDatabase.getInstance().getReference(GameConstants.GAME_ARRANGE).child(state.getGameDetails().getGameID()).removeValue();
        }
    }

    private void updatePlayerInfo(Player player) {
        names.get(player.getNo()).setText(player.getName());
        score.get(player.getNo()).setText(String.valueOf(player.getPoints()));
    }

    private void updateGame() {
        updateTurn();
        updatePlayerInfo(state.getPlayer());
        updatePlayerInfo(state.getEnemy());
        itemList.clear();
        if (isMyTurn) {
            gameTitle.setText(getString(R.string.your_turn));
            itemList.addAll(state.getEnemyGrid());
            backImage.get(state.getPlayer().getNo()).setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.green));
            backImage.get(state.getEnemy().getNo()).setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));

        } else {
            gameTitle.setText(getString(R.string.opponent_turn));
            itemList.addAll(state.getPlayerGrid());
            backImage.get(state.getEnemy().getNo()).setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.red));
            backImage.get(state.getPlayer().getNo()).setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        }
        adapter.notifyDataSetChanged();

        if (state.getPlayer().getPoints() >= maxScore || state.getEnemy().getPoints() >= maxScore) {
            showWinner();
        }

    }

    private void updateTurn() {
        isMyTurn = state.getPlayer().getNo() == state.getGameStatus().getCurrentTurn();
        if (isMyTurn) {
            adapter.setEnemy(true);
        } else {
            adapter.setEnemy(false);
        }
    }

    private void fired(int loc) {
        List<BoardItem> grid = state.getEnemyGrid();
        if (grid.get(loc).getType() == GameConstants.CELL_SHIP) {
            grid.get(loc).setType(GameConstants.CELL_DAMAGED_SHIP);
            state.getPlayer().addPoint();
        } else if (grid.get(loc).getType() == GameConstants.CELL_EMPTY) {
            grid.get(loc).setType(GameConstants.CELL_MISSED);
            isMyTurn = !isMyTurn;
            vibrator.vibrate(300);
        }
        if (state.getPlayer().getNo() == 0) {
            state.getGameStatus().updatePlayer2Grid(grid); //Update enemy one
            updateValue(Helper.playerGrid(1), state.getGameStatus().getPlayer2Grid());

        } else {
            state.getGameStatus().updatePlayer1Grid(grid); //Update enemy one
            updateValue(Helper.playerGrid(0), state.getGameStatus().getPlayer1Grid());
        }
        updateGame();
    }

    private void updateValue(String key, Object value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);

        if (isMyTurn) {
            map.put(GameConstants.NETWORK_GAME_TURN, state.getPlayer().getNo());
        } else {
            map.put(GameConstants.NETWORK_GAME_TURN, state.getEnemy().getNo());
        }


        myRef.child(state.getGameDetails().getGameID()).updateChildren(map);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            if (dataSnapshot.getValue() != null) {
                GameStatus s = dataSnapshot.getValue(GameStatus.class);
                state.setGameStatus(s);
                if (state.getPlayer().getNo() == 0) {
                    List<BoardItem> items = Helper.convertToGrid(s.getPlayer1Grid(), s.getPlayer1Ship());
                    state.getEnemy().setPoints(Helper.countScore(items));
                } else {
                    List<BoardItem> items = Helper.convertToGrid(s.getPlayer2Grid(), s.getPlayer2Ship());
                    state.getEnemy().setPoints(Helper.countScore(items));
                }
                updateGame();
            }
        }
        Log.inform(dataSnapshot);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        General.makeLongToast(getBaseContext(), databaseError.getMessage());
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
        myRef.child(state.getGameDetails().getGameID()).addValueEventListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
        myRef.child(state.getGameDetails().getGameID()).removeEventListener(this);
    }

    private void showWinner() {
        new AlertDialog.Builder(PlayGame.this)
                .setTitle("Game Over!")
                .setCancelable(false)
                .setMessage(getString(R.string.winner, state.getPlayer().getName(), state.getPlayer().getPoints(),
                        state.getEnemy().getName(), state.getEnemy().getPoints()))
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteInfo();
                    }
                }).show();
    }

    public void deleteInfo() {
        if (state.getPlayer().getPoints() < state.getEnemy().getPoints()) {
            myRef.child(state.getGameDetails().getGameID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    finish();
                }
            });
        } else {
            finish();
        }
    }
}
