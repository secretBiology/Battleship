package com.secretbiology.battleship.arrange;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.secretbiology.battleship.GameConstants;
import com.secretbiology.battleship.R;
import com.secretbiology.battleship.common.GameState;
import com.secretbiology.battleship.common.GameStatus;
import com.secretbiology.battleship.common.Helper;
import com.secretbiology.battleship.game.PlayGame;
import com.secretbiology.helpers.general.General;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.string.cancel;
import static android.R.string.ok;

public class ArrangeShips extends AppCompatActivity implements ShipFragment.OnShipSelected, GameConstants, ValueEventListener {

    @BindView(R.id.selected_recycler)
    RecyclerView selectedRecycler;

    @BindView(R.id.board_recycler)
    RecyclerView boardRecycler;

    @BindView(R.id.rotate_ship)
    ImageView rotateButton;
    @BindView(R.id.show_ships_btn)
    Button showShips;

    public static List<ShipModel> modelList;
    static boolean active = false;
    private List<BoardItem> itemList;
    private ShipModel selectedShip;
    private SelectedItemAdapter selectedAdapter;
    private BoardAdapter adapter;
    private SparseArray<ShipModel> shipMap;
    private DatabaseReference myRef;
    private GameState state;
    private boolean playerReady;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrange_board);
        ButterKnife.bind(this);
        state = GameState.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference(GameConstants.GAME_RUN);

        shipMap = Helper.getShips();

        modelList = new ArrayList<>();

        for (int i = 0; i < shipMap.size(); i++) {
            modelList.add(shipMap.valueAt(i));
        }

        itemList = new ArrayList<>();

        for (int i = 0; i < BOARD_ROWS * BOARD_COLUMNS; i++) {
            itemList.add(new BoardItem(CELL_EMPTY));
        }

        adapter = new BoardAdapter(itemList);
        boardRecycler.setAdapter(adapter);
        boardRecycler.setLayoutManager(new GridLayoutManager(getBaseContext(), BOARD_COLUMNS));

        selectedAdapter = new SelectedItemAdapter(0);
        selectedRecycler.setAdapter(selectedAdapter);
        selectedAdapter.setOnDirectionChange(new SelectedItemAdapter.OnDirectionChange() {
            @Override
            public void changeDirection() {
                rotateShip();
            }
        });

        adapter.setOnItemClick(new BoardAdapter.OnItemClick() {
            @Override
            public void selected(int position) {
                if (selectedShip != null) {
                    addShipToBoard(position);
                } else if (itemList.get(position).getShipID() != -1 && !playerReady) {
                    reselectShip(position);
                }
            }
        });

        rotateButton.setVisibility(View.GONE);
        myRef.child(state.getGameDetails().getGameID()).addValueEventListener(this);


    }


    @OnClick(R.id.show_ships_btn)
    public void showSheet() {
        if (modelList.size() != 0) {
            BottomSheetDialogFragment bottomSheetDialogFragment = new ShipFragment();
            bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        } else {
            if (!playerReady) {
                showWarning();
            }
        }
    }

    @Override
    public void selected(int position) {
        rotateButton.setVisibility(View.VISIBLE);
        selectedShip = modelList.get(position);
        updateShip();
    }

    private void updateShip() {
        if (selectedShip != null) {
            selectedAdapter.setSize(selectedShip.getSize());
            if (selectedShip.isVertical()) {
                selectedRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            } else {
                selectedRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
            }
            selectedAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.rotate_ship)
    public void rotateShip() {
        selectedShip.toggleDirection();
        updateShip();
    }

    private void addShipToBoard(int pos) {

        if (validatePosition(pos)) {
            for (Integer i : selectedShip.getLocations(pos)) {
                itemList.get(i).setType(CELL_SHIP);
                itemList.get(i).setShipID(selectedShip.getId());
            }
            adapter.notifyDataSetChanged();
            modelList.remove(selectedShip);
            selectedShip = null;
            rotateButton.setVisibility(View.GONE);
            selectedAdapter.setSize(0);
            selectedAdapter.notifyDataSetChanged();
            changeButton();
        }

    }

    private boolean validatePosition(int pos) {
        for (Integer i : selectedShip.getLocations(pos)) {

            if (i >= BOARD_COLUMNS * BOARD_ROWS) {
                General.makeShortToast(getBaseContext(), "Invalid location");
                return false;
            } else if (itemList.get(i).getShipID() != -1) {
                General.makeShortToast(getBaseContext(), "Overlapping not allowed!");
                return false;
            } else if (i % BOARD_ROWS < pos % BOARD_ROWS && !selectedShip.isVertical()) {
                General.makeShortToast(getBaseContext(), "Size exceeds area");
                return false;
            }

        }
        return true;
    }

    private void reselectShip(int position) {
        ShipModel model = shipMap.get(itemList.get(position).getShipID());
        for (BoardItem b : itemList) {
            if (b.getShipID() == model.getId()) {
                b.setShipID(-1);
                b.setType(CELL_EMPTY);
            }
        }
        modelList.add(model);
        selectedShip = model;
        selectedAdapter.setSize(model.getSize());
        selectedAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        rotateButton.setVisibility(View.VISIBLE);
        changeButton();
    }

    private void changeButton() {
        if (modelList.size() == 0) {
            showShips.setText(getString(R.string.start_game));
        } else {
            showShips.setText(getResources().getQuantityString(R.plurals.select_ships, modelList.size(), modelList.size()));
            showShips.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            if (dataSnapshot.getValue() != null) {
                GameStatus status = dataSnapshot.getValue(GameStatus.class);
                if (status.isPlayer1Ready() && status.isPlayer2Ready()) {
                    state.setGameStatus(status);
                    startActivity(new Intent(this, PlayGame.class));
                    finish();
                }
            }

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        General.makeLongToast(getBaseContext(), databaseError.getMessage());
    }

    private void sendDetails() {

        Map<String, Object> map = new HashMap<>();
        map.put(Helper.playerGrid(state.getPlayer().getNo()), Helper.convertCellType(itemList));
        map.put(Helper.playerShip(state.getPlayer().getNo()), Helper.convertShipType(itemList));
        map.put(Helper.playerReady(state.getPlayer().getNo()), true);
        map.put(NETWORK_GAME_ID, state.getGameDetails().getGameID());
        map.put(NETWORK_GAME_TURN, 0);
        myRef.child(state.getGameDetails().getGameID()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                playerReady = true;
                showShips.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.green));
                showShips.setText(getString(R.string.waiting_player));
            }
        });
    }

    private void showWarning() {
        new AlertDialog.Builder(ArrangeShips.this)
                .setTitle("Are you sure?")
                .setMessage(getString(R.string.game_start_warning))
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendDetails();
                    }
                }).setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
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


}
