package com.secretbiology.battleship.arrange;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.secretbiology.battleship.R;
import com.secretbiology.helpers.general.General;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

public class ShipFragment extends BottomSheetDialogFragment {

    private OnShipSelected selected;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
            new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            };

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.select_ships_fragment, null);
        dialog.setContentView(contentView);
        setUpHeight(contentView);

        TextView title = ButterKnife.findById(dialog, R.id.bm_fragment_title);
        RecyclerView recyclerView = ButterKnife.findById(dialog, R.id.bm_fragment_recycler);

        List<ShipModel> models = new ArrayList<>(ArrangeShips.modelList);
        ShipSelectionAdapter adapter = new ShipSelectionAdapter(models);

        title.setText(getString(R.string.available_ships, models.size()));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        adapter.setOnShipSelect(new ShipSelectionAdapter.OnItemSelect() {
            @Override
            public void onClick(int position) {
                selected.selected(position);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            selected = (OnShipSelected) context;
        } catch (ClassCastException castException) {
            /* The activity does not implement the listener. */
            General.makeLongToast(context, "Unable to start activity!");
        }
    }

    private void setUpHeight(View contentView) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        View parent = (View) contentView.getParent();
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        int screenHeight = (int) getContext().getResources().getDimension(R.dimen.bottom_sheet_height);
        bottomSheetBehavior.setPeekHeight(screenHeight);
        params.height = screenHeight;
        parent.setLayoutParams(params);
    }

    interface OnShipSelected {
        void selected(int position);
    }
}
