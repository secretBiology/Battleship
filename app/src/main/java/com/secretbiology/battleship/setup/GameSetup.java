package com.secretbiology.battleship.setup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
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
import com.secretbiology.battleship.common.GameDetails;
import com.secretbiology.battleship.common.GamePref;
import com.secretbiology.battleship.common.GameState;
import com.secretbiology.battleship.common.Player;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.views.ScrollUpRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameSetup extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.swipe_view)
    SwipeRefreshLayout layout;
    @BindView(R.id.game_list_recycler)
    ScrollUpRecyclerView recyclerView;
    @BindView(R.id.empty_text)
    TextView emptyText;

    private DatabaseReference myRef;
    private GamePref prefs;
    private List<GameDetails> games;
    private ProgressDialog progressDialog;
    private GameListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_setup);
        ButterKnife.bind(this);
        setTitle(getString(R.string.game_list));
        progressDialog = new ProgressDialog(GameSetup.this);
        progressDialog.setMessage("Creating new game...");
        progressDialog.setCancelable(false);

        games = new ArrayList<>();
        adapter = new GameListAdapter(games);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        prefs = new GamePref(getBaseContext());
        myRef = FirebaseDatabase.getInstance().getReference(GameConstants.GAME_LOBBY);
        layout.setOnRefreshListener(this);

        getGames();
        layout.setRefreshing(true);

        adapter.setOnGameClick(new GameListAdapter.onGameClick() {
            @Override
            public void gameSelected(int pos) {
                progressDialog.show();
                checkIfFull(pos);
            }
        });
    }

    private void checkIfFull(final int pos) {
        myRef.child(games.get(pos).getGameID()).child("full").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.getValue() != null) {
                        if ((Boolean) dataSnapshot.getValue()) {
                            progressDialog.hide();
                            General.makeLongToast(getBaseContext(), "Oops! game is full.");
                            getGames();

                        } else {
                            enterGame(pos);
                        }
                    }
                } else {
                    progressDialog.hide();
                    General.makeLongToast(getBaseContext(), "This game does not exists!");
                    getGames();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.hide();
                General.makeLongToast(getBaseContext(), "Something went wrong!");
                getGames();
            }
        });
    }

    private void enterGame(int pos) {
        Player player = new Player();
        player.setName(prefs.getName());
        player.setEmail(prefs.getEmail());
        player.setUid(prefs.getUID());
        player.setCreator(false);
        player.setNo(1);
        GameDetails details = games.get(pos);
        details.setFull(true);
        details.setPlayer2(player);
        GameState.initialize(details, 1);
        myRef.child(details.getGameID()).setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.hide();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(GameSetup.this, NewGame.class);
                    intent.setAction(NewGame.ACTION_JOIN);
                    startActivity(intent);
                } else {
                    General.makeLongToast(getBaseContext(), task.getException().getMessage());
                }
            }
        });
    }

    @OnClick(R.id.game_list_fab)
    public void createNewGame() {
        progressDialog.show();
        String gameID = myRef.push().getKey();
        GameDetails details = new GameDetails(gameID);
        Player player = new Player();
        player.setName(prefs.getName());
        player.setEmail(prefs.getEmail());
        player.setUid(prefs.getUID());
        player.setCreator(true);
        player.setNo(0);

        Player enemy = new Player();
        enemy.setName("--");
        enemy.setEmail("--");
        enemy.setUid("--");
        enemy.setCreator(false);
        enemy.setNo(1);

        details.setCreationTimestamp(General.timeStamp());
        details.setPlayer1(player);
        details.setPlayer2(enemy);
        details.setGameName("battleship");
        GameState.initialize(details, 0);

        myRef.child(gameID).setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.hide();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(GameSetup.this, NewGame.class);
                    intent.setAction(NewGame.ACTION_CREATE);
                    startActivity(intent);
                } else {
                    General.makeLongToast(getBaseContext(), task.getException().getMessage());
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        getGames();
    }

    private void getGames() {

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                layout.setRefreshing(false);
                if (dataSnapshot.getValue() != null) {
                    games.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        games.add(snapshot.getValue(GameDetails.class));
                    }
                    if (games.size() != 0) {
                        emptyText.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    emptyText.setVisibility(View.VISIBLE);
                    emptyText.setText(getString(R.string.no_games));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                layout.setRefreshing(false);
                General.makeLongToast(getBaseContext(), databaseError.getMessage());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getGames();
    }
}
