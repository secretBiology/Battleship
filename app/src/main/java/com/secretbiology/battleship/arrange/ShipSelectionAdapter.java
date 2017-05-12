package com.secretbiology.battleship.arrange;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.secretbiology.battleship.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by Dexter for Battleship .
 * Code is released under MIT license
 */

class ShipSelectionAdapter extends RecyclerView.Adapter<ShipSelectionAdapter.ShipView> {

    private List<ShipModel> modelList;
    private OnItemSelect selected;

    ShipSelectionAdapter(List<ShipModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public ShipView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShipView(LayoutInflater.from(parent.getContext()).inflate(R.layout.ship_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ShipView holder, int position) {
        ShipModel model = modelList.get(position);

        for (int i = 0; i < holder.shipParts.size(); i++) {
            if (i < model.getSize()) {
                holder.shipParts.get(i).setVisibility(View.VISIBLE);
            } else {
                holder.shipParts.get(i).setVisibility(View.GONE);
            }
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected.onClick(holder.getLayoutPosition());
            }
        });

    }

    void setOnShipSelect(OnItemSelect s) {
        this.selected = s;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ShipView extends RecyclerView.ViewHolder {
        @BindViews({R.id.ship_part1, R.id.ship_part2, R.id.ship_part3, R.id.ship_part4, R.id.ship_part5})
        List<ImageView> shipParts;

        @BindView(R.id.ship_item_layout)
        LinearLayout layout;

        ShipView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnItemSelect {
        void onClick(int position);
    }
}
