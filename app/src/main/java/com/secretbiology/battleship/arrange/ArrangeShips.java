package com.secretbiology.battleship.arrange;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.secretbiology.battleship.GameConstants;
import com.secretbiology.battleship.R;
import com.secretbiology.battleship.network.FirebaseMethods;
import com.secretbiology.helpers.general.General;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArrangeShips extends AppCompatActivity implements ShipFragment.OnShipSelected, GameConstants {

    @BindView(R.id.selected_recycler)
    RecyclerView selectedRecycler;

    @BindView(R.id.board_recycler)
    RecyclerView boardRecycler;

    @BindView(R.id.rotate_ship)
    ImageView rotateButton;
    @BindView(R.id.show_ships_btn)
    Button showShips;

    public static List<ShipModel> modelList;
    private List<BoardItem> itemList;
    private ShipModel selectedShip;
    private SelectedItemAdapter selectedAdapter;
    private BoardAdapter adapter;
    private SparseArray<ShipModel> shipMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrange_board);
        ButterKnife.bind(this);

        shipMap = new SparseArray<>();
        shipMap.put(0, new ShipModel(0, 2));
        shipMap.put(1, new ShipModel(1, 5));
        shipMap.put(2, new ShipModel(2, 3));
        shipMap.put(3, new ShipModel(3, 3));

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
                } else if (itemList.get(position).getShipID() != -1) {
                    reselectShip(position);
                }
            }
        });

        rotateButton.setVisibility(View.GONE);
        FirebaseMethods.getData();

    }


    @OnClick(R.id.show_ships_btn)
    public void showSheet() {
        if (modelList.size() != 0) {
            BottomSheetDialogFragment bottomSheetDialogFragment = new ShipFragment();
            bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        } else {
            //Start
            FirebaseMethods.sendData(itemList);
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
            showShips.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.green));
        } else {
            showShips.setText(getResources().getQuantityString(R.plurals.select_ships, modelList.size(), modelList.size()));
            showShips.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        }
    }

}
